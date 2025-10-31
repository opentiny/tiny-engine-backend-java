# HarmonyComponent button 组件生成 schema

HarmonyButton 是一个按钮组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## button 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyButton",
  "props": {
    "text": "按钮文案", // 可以设置按钮的文案。
    "className": "component-base-style",
    "size": "medium", // 可以设置按钮的大小。可选值为 large 、 medium 、 small 、 mini
    "disabled": true, // 可以设置按钮是否为禁用状态。
    "type": "primary", // 可以设置按钮的类型。可选值为 primary 、 success 、 warning 、 danger 、 info 、 text
    "round": true, // 可以设置按钮是否为圆角。
    "plain": true, // 可以设置按钮是否镂空，背景色透明。
    "autofocus": true, // 可以设置按钮是否自动聚焦。
    "loading": true // 可以设置按钮是否为加载中状态。
  },
  "children": [],
  "id": "36322253"
}
```
