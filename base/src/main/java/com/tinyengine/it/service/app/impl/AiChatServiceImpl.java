/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.gateway.ai.AiChatClient;
import com.tinyengine.it.model.dto.AiMessages;
import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.model.dto.OpenAiBodyDto;
import com.tinyengine.it.service.app.AiChatService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Ai chat service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class AiChatServiceImpl implements AiChatService {
    private static final Pattern PATTERN_TAG_START = Pattern.compile("```javascript|<template>");
    private static final Pattern PATTERN_TAG_END = Pattern.compile("```|</template>|</script>|</style>");
    private static final Pattern PATTERN_MESSAGE = Pattern.compile(".*编码时遵从以下几条要求.*");

    private AiChatClient aiChatClient = new AiChatClient();

    /**
     * Get start and end int [ ].
     *
     * @param str the str
     * @return the int [ ]
     */
    public static int[] getStartAndEnd(String str) {
        // 查找开始标记的位置
        Matcher startMatcher = PATTERN_TAG_START.matcher(str);
        int start = -1;
        if (startMatcher.find()) {
            start = startMatcher.end();
        }

        // 查找结束标记的位置
        Matcher endMatcher = PATTERN_TAG_END.matcher(str);
        int end = -1;

        while (endMatcher.find()) {
            if (endMatcher.start() > start) {
                end = endMatcher.start();
            }
        }

        return new int[]{start, end};
    }

    @SystemServiceLog(description = "getAnswerFromAi 获取ai回答")
    @Override
    public Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam) {
        if (aiParam.getMessages().isEmpty()) {
            return Result.failed("Not passing the correct message parameter");
        }
        String model = aiParam.getFoundationModel().get("model");
        if (aiParam.getFoundationModel().get("model").isEmpty()) {
            model = Enums.FoundationModel.GPT_35_TURBO.getValue();
        }
        Map<String, Object> data = requestAnswerFromAi(aiParam.getMessages(), model).getData();
        if (data.isEmpty()) {
            return Result.failed("调用AI大模型接口未返回正确数据");
        }
        List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");
        Map<String, String> message = (Map<String, String>) choices.get(0).get("message");

        String answerContent = "";
        String isFinish = "";
        Object finishReason = choices.get(0).get("finish_reason");
        if (finishReason instanceof String) {
            isFinish = (String) finishReason;
        }
        if (!"length".equals(isFinish)) {
            answerContent = message.get("content");
        }

        // 若内容被截断，继续请求AI
        while ("length".equals(isFinish)) {
            String prefix = message.get("content");
            answerContent = answerContent + prefix;

            // 将此部分内容加入消息列表
            Map<String, Object> partialMessage = new HashMap<>();
            AiMessages aiMessages = new AiMessages();
            List<AiMessages> messagesList = aiParam.getMessages();
            aiMessages.setRole("assistant");
            aiMessages.setName("AI");
            aiMessages.setContent(prefix);
            messagesList.add(aiMessages);
            aiParam.setMessages(messagesList);

            // 再次请求AI
            try {
                data = requestAnswerFromAi(aiParam.getMessages(), model).getData();
            } catch (Exception e) {
               throw  new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultMsg());
            }
            choices = (List<Map<String, Object>>) data.get("choices");
            message = (Map<String, String>) choices.get(0).get("message");
            StringBuilder sb = new StringBuilder();
            answerContent = String.valueOf(sb.append(message.get("content")));
            finishReason = choices.get(0).get("finish_reason");
            if (finishReason instanceof String) {
                isFinish = (String) finishReason;
            }
        }


        String replyWithoutCode = removeCode(answerContent);
        // 通过二方包将页面转成schema
        String codes = extractCode(answerContent);

        Map<String, Object> schema = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        AiMessages aiMessagesresult = new AiMessages();
        aiMessagesresult.setContent(answerContent);
        aiMessagesresult.setRole(message.get("role"));
        aiMessagesresult.setName(message.get("name"));
        result.put("originalResponse", aiMessagesresult);
        result.put("replyWithoutCode", replyWithoutCode);
        result.put("schema", schema);

        return Result.success(result);
    }

    private Result<Map<String, Object>> requestAnswerFromAi(List<AiMessages> messages, String model) {
        List<AiMessages> aiMessages = formatMessage(messages);

        OpenAiBodyDto openAiBodyDto = new OpenAiBodyDto(model, aiMessages);
        Map<String, Object> response = aiChatClient.executeChatRequest(openAiBodyDto);
        // 适配文心一言的响应数据结构，文心的部分异常情况status也是200，需要转为400，以免前端无所适从
        if (response.get("error_code") != null) {
            return Result.failed(response.get("error_msg").toString());
        }
        if (response.get("error") != null) {
            String code = (response.get("code") != null) ? response.get("code").toString() : "";
            String message = (response.get("message") != null) ? response.get("message").toString() : "";
            return Result.failed(code, message);
        }
        if (Enums.FoundationModel.ERNIBOT_TURBO.getValue().equals(model)) {
            return modelResultConvet(response);
        }
        return Result.success(response);
    }

    /**
     * 转换模型返回格式
     * <p>
     * 暂且只满足回复中只包括一个代码块的场景
     *
     * @param response ai返回内容
     * @return result 返回结果
     */
    private Result<Map<String, Object>> modelResultConvet(Map<String, Object> response) {

        // 构建返回的 Map 结构
        Map<String, Object> resData = new HashMap<>(response); // Copy original data

        // 构建 choices 数组
        List<Map<String, Object>> choices = new ArrayList<>();
        Map<String, Object> choice = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "assistant");
        message.put("content", (String) response.get("result"));
        message.put("name", "AI");
        choice.put("message", message);
        choices.add(choice);

        // 将 choices 添加到响应中
        resData.put("choices", choices);

        return Result.success(resData);

    }

    /**
     * 提取回复中的代码
     * <p>
     * 暂且只满足回复中只包括一个代码块的场景
     *
     * @param content ai回复的内容
     * @return 提取的文本 string
     */
    public String extractCode(String content) {
        int[] indices = getStartAndEnd(content);
        int start = indices[0];
        int end = indices[1];
        if (start < 0 || end < 0) {
            return "";
        }
        return content.substring(start, end);
    }

    /**
     * 去除回复中的代码
     * <p>
     * 暂且只满足回复中只包括一个代码块的场景
     *
     * @param content ai回复的内容
     * @return 去除代码后的回复内容 string
     */
    public String removeCode(String content) {
        int[] indices = getStartAndEnd(content);
        int start = indices[0];
        int end = indices[1];
        if (start < 0 || end < 0) {
            return content;
        }
        return content.substring(0, start) + "<代码在画布中展示>" + content.substring(end);
    }

    private List<AiMessages> formatMessage(List<AiMessages> messages) {
        AiMessages defaultWords = new AiMessages();
        defaultWords.setRole("user");
        defaultWords.setContent("你是一名前端开发专家，编码时遵从以下几条要求:\n"
                + "###\n"
                + "1. 只使用 element-ui组件库的el-button 和 el-table组件\n"
                + "2. el-table表格组件的使用方式为 <el-table :columns=\"columnData\" :data=\"tableData\"></el-table> "
                + "columns的columnData表示列数据，其中用title表示列名，field表示表格数据字段； data的tableData表示表格展示的数据。 "
                + "el-table标签内不得出现子元素\n"
                + "3. 使用vue2技术栈\n"
                + "4. 回复中只能有一个代码块\n"
                + "5. 不要加任何注释\n"
                + "6. el-table标签内不得出现el-table-column\n"
                + "###");
        defaultWords.setName(messages.get(0).getName());
        String role = messages.get(0).getRole();
        String content = messages.get(0).getContent();

        List<AiMessages> aiMessages = new ArrayList<>();

        if (!PATTERN_MESSAGE.matcher(content).matches()) {
            AiMessages aiMessagesResult = messages.get(0);
            aiMessagesResult.setContent(defaultWords.getContent() + "\n" + content);
        }
        if (!"user".equals(role)) {
            aiMessages.add(0, defaultWords);
        }

        return messages;
    }
}
