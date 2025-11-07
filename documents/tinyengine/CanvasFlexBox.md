# CanvasFlexBox 组件生成 schema

CanvasFlexBox 组件是一个弹性布局容器。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## CanvasFlexBox 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "CanvasFlexBox",
  "props": {
    "flexDirection": "row",
    "gap": "8px", // 可通过 gap 属性调整间距
    "padding": "8px", // padding 属性调整内间距
    "className": "component-base-style",
    "flexDirection": "row", // 通过对应的 flexDirection 属性，可以设置为对应的水平对齐方式。默认为 row（水平,起点在左端），可选值为 row（水平,起点在左端）、row-reverse（水平,起点在右端）、 column（垂直,起点在上沿）、column-reverse（垂直,起点在下沿）。
    "justifyContent": "space-around", // 通过对应的 justifyContent 属性，可以设置为对应的水平对齐方式。默认为 flex-start（左对齐），可选值为 flex-start（左对齐）、flex-end（右对齐）、 center（居中）、space-between（两端对齐，子元素间隔相等）、space-around（子元素两侧间隔相等）。
    "alignItems": "stretch" // 通过对应的 alignItems 属性，可以设置为对应的垂直对齐方式。默认为 center（交叉轴的中点对齐），可选值为 center（交叉轴的中点对齐）、flex-start（交叉轴的起点对齐）、flex-end（交叉轴的终点对齐）、baseline（以子元素第一行文字的基线对齐）、stretch（占满容器高度）。
  },
  "children": [],
  "id": "236d76f3"
}
```