# HarmonyComponent slider 组件生成 schema

HarmonySlider 是一个滑块选择器组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## slider 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonySlider",
  "props": {
    "value": 6, // 可以设置滑块选择器的初始值。
    "show-value": true, // 可以设置是否显示数值。
    "className": "component-base-style",
    "min": 0, // 可以设置滑块选择器的最小值。
    "max": 100, // 可以设置滑块选择器最大值。
    "step": 1, // 可以设置滑块选择器的步长。
    "disabled": true // 可以设置是否禁用滑块选择器。
  },
  "children": [],
  "id": "25164222"
}
```
