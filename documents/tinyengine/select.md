# TinyVue select 组件生成 schema

TinySelect是一个选择器组件
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## select 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinySelect",
  "props": {
    "modelValue": "",
    "placeholder": "请选择",
    "options": [
      {
        "value": "1",
        "label": "黄金糕"
      },
      {
        "value": "2",
        "label": "双皮奶"
      }
    ],
    "className": "component-base-style",
    "clearable": true, // 可通过 clearable 属性设置下拉框显示清空图标按钮
    "searchable": true, // 可通过 searchable 属性设置下拉框是否下拉可搜索
    "disabled": false, // 可通过 disabled 属性设置下拉框是否禁用
    "multiple": false, // 可通过 multiple 属性设置下拉框是否禁用
    "multiple-limit": 2, // 可通过 multiple-limit 属性设置下拉框最大可选值，多选时用户最多可以选择的项目数，为 0 则不限制
    "collapse-tags": true, // 可通过 collapse-tags 属性设置下拉框多选时是否将选中值按文字的形式展示
    "popper-class": "select", // 可通过 popper-class 属性设置下拉框类名
    "onUpdate:modelValue": {
      "type": "JSExpression",
      "value": "this.onUpdate_modelValueNew"
    } // 下拉框的事件，包括 onChange（值被改变时触发）、onUpdate:modelValue（双向绑定的值改变时触发）、onBlur（失去焦点时触发）、onFocus（获得焦点时触发）、onClear（点击清空按钮时触发）、onRemoveTag（多选模式下移除tag时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "2446244a"
}
```
