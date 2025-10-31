# HarmonyComponent swiper 组件生成 schema

HarmonySwiper 是一个滑块视图组件
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。
componentName 为 HarmonySwiperItem 的组件， 通过对应的 title 属性，可以设置标题。 通过对应的 name 属性，可以设置唯一标识。

## swiper 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonySwiper",
  "props": {
    "height": "180px",
    "className": "component-base-style",
    "autoplay": true, // 可以设置是否自动切换。
    "current": 0, // 可以设置当前所在滑块的 index。
    "interval": 5000, // 可以设置自动切换的时间间隔，单位为毫秒。
    "circular": true, // 可以设置是否采用衔接滑动，即播放到末尾后重新回到开头。
    "vertical": true, // 可以设置滑动方向是否为纵向。
    "ref": "swiper", // 可以设置需要的ref引用类名。
  },
  "children": [
    {
      "componentName": "HarmonySwiperItem",
      "props": {
        "title": "swiper-item-a"
      },
      "children": [
        {
          "componentName": "div",
          "id": "66837336"
        }
      ],
      "id": "46446424"
    },
    {
      "componentName": "HarmonySwiperItem",
      "props": {
        "title": "swiper-item-b"
      },
      "children": [
        {
          "componentName": "div",
          "id": "24122354"
        }
      ],
      "id": "63422654"
    }
  ],
  "id": "362166a8"
}
```
