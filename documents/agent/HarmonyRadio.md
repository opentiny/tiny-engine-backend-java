# HarmonyComponent radio 组件生成 schema

HarmonyRadio 是一个单项选择器组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## radio 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyRadio",
  "props": {
    "checked": true, // 可以设置是否绑定默认选中。
    "value": "1",
    "text": "单项选择器文案", // 可以设置单项选择器的文案。
    "className": "component-base-style",
    "disabled": true // 可以设置是否禁用。
  },
  "children": [],
  "id": "51334543"
}
```
