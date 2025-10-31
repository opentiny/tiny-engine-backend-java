# HarmonyComponent textarea 组件生成 schema

HarmonyTextarea 是一个多行输入框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## textarea 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyTextarea",
  "props": {
    "placeholder": "请输入", // 可以设置占位符。
    "value": "value", // 可以设置多行输入框的初始内容。
    "className": "component-base-style",
    "disabled": true, // 可以设置是否禁用。
    "clearable": true, // 可以设置是否显示清除按钮。
    "rows": 2, // 可以设置多行输入框的行数。
    "maxlength": 1000 // 可以设置多行输入框的最大输入长度。
  },
  "children": [],
  "id": "152352c5"
}
```
