# HarmonyComponent image 组件生成 schema

HarmonyImage 是一个图片组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## image 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyImage",
  "props": {
    "src": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/designer-default-icon.jpg", // 可以设置图片的源。
    "className": "component-base-style",
    "draggable": true, // 可以设置图片是否可拖拽。
    "mode": "scaleToFill" // 可以设置图片的缩放模式。可选值为 scaleToFill 、 aspectFit 、 aspectFill 、 widthFix 、 heightFix
  },
  "children": [],
  "id": "62256151"
}
```
