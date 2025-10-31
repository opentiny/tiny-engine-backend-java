# HarmonyComponent switch 组件生成 schema

HarmonySwitch 是一个开关组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## switch 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonySwitch",
  "props": {
    "checked": true, // 可以设置是否绑定默认打开。
    "className": "component-base-style",
    "disabled": true, // 可以设置是否禁用。
    "color": "red" // 可以设置开关颜色。
  },
  "children": [],
  "id": "63346431"
}
```
