# Gemini API 快速开始指南

## 🚀 5分钟快速上手

### 第一步：获取 API Key

1. 访问 [Google AI Studio](https://makersuite.google.com/app/apikey)
2. 登录 Google 账号
3. 点击 "Create API Key" 创建密钥
4. 复制生成的 API Key（格式：AIza...）

### 第二步：启动项目

```bash
# 编译项目
cd E:\tiny-engine-backend-java
mvn clean install -DskipTests

# 启动应用
cd app
mvn spring-boot:run
```

### 第三步：测试 API

#### 方法 1：使用 curl（推荐）

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-pro",
    "apiKey": "你的API_KEY",
    "messages": [
      {
        "role": "user",
        "content": "你好，请介绍一下你自己"
      }
    ],
    "temperature": 0.7
  }'
```

#### 方法 2：使用 Postman

1. 创建 POST 请求：`http://localhost:8080/app-center/api/ai/chat`
2. Headers：`Content-Type: application/json`
3. Body（raw JSON）：
```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "你的API_KEY",
  "messages": [
    {"role": "user", "content": "你好"}
  ]
}
```

#### 方法 3：使用 JavaScript

```javascript
async function chatWithGemini() {
  const response = await fetch('http://localhost:8080/app-center/api/ai/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      model: 'gemini-1.5-pro',
      apiKey: '你的API_KEY',
      messages: [
        {role: 'user', content: '你好'}
      ],
      temperature: 0.7
    })
  });
  
  const data = await response.json();
  console.log(data.choices[0].message.content);
}

chatWithGemini();
```

---

## 📝 常用示例

### 示例 1：简单对话

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-pro",
    "apiKey": "你的API_KEY",
    "messages": [{"role": "user", "content": "什么是人工智能？"}]
  }'
```

### 示例 2：代码生成

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-pro",
    "apiKey": "你的API_KEY",
    "messages": [
      {"role": "user", "content": "用Java写一个冒泡排序算法"}
    ],
    "temperature": 0.3
  }'
```

### 示例 3：流式响应（打字机效果）

```bash
curl -X POST http://localhost:8080/app-center/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemini-1.5-flash",
    "apiKey": "你的API_KEY",
    "messages": [
      {"role": "user", "content": "写一首关于春天的诗"}
    ],
    "stream": true
  }' \
  --no-buffer
```

### 示例 4：多轮对话

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "你的API_KEY",
  "messages": [
    {"role": "user", "content": "什么是机器学习？"},
    {"role": "assistant", "content": "机器学习是人工智能的一个分支..."},
    {"role": "user", "content": "它有哪些应用？"}
  ]
}
```

### 示例 5：系统提示词

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "你的API_KEY",
  "messages": [
    {"role": "system", "content": "你是一个专业的Java开发导师"},
    {"role": "user", "content": "什么是Spring Boot？"}
  ]
}
```

---

## 🎯 模型选择指南

| 模型 | 速度 | 质量 | 适用场景 |
|------|------|------|----------|
| **gemini-pro** | ⭐⭐⭐ | ⭐⭐⭐ | 通用对话、基础任务 |
| **gemini-1.5-pro** | ⭐⭐ | ⭐⭐⭐⭐⭐ | 复杂任务、长文本、图片理解 |
| **gemini-1.5-flash** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 快速响应、实时对话 |

### 推荐使用场景

- **客服机器人**：gemini-1.5-flash（快速响应）
- **代码生成**：gemini-1.5-pro（高质量输出）
- **文档分析**：gemini-1.5-pro（支持长文本）
- **图片识别**：gemini-1.5-pro 或 gemini-1.5-flash

---

## ⚙️ 常用参数说明

### 基础参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| model | string | 是 | - | 模型名称 |
| apiKey | string | 是 | - | API 密钥 |
| messages | array | 是 | - | 对话消息列表 |
| temperature | float | 否 | 0.7 | 创造性（0-1） |
| stream | boolean | 否 | false | 是否流式输出 |
| maxTokens | integer | 否 | - | 最大生成长度 |

### Temperature 参数建议

- **0.0 - 0.3**：适合代码生成、数据分析（更确定性）
- **0.4 - 0.7**：适合通用对话、问答（平衡）
- **0.8 - 1.0**：适合创意写作、头脑风暴（更随机）

---

## 🔍 故障排查

### 问题 1：401 Unauthorized

**原因**：API Key 无效或未提供

**解决**：
```bash
# 检查 API Key 是否正确
# 确保 apiKey 字段已填写
{
  "apiKey": "AIza..."  // 替换为你的真实 API Key
}
```

### 问题 2：403 Forbidden

**原因**：API 未启用或配额不足

**解决**：
1. 访问 [Google Cloud Console](https://console.cloud.google.com/)
2. 启用 Generative Language API
3. 检查配额使用情况

### 问题 3：网络连接失败

**原因**：无法访问 Google API

**解决**：
- 检查网络连接
- 如在国内，可能需要配置代理
- 确认防火墙设置

### 问题 4：响应速度慢

**解决**：
- 使用 gemini-1.5-flash 模型
- 减少 maxTokens 参数
- 优化网络连接

---

## 💡 最佳实践

### 1. API Key 管理

❌ **不要这样**：
```java
String apiKey = "AIzaSyD..."; // 硬编码在代码中
```

✅ **推荐做法**：
```java
// 使用环境变量
String apiKey = System.getenv("GEMINI_API_KEY");

// 或使用配置文件
@Value("${gemini.api.key}")
private String apiKey;
```

### 2. 错误处理

```java
try {
    Object response = aiChatV1Service.chatCompletion(request);
    return ResponseEntity.ok(response);
} catch (Exception e) {
    logger.error("Gemini API 调用失败", e);
    return ResponseEntity.status(500)
        .body("AI 服务暂时不可用，请稍后重试");
}
```

### 3. 频率限制

```java
// 实现简单的频率限制
@RateLimiter(name = "gemini", fallbackMethod = "rateLimitFallback")
public Object chatWithGemini(ChatRequest request) {
    return aiChatV1Service.chatCompletion(request);
}
```

### 4. 超时控制

```json
{
  "model": "gemini-1.5-pro",
  "apiKey": "你的API_KEY",
  "messages": [...],
  "timeout": 30  // 30秒超时
}
```

---

## 📊 性能优化建议

### 1. 选择合适的模型
- 快速响应 → gemini-1.5-flash
- 高质量输出 → gemini-1.5-pro

### 2. 控制输入长度
```json
{
  "maxTokens": 500,  // 限制输出长度
  "temperature": 0.7
}
```

### 3. 使用流式响应
```json
{
  "stream": true  // 提升用户体验
}
```

### 4. 缓存常见问题
```java
// 对常见问题进行缓存
@Cacheable(value = "geminiResponses", key = "#question")
public String getAnswer(String question) {
    // ...
}
```

---

## 🎓 学习资源

### 官方资源
- [Google Gemini 文档](https://ai.google.dev/docs)
- [API 参考](https://ai.google.dev/api)
- [定价说明](https://ai.google.dev/pricing)

### 项目文档
- 详细集成说明：`documents/gemini-integration.md`
- 代码示例：`documents/gemini-examples.http`
- 技术总结：`Gemini集成完成总结.md`

---

## 🆘 获取帮助

### 常见问题
1. 查看项目日志：`logs/tiny-engine-backend-java/error.log`
2. 参考错误处理文档：`documents/gemini-integration.md`
3. 检查 API Key 和网络连接

### 技术支持
- 查看项目 README
- 参考示例代码
- 查看单元测试代码

---

## ✅ 快速检查清单

在开始使用前，请确认：

- [ ] 已获取 Gemini API Key
- [ ] 项目已成功编译（mvn clean install）
- [ ] 应用已启动（端口 8080）
- [ ] 网络可以访问 generativelanguage.googleapis.com
- [ ] 已阅读基础示例

---

## 🎉 开始使用

一切准备就绪！现在你可以：

1. 尝试基础对话示例
2. 测试不同的模型
3. 体验流式响应
4. 探索高级功能

**祝你使用愉快！** 🚀

---

*更新时间：2025-11-26*  
*版本：v1.0.0*

