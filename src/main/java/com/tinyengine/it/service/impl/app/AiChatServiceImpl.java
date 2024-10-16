package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.config.AiChatClient;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.IBaseError;
import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.model.dto.OpenAiBodyDto;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.service.app.AiChatService;
import com.tinyengine.it.utils.ExecuteJavaScript;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiChatServiceImpl implements AiChatService {
    @Value("${path.tfScriptPath}")
    private String scriptPath;

    @SystemServiceLog(description = "getAnswerFromAi 获取ai回答")
    @Override
    public Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam) {
        if (aiParam.getMessages().isEmpty()) {
            return Result.failed("Not passing the correct message parameter");
        }
        String model = aiParam.getFoundationModel().get("model");
        if (aiParam.getFoundationModel().get("model").isEmpty()) {
            model = Enums.E_FOUNDATION_MODEL.GPT_35_TURBO.getValue();
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
        ExecuteJavaScript executeJavaScript = new ExecuteJavaScript();
        // 指定 npm 的完整路径
        String npmCommand = "node";
        List<String> command = new ArrayList<>();
        command.add(npmCommand);
        command.add(scriptPath);
        command.add(codes.toString());

        Map<String, Object> schema = executeJavaScript.executeTransForm(command);
        Map<String, Object> result = new HashMap<>();
        result.put("originalResponse", data);
        result.put("replyWithoutCode", replyWithoutCode);
        result.put("schema", schema);

        return Result.success(result);
    }

    private Result<Map<String, Object>> requestAnswerFromAi(List<Map<String, String>> messages, String model) {
        messages = formatMessage(messages);
        AiChatClient aiChatClient = new AiChatClient();
        OpenAiBodyDto openAiBodyDto = new OpenAiBodyDto(model, messages);
        Map<String, Object> response;
        try {
            response = aiChatClient.executeChatRequest(openAiBodyDto);
        } catch (Exception e) {
            return Result.failed("调用AI大模型接口失败");
        }

        // 适配文心一言的响应数据结构，文心的部分异常情况status也是200，需要转为400，以免前端无所适从
        if (response.get("error_code") != null) {
            return Result.failed((IBaseError) response.get("error_msg"));
        }
        if (response.get("error") != null) {
            String code = (String) response.get("code");
            String message = (String) response.get("message");
            return Result.failed(code, message);
        }
        if (Enums.E_FOUNDATION_MODEL.ERNIE_BOT_TURBO.getValue().equals(model)) {
            // 进行转换
            Map<String, Object> responseData = (Map<String, Object>) response.get("data");

            Map<String, Object> openAiResponse = new HashMap<>();
            openAiResponse.put("id", responseData.get("id"));
            openAiResponse.put("object", "text_completion");
            openAiResponse.put("created", System.currentTimeMillis() / 1000); // 设置创建时间戳
            openAiResponse.put("model", responseData.get("model"));

            List<Map<String, Object>> chatgptChoices = new ArrayList<>();
            List<Map<String, Object>> originalChoices = (List<Map<String, Object>>) responseData.get("choices");
            Map<String, Object> originalChoice = originalChoices.get(0);
            Map<String, Object> chatgptChoice = new HashMap<>();
            chatgptChoice.put("text", originalChoice.get("text"));
            chatgptChoice.put("index", originalChoice.get("index"));
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
     * @return 提取的文本
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
     * @return 去除代码后的回复内容
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

    public static int[] getStartAndEnd(String str) {
        // 查找开始标记的位置
        Pattern startPattern = Pattern.compile("```javascript|<template>");
        Matcher startMatcher = startPattern.matcher(str);
        int start = -1;
        if (startMatcher.find()) {
            start = startMatcher.end();
        }

        // 查找结束标记的位置
        Pattern endPattern = Pattern.compile("```|</template>|</script>|</style>");
        Matcher endMatcher = endPattern.matcher(str);
        int end = -1;

        while (endMatcher.find()) {
            if (endMatcher.start() > start) {
                end = endMatcher.start();
            }
        }

        return new int[]{start, end};
    }

    private List<Map<String, String>> formatMessage(List<Map<String, String>> messages) {
        Map<String, String> defaultWords = new HashMap<>();
        defaultWords.put("role", "user");
        defaultWords.put("content", "你是一名前端开发专家，编码时遵从以下几条要求:\n"
                + "###\n"
                + "1. 只使用 element-ui组件库的el-button 和 el-table组件\n"
                + "2. el-table表格组件的使用方式为 <el-table :columns=\"columnData\" :data=\"tableData\"></el-table> columns的columnData表示列数据，其中用title表示列名，field表示表格数据字段； data的tableData表示表格展示的数据。 el-table标签内不得出现子元素\n"
                + "3. 使用vue2技术栈\n"
                + "4. 回复中只能有一个代码块\n"
                + "5. 不要加任何注释\n"
                + "6. el-table标签内不得出现el-table-column\n"
                + "###");

        Pattern pattern = Pattern.compile(".*编码时遵从以下几条要求.*");

        Map<String, String> firstMessage = messages.get(0);
        String role = (String) firstMessage.get("role");
        String content = (String) firstMessage.get("content");

        if (!"user".equals(role)) {
            messages.add(0, defaultWords);
        } else if (!pattern.matcher(content).matches()) {
            firstMessage.put("content", defaultWords.get("content") + "\n" + content);
        }

        return messages;
    }
}
