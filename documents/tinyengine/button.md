# TinyVue button 组件生成 schema

TinyButton是一个按钮组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## button 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyButton",
  "props": {
    "text": "按钮文案",
    "className": "component-base-style",
    "type": "primary", // 通过 type 设置按钮类型，可以设置为对应的类型。可选值为 primary、success、info、warning、danger和 text。
    "disabled": false, // 可通过 disabled 属性设置按钮是否禁用。
    "size": "large", // 可通过 size 属性设置尺寸大小，可选值：large / medium / small / mini。
    "round": true, // 可通过 round 属性设置按钮是否圆角。
    "plain": true, // 可通过 plain 属性设置按钮是否为朴素按钮。
    "reset-time": 2, // 可通过 reset-time 属性设置按钮禁用时间。可防止重复提交，单位毫秒
    "circle": false, // 可通过 circle 属性设置是否为圆形按钮。
    "autofocus": true, // 可通过 autofocus 属性设置是否自动聚焦。
    "loading": true, // 可通过 loading 属性设置是否加载中样式。
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 按钮的事件，包括 onChange（值被改变时触发）、onInput（输入值改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onBlur（失去焦点时触发）、onFocus（获得焦点时触发）、onClear（点击清空按钮时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "84331435"
}
```