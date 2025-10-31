# TinyVue search 组件生成 schema

TinySearch是一个搜索框组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## search 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinySearch",
  "props": {
    "modelValue": "",
    "placeholder": "输入关键词",
    "className": "component-base-style",
    "disabled": true, // 可通过 disabled 属性设置输入框是否禁用。
    "clearable": true, // 可通过 clearable 属性设置输入框显示清空图标按钮
    "isEnterSearch": true, // 可通过 isEnterSearch 属性设置是否在按下键盘Enter键的时候触发search事件
    "mini": true, // 可通过 mini 属性设置迷你模式，配置为true时，搜索默认显示为一个带图标的圆形按钮，点击后展开
    "ref": "search", // 通过对应的 ref 属性，可以设置需要的ref引用类名
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 搜索框的事件，包括 onChange（值被改变时触发）、onSearch（点击搜索按钮时触发）、onClick（点击事件）、onFocus（获得焦点时触发）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "56121254"
}
```
