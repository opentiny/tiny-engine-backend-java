package com.tinyengine.it.config;

import com.tinyengine.it.common.enums.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Ai chat config.
 *
 * @since 2024-10-20
 */
public class AiChatConfig {
    private static final String OPENAI_API_URL =
        System.getenv("OPENAI_API_URL") != null ? System.getenv("OPENAI_API_URL") : "https://api.openai.com";
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    private static final String LOCAL_GPT_API_URL =
        System.getenv("Local_GPT_API_URL") != null ? System.getenv("Local_GPT_API_URL")
            : "https://dashscope.aliyuncs.com/compatible-mode";
    private static final String LOCAL_GPT_API_KEY = System.getenv("Local_GPT_API_KEY");

    private static final String WENXIN_ACCESS_TOKEN = System.getenv("WENXIN_ACCESS_TOKEN");

    /**
     * Gets AI chat config.
     *
     * @return the AI chat config
     */
    public static Map<String, AiChatConfigData> getAiChatConfig() {
        Map<String, AiChatConfigData> config = new HashMap<>();

        Map<String, String> openaiHeaders = new HashMap<>();
        openaiHeaders.put("Authorization", "Bearer " + OPENAI_API_KEY);

        Map<String, String> localGptHeaders = new HashMap<>();
        localGptHeaders.put("Authorization", "Bearer " + LOCAL_GPT_API_KEY);

        Map<String, String> ernieBotHeaders = new HashMap<>();

        config.put(Enums.E_FOUNDATION_MODEL.GPT_35_TURBO.getValue(),
            new AiChatConfigData(OPENAI_API_URL + "/v1/chat/completions", createCommonRequestOption(), openaiHeaders,
                "openai"));

        config.put(Enums.E_FOUNDATION_MODEL.LOCAL_GPT.getValue(),
            new AiChatConfigData(LOCAL_GPT_API_URL + "/v1/chat/completions", createCommonRequestOption(),
                localGptHeaders, "!openai"));

        config.put(Enums.E_FOUNDATION_MODEL.ERNIE_BOT_TURBO.getValue(), new AiChatConfigData(
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + WENXIN_ACCESS_TOKEN,
            createCommonRequestOption(), ernieBotHeaders, "baidu"));

        return config;
    }

    private static HttpRequestOption createCommonRequestOption() {
        return new HttpRequestOption("POST", "json", "json", 10 * 60 * 1000);
        // 10 minutes
    }

    /**
     * The type Ai chat config data.
     */
    public static class AiChatConfigData {
        /**
         * The Http request url.
         */
        public final String httpRequestUrl;
        /**
         * The Http request option.
         */
        public final HttpRequestOption httpRequestOption;
        /**
         * The Headers.
         */
        public final Map<String, String> headers;
        /**
         * The Manufacturer.
         */
        public final String manufacturer;
        /**
         * The Request body.
         */
        public final Map<String, Object> requestBody;

        /**
         * Instantiates a new Ai chat config data.
         *
         * @param httpRequestUrl    the http request url
         * @param httpRequestOption the http request option
         * @param headers           the headers
         * @param manufacturer      the manufacturer
         */
        public AiChatConfigData(String httpRequestUrl, HttpRequestOption httpRequestOption, Map<String, String> headers,
            String manufacturer) {
            this.httpRequestUrl = httpRequestUrl;
            this.httpRequestOption = httpRequestOption;
            this.headers = headers;
            this.manufacturer = manufacturer;
            this.requestBody = new HashMap<>();
        }
    }

    /**
     * The type Http request option.
     */
    public static class HttpRequestOption {
        /**
         * The Method.
         */
        public final String method;
        /**
         * The Data type.
         */
        public final String dataType;
        /**
         * The Content type.
         */
        public final String contentType;
        /**
         * The Timeout.
         */
        public final int timeout;

        /**
         * Instantiates a new Http request option.
         *
         * @param method      the method
         * @param dataType    the data type
         * @param contentType the content type
         * @param timeout     the timeout
         */
        public HttpRequestOption(String method, String dataType, String contentType, int timeout) {
            this.method = method;
            this.dataType = dataType;
            this.contentType = contentType;
            this.timeout = timeout;
        }
    }
}