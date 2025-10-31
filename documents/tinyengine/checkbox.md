# TinyVue checkout 组件生成 schema

TinyCheckbox是一个多选框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## checkout 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyCheckbox",
  "props": {
    "text": "复选框文案",
    "className": "component-base-style",
    "disabled": false, // 可通过 disabled 属性设置复选框是否禁用。
    "checked": true, // 可通过 checked 属性设置复选框是否勾选。
    "text": "复选框文案", // 可通过 text 属性设置复选框的描述文本。
    "border": true, // 可通过 border 属性设置复选框是否有边框。
    "true-label": "真文本", // 通过 true-label 属性设置选中的值。
    "false-label": "假文本", // 通过 false-label 属性设置选中的值。
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 复选框的事件，包括 onChange（值被改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onFocus（获得焦点时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "e4636442"
}
```
