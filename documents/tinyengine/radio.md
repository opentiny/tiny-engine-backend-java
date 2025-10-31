# TinyVue radio 组件生成 schema

TinyRadio是一个单选框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## radio 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyRadio",
  "props": {
    "label": "1",
    "text": "单选文本", // 可通过 text 属性设置文本内容， label 属性设置选中值
    "className": "component-base-style",
    "disabled": true, // 可通过 disabled 属性设置单选框是否禁用。
    "border": true, // 可通过 border 属性设置是否显示边框。
    "size": "medium", // 可通过 size 属性设置尺寸大小，可选值：medium / small / mini。
    "name": "radio", // 可通过 name 属性设置 原生name属性
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 单选框的事件，包括 onChange（值被改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onFocus（获得焦点时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "26723552"
}
```
