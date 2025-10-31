# HarmonyComponent checkbox 组件生成 schema

HarmonyCheckbox 是一个多选框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## checkbox 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyCheckbox",
  "props": {
    "checked": true, // 可以设置多选框的选中状态。
    "text": "多选框文案", // 可以设置多选框的文案。
    "className": "component-base-style",
    "disabled": true // 可以设置多选框是否为禁用状态。
  },
  "children": [],
  "id": "58523b45"
}
```
