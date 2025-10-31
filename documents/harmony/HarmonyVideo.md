# HarmonyComponent video 组件生成 schema

HarmonyVideo 是一个视频组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## video 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyVideo",
  "props": {
    "src": "https://tinyengine-assets.obs.myhuaweicloud.com/files/in-action.mp4", // 可以设置视频的资源地址。
    "width": "200", // 可以设置视频的宽度。
    "height": "100", // 可以设置视频的高度。
    "style": "border:1px solid #ccc", // 可以设置视频的样式。
    "className": "component-base-style",
    "controls": true, // 可以设置是否显示播放控件。
    "autoplay": true, // 可以设置是否自动播放。
    "loop": true, // 可以设置是否循环播放。
    "muted": true, // 可以设置是否静音播放。
    "poster": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/designer-default-icon.jpg" // 可以设置视频的封面图片。
  },
  "children": [],
  "id": "62561452"
}
```
