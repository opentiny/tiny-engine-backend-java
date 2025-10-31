# HarmonyComponent progress 组件生成 schema

HarmonyProgress 是一个进度条组件。
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。

## progress 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyProgress",
  "props": {
    "stroke-width": 6, // 可以设置进度条的宽度。
    "percent": 20, // 可以设置进度条的百分比。
    "className": "component-base-style",
    "show-info": true, // 可以设置是否在进度条右侧显示百分比。
    "activeColor": "#147523" // 可以设置进度条的激活状态颜色。
  },
  "children": [],
  "id": "61b4ff44"
}
```
