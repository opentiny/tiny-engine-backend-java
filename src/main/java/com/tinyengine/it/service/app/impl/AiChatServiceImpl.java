
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.IBaseError;
import com.tinyengine.it.config.log.SystemServiceLog;
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
        String answerContent = message.get("content");
        String replyWithoutCode = removeCode(answerContent);
        // 通过二方包将页面转成schema
        String codes = extractCode(answerContent);

        Map<String, Object> schema = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        result.put("originalResponse", data);
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
            return Result.failed((IBaseError) response.get("error_msg"));
        }
        if (response.get("error") != null) {
            String code = (String) response.get("code");
            String message = (String) response.get("message");
            return Result.failed(code, message);
        }
        if (Enums.FoundationModel.ERNIBOT_TURBO.getValue().equals(model)) {
            // 进行转换
            Map<String, Object> responseData = (Map<String, Object>) response.get("data");

            Map<String, Object> openAiResponse = new HashMap<>();
            openAiResponse.put("id", responseData.get("id"));
            openAiResponse.put("object", "text_completion");
            openAiResponse.put("created", System.currentTimeMillis() / 1000);
            // 设置创建时间戳
            openAiResponse.put("model", responseData.get("model"));

            List<Map<String, Object>> chatgptChoices = new ArrayList<>();
            List<Map<String, Object>> originalChoices = (List<Map<String, Object>>) responseData.get("choices");
            Map<String, Object> originalChoice = originalChoices.get(0);
            Map<String, Object> chatgptChoice = new HashMap<>();
            chatgptChoice.put("text", originalChoice.get("text"));
            chatgptChoice.put("index", originalChoice.get("index"));
            chatgptChoice.put("message", originalChoice.get("message"));
            chatgptChoices.add(chatgptChoice);
            openAiResponse.put("choices", chatgptChoices);

            Map<String, Object> chatgptUsage = new HashMap<>();
            chatgptUsage.put("prompt_tokens", responseData.get("input_tokens"));
            chatgptUsage.put("completion_tokens", responseData.get("output_tokens"));
            chatgptUsage.put("total_tokens", responseData.get("total_tokens"));
            openAiResponse.put("usage", chatgptUsage);
            return Result.success(openAiResponse);
        }
        return Result.success(response);
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

        String role = messages.get(0).getRole();
        String content = messages.get(0).getContent();

        List<AiMessages> aiMessages = new ArrayList<>();

        if (!"user".equals(role)) {
            aiMessages.add(0, defaultWords);
        }
        if (!PATTERN_MESSAGE.matcher(content).matches()) {
            AiMessages aiMessagesResult = new AiMessages();
            aiMessagesResult.setContent(defaultWords.getContent() + "\n" + content);
            aiMessages.add(aiMessagesResult);
        }
        return aiMessages;
    }
}
