# HarmonyComponent editor 组件生成 schema

HarmonyEditor 是一个编辑器组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## editor 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyEditor",
  "props": {
    "modelValue": "value", // 可以设置编辑器的绑定值。
    "className": "component-base-style",
    "read-only": true // 可以设置是否为只读模式。
  },
  "children": [],
  "id": "3255e443"
}
```
