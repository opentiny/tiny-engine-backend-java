# TinyVue carousel 组件生成 schema

TinyCarousel是一个走马灯组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。
componentName 为 TinyCarouselItem 的组件， 通过对应的 title 属性，可以设置标题。 通过对应的 name 属性，可以设置唯一标识， 通过对应的 indicator-position 属性，可以设置指示器位置。可选值为 outside 和 none。

## carousel 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "TinyCarousel",
  "props": {
    "height": "180px", // 设置走马灯的高度。
    "className": "component-base-style",
    "arrow": "always", // 可以设置切换箭头的显示效果。默认值为 hover， 可选值为 always 、 hover 和 never。
    "autoplay": true, // 可以设置是否自动切换。
    "indicator-position": "outside", // 可以设置指示器位置。可选值为 outside 和 none。
    "initial-index": 0, // 可以设置初始状态激活的幻灯片的索引。
    "interval": 5000, // 可以设置自动切换的时间间隔，单位为毫秒。
    "loop": true, // 可以设置是否循环显示。
    "show-title": true, // 可以设置是否显示标题。
    "trigger": "hover", // 可以设置指示器的触发方式。默认值为 hover，可选值为 hover 和 click。
    "type": "card", // 可以设置走马灯的类型。默认值为 horizontal，可选值为 horizontal 、 vertical 和 card。
    "ref": "carousel", // 可以设置需要的ref引用类名。
    // 走马灯的事件，包括 onChange（值被改变时触发）、onFocus（获得焦点时触发）、onClick（点击事件）、onMousemove（鼠标移动时触发），添加对应的事件类型为当前属性，value值绑定事件名，取对应的参数值
    "onClick": {
      "type": "JSExpression",
      "value": "this.onClickNew"
    }
  },
  "children": [
    {
      "componentName": "TinyCarouselItem",
      "props": {
        "title": "carousel-item-a"
      },
      "children": [
        {
          "componentName": "div",
          "props": {
            "style": "margin:10px 0 0 30px"
          },
          "id": "614d4146"
        }
      ],
      "id": "2366c336"
    },
    {
      "componentName": "TinyCarouselItem",
      "props": {
        "title": "carousel-item-b"
      },
      "children": [
        {
          "componentName": "div",
          "props": {
            "style": "margin:10px 0 0 30px"
          },
          "id": "1732256d"
        }
      ],
      "id": "442457ef"
    }
  ],
  "id": "231c8a63"
}
```
