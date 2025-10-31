# TinyVue tabs 组件生成 schema

TinyTabs是一个页签组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## tabs 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyTabs",
  "props": {
    "id": "tabs1",
    "modelValue": "first", // 该字段必须有，可通过 modelValue 属性设置默认展示的标签页项
    "className": "component-base-style",
    "showEditIcon": true, // 可通过 showEditIcon 属性设置是否显示编辑图标。
    "tab-style": "card", // 通过对应的 tab-style 属性，可以设置为对应的标签页样式。默认为 空 ，可选值为 card、border-card 和清空所选值。
    "ref": "tabs", // 通过对应的 ref 属性，可以设置需要的ref引用类名。
    "onClick": {
      "type": "JSExpression",
      "value": "this.onClickNew"
    } // 标签页的事件，包括 onClick（点击事件）、onEdit（点击新增按钮或关闭按钮或者编辑按钮后触发）、onClose（关闭页签时触发）、onChange（值被改变时触发）、onFocus（获得焦点时触发）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  // 通过对应的 children 属性，在 children 下新增一个子组件 TinyTabItem。componentName 为 TinyTabItem 的组件， 通过对应的 title 属性，可以设置标题。 通过对应的 name 属性，可以设置唯一标识
  "children": [
    {
      "componentName": "TinyTabItem",
      "props": {
        "title": "标签页1",
        "name": "first" // 该字段必须有，可通过 name 属性与父级 modelValue 属性对应，展示对应的标签页项
      },
      "children": [
        {
          "componentName": "div",
          "props": {
            "style": "margin:10px 0 0 30px"
          },
          "id": "216721a4"
        }
      ],
      "id": "15556232"
    },
    {
      "componentName": "TinyTabItem",
      "props": {
        "title": "标签页2",
        "name": "second"
      },
      "children": [
        {
          "componentName": "div",
          "props": {
            "style": "margin:10px 0 0 30px"
          },
          "id": "69a8d345"
        }
      ],
      "id": "fe4d3436"
    }
  ],
  "id": "45153634"
}
```
