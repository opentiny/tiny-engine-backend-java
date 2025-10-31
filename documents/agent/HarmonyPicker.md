# HarmonyComponent picker 组件生成 schema

HarmonyPicker 是一个选择器组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## picker 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyPicker",
  "props": {
    "value": "1", // 可以设置选择器的初始值。
    "mode": "selector", //  可以设置选择器的模式。默认值为 selector，可选值为 selector 、 date 和 time
    "range": [
      {
        "value": "1",
        "label": "黄金糕"
      },
      {
        "value": "2",
        "label": "双皮奶"
      }
    ], // 可以设置选择器的数据源。模式为 selector 时，range 为数组，数组的元素为对象，对象包含 value 和 label 属性。
    "className": "component-base-style",
    "disabled": true // 可以设置是否禁用选择器。
  },
  "children": [],
  "id": "54e34551"
}
```
