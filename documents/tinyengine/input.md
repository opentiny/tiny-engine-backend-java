# TinyVue input 组件生成 schema

TinyInput 是一个输入框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## input 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyInput",
  "props": {
    "placeholder": "请输入",
    "modelValue": "",
    "className": "component-base-style",
    "clearable": true, // 可通过 clearable 属性设置输入框显示清空图标按钮
    "disabled": false, // 可通过 disabled 属性设置输入框是否禁用。
    "type": "text", // 通过对应的 type 属性，可以设置为对应的类型。默认为 text，可选值为 text、 textarea 和 password。
    "size": "medium", // 可通过 size 属性设置尺寸大小，可选值：medium / small / mini。注意： 只在type!="textarea"时有效。
    "modelValue": {
      "type": "JSExpression",
      "value": "this.state.inputData",
      "model": true
    }, // 通过配置对应的 modelValue 属性，modelValue中的value是设置文本的绑定值，取this.state下对应的参数值。
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 输入框的事件，包括 onChange（值被改变时触发）、onInput（输入值改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onBlur（失去焦点时触发）、onFocus（获得焦点时触发）、onClear（点击清空按钮时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "12731335"
}
```