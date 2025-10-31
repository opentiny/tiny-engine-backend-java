# HarmonyComponent form 组件生成 schema

HarmonyForm 是一个表单组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## form 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyForm",
  "props": {
    "className": "component-base-style",
    "report-submit": true, // 可以设置是否返回 formId 用于发送模板消息
    "report-submit-timeout": 100 // 可以设置返回 formId 的超时时间，单位为 ms
  },
  "children": [
    {
      "componentName": "HarmonyView",
      "props": {},
      "children": [
        {
          "componentName": "HarmonyView",
          "props": {
            "label": "用户名"
          },
          "children": [
            {
              "componentName": "HarmonyText",
              "props": {
                "text": "姓名"
              },
              "id": "45655522"
            }
          ],
          "id": "5224f433"
        },
        {
          "componentName": "HarmonyView",
          "props": {
            "label": "用户名"
          },
          "children": [
            {
              "componentName": "HarmonyInput",
              "props": {
                "placeholder": "请输入用户名"
              },
              "id": "5533e24d"
            }
          ],
          "id": "6c946365"
        }
      ],
      "id": "d64452a5"
    }
  ],
  "id": "53654522"
}
```
