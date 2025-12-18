# Gemini API "Model Not Exist" 错误修复总结

## 问题描述

在使用 Gemini API 时，出现以下错误：

```json
{
  "message": "Model Not Exist",
  "type": "invalid_request_error",
  "param": null,
  "code": "invalid_request_error"
}
```

**报错接口**：`/app-center/api/chat/completions`

## 根本原因

问题出在 `AiChatV1ServiceImpl.java` 中的 `normalizeApiUrl` 方法。当构建 Gemini API URL 时，代码直接使用了传入的 `model` 参数：

```java
// 原始代码（有问题）
baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
```

如果用户传入的模型名称是 `models/gemini-1.5-pro` 格式（Google 官方文档中的完整格式），最终构建的 URL 会变成：

```
https://generativelanguage.googleapis.com/v1beta/models/models/gemini-1.5-pro:generateContent
```

这个 URL 中包含了重复的 `models/` 路径，导致 Gemini API 返回 "Model Not Exist" 错误。

## 解决方案

### 1. 添加模型名称规范化方法

在 `AiChatV1ServiceImpl.java` 中添加了 `normalizeGeminiModelName` 方法，用于移除模型名称中可能存在的 `models/` 前缀：

```java
/**
 * 规范化 Gemini 模型名称，移除 "models/" 前缀
 */
private String normalizeGeminiModelName(String model) {
    if (model == null) {
        return "gemini-1.5-pro";
    }
    // Remove "models/" prefix if exists
    if (model.startsWith("models/")) {
        return model.substring(7); // Remove "models/"
    }
    return model;
}
```

### 2. 更新 URL 构建逻辑

修改 `normalizeApiUrl` 方法，在构建 Gemini URL 时使用规范化后的模型名称：

```java
private String normalizeApiUrl(String baseUrl, String model, boolean isGemini, String apiKey) {
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
        if (isGemini) {
            // Normalize model name: remove "models/" prefix if exists
            String normalizedModel = normalizeGeminiModelName(model);
            // Gemini default URL structure
            baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + normalizedModel + ":generateContent";
        } else {
            baseUrl = config.getBaseUrl();
        }
    }
    // ... 其他代码
}
```

## 修改的文件

1. **E:\tiny-engine-backend-java\base\src\main\java\com\tinyengine\it\service\app\impl\v1\AiChatV1ServiceImpl.java**
   - 添加 `normalizeGeminiModelName` 方法
   - 更新 `normalizeApiUrl` 方法以使用规范化的模型名称

2. **E:\tiny-engine-backend-java\documents\gemini-integration.md**
   - 添加了模型名称格式的说明
   - 更新了错误处理部分，添加 "Model Not Exist" 错误的排查方法

3. **E:\tiny-engine-backend-java\documents\gemini-test-examples.http**（新建）
   - 创建了完整的测试用例集合，包括不同模型名称格式的测试

## 支持的模型名称格式

修复后，系统支持以下两种模型名称格式：

1. **简单格式（推荐）**：
   - `gemini-pro`
   - `gemini-1.5-pro`
   - `gemini-1.5-flash`

2. **完整格式（自动规范化）**：
   - `models/gemini-pro`
   - `models/gemini-1.5-pro`
   - `models/gemini-1.5-flash`

系统会自动处理两种格式，确保构建正确的 API URL。

## 验证步骤

### 1. 重新编译项目

```bash
cd E:\tiny-engine-backend-java
mvn clean compile -DskipTests
```

### 2. 重启应用

```bash
mvn spring-boot:run
```

### 3. 测试 API

使用 `documents/gemini-test-examples.http` 中的测试用例进行验证：

```bash
# 测试简单格式
POST http://localhost:8080/app-center/api/chat/completions
Content-Type: application/json

{
  "model": "gemini-1.5-pro",
  "apiKey": "YOUR_GEMINI_API_KEY",
  "messages": [
    {
      "role": "user",
      "content": "你好"
    }
  ]
}

# 测试完整格式（修复后应该正常工作）
POST http://localhost:8080/app-center/api/chat/completions
Content-Type: application/json

{
  "model": "models/gemini-1.5-pro",
  "apiKey": "YOUR_GEMINI_API_KEY",
  "messages": [
    {
      "role": "user",
      "content": "你好"
    }
  ]
}
```

## 预期结果

修复后，无论使用哪种模型名称格式，都应该能够正常调用 Gemini API 并获得响应：

```json
{
  "id": "gemini-1732859123456",
  "object": "chat.completion",
  "created": 1732859123,
  "model": "gemini-1.5-pro",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "你好！很高兴与你交流..."
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 5,
    "completion_tokens": 20,
    "total_tokens": 25
  }
}
```

## 其他注意事项

1. **API Key 格式**：确保使用正确的 Gemini API Key（通常以 `AIzaSy` 开头）
2. **API 启用**：在 Google Cloud Console 中确保 Generative Language API 已启用
3. **网络访问**：确保服务器可以访问 `generativelanguage.googleapis.com`
4. **配额限制**：注意 Gemini API 的调用配额限制

## 相关文档

- [Gemini API 集成说明](./gemini-integration.md)
- [Gemini 测试示例](./gemini-test-examples.http)
- [快速开始指南](../QUICKSTART_GEMINI.md)

## 更新日期

2025-11-29

