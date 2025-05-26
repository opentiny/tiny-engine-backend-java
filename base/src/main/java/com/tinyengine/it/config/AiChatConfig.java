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
    private static final String OPENAI_API_URL = "https://api.openai.com";
    private static final String LOCAL_GPT_API_URL = "https://dashscope.aliyuncs.com/compatible-mode";
    private static final String DEEPSEEK_V3_URL = "https://api.deepseek.com";


    /**
     * Gets AI chat config.
     *
     * @return the AI chat config
     */
    public static Map<String, AiChatConfigData> getAiChatConfig(String model, String token) {
        Map<String, AiChatConfigData> config = new HashMap<>();

        Map<String, String> openaiHeaders = new HashMap<>();
        // 根据model值判断添加对应的header
        String openAiApiKey = Enums.FoundationModel.GPT_35_TURBO.getValue().equals(model) ? token : null;
        openaiHeaders.put("Authorization", "Bearer " + openAiApiKey);

        Map<String, String> localGptHeaders = new HashMap<>();
        String localGptApiKey = Enums.FoundationModel.LOCAL_GPT.getValue().equals(model) ? token : null;
        localGptHeaders.put("Authorization", "Bearer " + localGptApiKey);

        Map<String, String> deepSeekHeaders = new HashMap<>();
        String deepSeekApiKey = Enums.FoundationModel.DEEPSEEK_V3.getValue().equals(model) ? token : null;
        deepSeekHeaders.put("Authorization", "Bearer " + deepSeekApiKey);

        Map<String, String> ernieBotHeaders = new HashMap<>();


        config.put(Enums.FoundationModel.GPT_35_TURBO.getValue(),
            new AiChatConfigData(
                OPENAI_API_URL + "/v1/chat/completions", createCommonRequestOption(), openaiHeaders,
    "openai"));

        config.put(Enums.FoundationModel.LOCAL_GPT.getValue(),
            new AiChatConfigData(
                LOCAL_GPT_API_URL + "/v1/chat/completions", createCommonRequestOption(), localGptHeaders,
    "!openai"));

        config.put(Enums.FoundationModel.DEEPSEEK_V3.getValue(),
            new AiChatConfigData(
                DEEPSEEK_V3_URL + "/chat/completions", createCommonRequestOption(), deepSeekHeaders,
    "DeepSeek"));

        String ernieBotAccessToken = Enums.FoundationModel.ERNIBOT_TURBO.getValue().equals(model) ? token : null;
        config.put(Enums.FoundationModel.ERNIBOT_TURBO.getValue(), new AiChatConfigData(
"https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token="
                 + ernieBotAccessToken, createCommonRequestOption(), ernieBotHeaders, "baidu"));
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