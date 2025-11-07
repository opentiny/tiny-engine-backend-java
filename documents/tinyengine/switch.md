# TinyVue switch 组件生成 schema

TinySwitch是一个开关组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## switch 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinySwitch",
  "props": {
    "modelValue": "",
    "className": "component-base-style",
    "disabled": false, // 可通过 disabled 属性设置开关是否禁用。
    "true-value": "yes", // 通过 true-value 属性设置打开值。
    "false-value": "no", // 通过 false-value 属性设置关闭值。
    "mini": true, // 可通过 mini 属性设置小尺寸开关。
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 开关的事件，包括 onChange（值被改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onFocus（获得焦点时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "e2642249"
}
```
