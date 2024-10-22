package com.tinyengine.it.config;

import java.util.HashMap;
import java.util.Map;

public class AiChatConfig {
    private static final String OPENAI_API_URL = System.getenv("OPENAI_API_URL") != null ? System.getenv("OPENAI_API_URL") : "https://api.openai.com";
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    private static final String LOCAL_GPT_API_URL = System.getenv("Local_GPT_API_URL") != null ? System.getenv("Local_GPT_API_URL") : "https://dashscope.aliyuncs.com/compatible-mode";
    private static final String LOCAL_GPT_API_KEY = System.getenv("Local_GPT_API_KEY");

    private static final String WENXIN_ACCESS_TOKEN = System.getenv("WENXIN_ACCESS_TOKEN");


    public static Map<String, AiChatConfigData> getAiChatConfig() {
        Map<String, AiChatConfigData> config = new HashMap<>();

        Map<String, String> openaiHeaders = new HashMap<>();
        openaiHeaders.put("Authorization", "Bearer " + OPENAI_API_KEY);

        Map<String, String> localGptHeaders = new HashMap<>();
        localGptHeaders.put("Authorization", "Bearer " + LOCAL_GPT_API_KEY);

        Map<String, String> ernieBotHeaders = new HashMap<>();

        config.put(Enums.E_FOUNDATION_MODEL.GPT_35_TURBO.getValue(), new AiChatConfigData(
                OPENAI_API_URL + "/v1/chat/completions",
                createCommonRequestOption(),
                openaiHeaders,
                "openai"
        ));

        config.put(Enums.E_FOUNDATION_MODEL.Local_GPT.getValue(), new AiChatConfigData(
                LOCAL_GPT_API_URL + "/v1/chat/completions",
                createCommonRequestOption(),
                localGptHeaders,
                "!openai"
        ));

        config.put(Enums.E_FOUNDATION_MODEL.ERNIE_BOT_TURBO.getValue(), new AiChatConfigData(
                "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + WENXIN_ACCESS_TOKEN,
                createCommonRequestOption(),
                ernieBotHeaders,
                "baidu"
        ));

        return config;
    }

    private static HttpRequestOption createCommonRequestOption() {
        return new HttpRequestOption("POST", "json", "json", 10 * 60 * 1000); // 10 minutes
    }

    public static class AiChatConfigData {
        public final String httpRequestUrl;
        public final HttpRequestOption httpRequestOption;
        public final Map<String, String> headers;
        public final String manufacturer;
        public final Map<String, Object> requestBody;

        public AiChatConfigData(String httpRequestUrl, HttpRequestOption httpRequestOption, Map<String, String> headers, String manufacturer) {
            this.httpRequestUrl = httpRequestUrl;
            this.httpRequestOption = httpRequestOption;
            this.headers = headers;
            this.manufacturer = manufacturer;
            this.requestBody = new HashMap<>();
        }
    }

    public static class HttpRequestOption {
        public final String method;
        public final String dataType;
        public final String contentType;
        public final int timeout;

        public HttpRequestOption(String method, String dataType, String contentType, int timeout) {
            this.method = method;
            this.dataType = dataType;
            this.contentType = contentType;
            this.timeout = timeout;
        }
    }
}