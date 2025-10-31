# HarmonyComponent input 组件生成 schema

HarmonyInput 是一个输入框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## input 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyInput",
  "props": {
    "placeholder": "请输入", // 可以设置输入框的提示文字。
    "modelValue": "value", // 可以设置输入框的绑定值。
    "className": "component-base-style",
    "type": "text", // 可以设置输入框的类型。
    "clearable": true, // 可以设置是否显示清除按钮。
    "disabled": true, // 可以设置是否禁用输入框。
    "maxlength": 10 // 可以设置输入框的最大长度。
  },
  "children": [],
  "id": "31365645"
}
```
