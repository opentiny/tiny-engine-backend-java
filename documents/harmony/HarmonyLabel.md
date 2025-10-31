# HarmonyComponent label 组件生成 schema

HarmonyLabel 是一个标签组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## label 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyLabel",
  "props": {
    "className": "component-base-style",
    "for": "name" // 可以设置 label 标签的 for 属性，用于绑定对应的表单控件。
  },
  "children": [],
  "id": "5125653c"
}
```
