# HarmonyComponent scrollView 组件生成 schema

HarmonyScrollView 是一个可滚动视图组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## scrollView 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyScrollView",
  "props": {
    "className": "component-base-style",
    "scroll-x": true, // 可以设置横向滚动。
    "scroll-y": true // 可以设置纵向滚动。
  },
  "children": [],
  "id": "55f32255"
}
```
