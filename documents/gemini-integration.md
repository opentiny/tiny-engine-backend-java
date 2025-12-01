# Gemini API 集成说明

## 概述

本项目现已支持 Google Gemini API 调用，可以与 DeepSeek、OpenAI 等其他 AI 模型并行使用。

## 支持的 Gemini 模型

- `gemini-pro` - Gemini Pro 基础模型
- `gemini-1.5-pro` - Gemini 1.5 Pro 模型
- `gemini-1.5-flash` - Gemini 1.5 Flash 快速模型

**注意**：模型名称支持两种格式：
- 简单格式：`gemini-1.5-pro`（推荐）
- 完整格式：`models/gemini-1.5-pro`（系统会自动规范化）

系统会自动处理模型名称中的 `models/` 前缀，无需担心格式问题。

## API 配置

### 1. 获取 Gemini API Key

1. 访问 [Google AI Studio](https://makersuite.google.com/app/apikey)
2. 登录您的 Google 账号
3. 创建新的 API Key
4. 复制生成的 API Key

### 2. 配置方式

#### 方式一：通过请求参数配置（推荐）

在调用 AI Chat API 时，通过请求体传入配置：

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "YOUR_GEMINI_API_KEY",
  "baseUrl": "https://generativelanguage.googleapis.com",
  "messages": [
    {
      "role": "user",
      "content": "Hello, how are you?"
    }
  ],
  "temperature": 0.7,
  "stream": false
}
```

#### 方式二：修改配置文件

修改 `OpenAIConfig.java` 的默认配置：

```java
@Data
@Configuration
public class OpenAIConfig {
    private String apiKey = "YOUR_GEMINI_API_KEY";
    private String baseUrl = "https://generativelanguage.googleapis.com";
    private String defaultModel = "gemini-1.5-pro";
    private int timeoutSeconds = 300;
}
```

## 使用示例

### 基础对话

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-pro",
    "apiKey": "YOUR_GEMINI_API_KEY",
    "messages": [
      {
        "role": "user",
        "content": "介绍一下 Google Gemini"
      }
    ],
    "temperature": 0.7
  }'
```

### 流式响应

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-flash",
    "apiKey": "YOUR_GEMINI_API_KEY",
    "messages": [
      {
        "role": "user",
        "content": "写一首关于春天的诗"
      }
    ],
    "stream": true,
    "temperature": 0.9
  }'
```

### 多轮对话

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "YOUR_GEMINI_API_KEY",
  "messages": [
    {
      "role": "user",
      "content": "什么是人工智能？"
    },
    {
      "role": "assistant",
      "content": "人工智能（Artificial Intelligence, AI）是计算机科学的一个分支..."
    },
    {
      "role": "user",
      "content": "它有哪些应用场景？"
    }
  ]
}
```

### 图片理解（仅 gemini-1.5-pro 和 gemini-1.5-flash）

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "YOUR_GEMINI_API_KEY",
  "messages": [
    {
      "role": "user",
      "content": [
        {
          "type": "text",
          "text": "这张图片里有什么？"
        },
        {
          "type": "image_url",
          "image_url": {
            "url": "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
          }
        }
      ]
    }
  ]
}
```

## 参数说明

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| model | string | 是 | 模型名称，可选值：gemini-pro, gemini-1.5-pro, gemini-1.5-flash |
| apiKey | string | 是* | Gemini API Key（如已在配置文件中设置则可省略） |
| baseUrl | string | 否 | API 基础 URL，默认为 https://generativelanguage.googleapis.com |
| messages | array | 是 | 对话消息数组 |
| temperature | float | 否 | 温度参数，范围 0-1，默认 0.7 |
| maxTokens | integer | 否 | 最大生成 token 数 |
| stream | boolean | 否 | 是否启用流式响应，默认 false |
| stop | string/array | 否 | 停止序列 |

## API 格式转换

项目内部会自动将 OpenAI 格式的请求转换为 Gemini API 格式：

- **角色映射**：
  - `user` → `user`
  - `assistant` → `model`
  - `system` → `user` (系统消息会作为用户消息处理)

- **响应格式**：Gemini 的响应会被转换为 OpenAI 兼容格式，确保前端调用一致性

## 注意事项

1. **API Key 安全**：请勿在代码中硬编码 API Key，建议通过环境变量或配置文件管理
2. **配额限制**：Gemini API 有调用配额限制，请查看 [Google AI Studio](https://ai.google.dev/pricing) 了解详情
3. **模型选择**：
   - `gemini-pro`：适合通用对话任务
   - `gemini-1.5-pro`：支持更长上下文，适合复杂任务
   - `gemini-1.5-flash`：响应更快，适合需要快速反馈的场景
4. **网络访问**：确保服务器可以访问 `generativelanguage.googleapis.com`

## 错误处理

常见错误及解决方案：

| 错误 | 原因 | 解决方案 |
|------|------|----------|
| 401 Unauthorized | API Key 无效或未提供 | 检查 API Key 是否正确 |
| 403 Forbidden | API 未启用或配额不足 | 在 Google Cloud Console 中启用 API |
| 404 Model Not Exist | 模型名称格式错误 | 使用正确的模型名称（如 `gemini-1.5-pro`），系统已自动处理 `models/` 前缀 |
| 429 Too Many Requests | 超出调用限制 | 降低请求频率或升级配额 |
| 500 Internal Server Error | API 格式错误 | 检查请求格式是否符合要求 |

### 常见问题排查

#### 1. Model Not Exist 错误
**原因**：早期版本可能存在模型名称格式处理问题。

**解决方案**：
- 确保使用简单格式的模型名称：`gemini-1.5-pro`、`gemini-1.5-flash`
- 避免使用 `models/gemini-1.5-pro` 格式（虽然新版本已支持）
- 检查模型名称拼写是否正确

#### 2. Authentication Fails 错误
**原因**：API Key 无效或格式错误。

**解决方案**：
- 在 [Google AI Studio](https://makersuite.google.com/app/apikey) 重新生成 API Key
- 确保 API Key 完整复制，没有多余空格
- 验证 Generative Language API 已在项目中启用

## 技术实现

### 核心文件

1. **GeminiApiAdapter.java**：负责 OpenAI 格式与 Gemini 格式的相互转换
2. **AiChatV1ServiceImpl.java**：主要服务实现，支持多种 AI 模型
3. **AiChatConfig.java**：AI 模型配置管理
4. **Enums.java**：添加了 Gemini 模型枚举

### 转换逻辑

项目使用适配器模式实现格式转换：

```java
// 请求转换
Map<String, Object> geminiRequest = GeminiApiAdapter.convertRequestToGemini(openAiRequest);

// 响应转换
Map<String, Object> openAiResponse = GeminiApiAdapter.convertResponseFromGemini(geminiResponse, model);
```

## 扩展开发

如需添加新的 AI 模型支持，可参考 Gemini 的实现方式：

1. 在 `Enums.FoundationModel` 中添加新模型枚举
2. 在 `AiChatConfig` 中添加模型配置
3. 创建对应的 Adapter 进行格式转换（如需要）
4. 在 `AiChatV1ServiceImpl` 中添加模型识别和处理逻辑

## 参考链接

- [Google Gemini API 文档](https://ai.google.dev/docs)
- [Google AI Studio](https://makersuite.google.com/)
- [Gemini API 定价](https://ai.google.dev/pricing)

