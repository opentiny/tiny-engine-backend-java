# TinyVue breadcrumb 组件生成 schema

TinyBreadcrumb是一个面包屑组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## breadcrumb 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyBreadcrumb",
  "props": {
    "options": [
      {
        "to": "{ path: '/' }",
        "label": "首页"
      },
      {
        "to": "{ path: '/breadcrumb' }",
        "label": "产品"
      },
      {
        "replace": "true",
        "label": "软件"
      }
    ],
    "className": "component-base-style",
    "separator": ".", // 可通过 separator 属性设置面包屑中间的分隔符。
    "textField": "label", // 可通过 textField 属性设置面包屑的显示键值，结合options使用。
    "onSelect": {
      "type": "JSExpression",
      "value": "this.onSelectNew"
    } // 面包屑的事件，包括 onSelect（选择 breadcrumb 时触发）、onClick（点击事件）、onChange（值被改变时触发）、onFocus（获得焦点时触发）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
  },
  "children": [],
  "id": "62815232"
}
```