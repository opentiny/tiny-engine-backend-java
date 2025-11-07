# HarmonyComponent navigator 组件生成 schema

HarmonyNavigator 是一个页面跳转组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## navigator 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyNavigator",
  "props": {
    "open-type": "navigate", // 可以设置跳转方式。可选值为 navigate 、 redirect 、 switchTab 、 reLaunch 、 navigateBack
    "target": "self", // 可以设置跳转后页面的打开方式。可选值为 self 、 top 、 parent
    "className": "component-base-style",
    "url": "https://huaweicloud.com", // 可以设置跳转的页面路径。
    "delta": 1, // 可以设置回退的层数。当 open-type 为 'navigateBack' 时有效，表示回退的层数
    "animation-type": "pop-in", // 可以设置跳转页面的动画效果。可选值为 pop-in 、 pop-out 、 none
    "animation-duration": 1000, // 可以设置跳转页面的动画时长。单位为 ms
    "render-link": true // 可以设置是否给 navigator 组件加一层 a 标签控制 ssr 渲染。
  },
  "children": [
    {
      "componentName": "HarmonyButton",
      "props": {
        "text": "跳转页面"
      },
      "id": "62555236"
    }
  ],
  "id": "55325f22"
}
```
