# HarmonyComponent tabbar 组件生成 schema

HarmonyTabBar 是一个导航菜单组件
componentName 为渲染时候的组件名称，props 为组件绑定的属性，children 为子组件列表，id 为可以动态生成的唯一 id。
componentName 为 HarmonyTabBarItem 的组件， 通过对应的 to 属性，可以设置跳转页面。 通过对应的 iconPath 属性，可以设置显示的图标， 通过对应的 selectedIconPath 属性，可以设置选中后的图标，  通过对应的 text 属性，可以设置显示的名称。

## tabbar 组件生成 schema 代码示例

### 基本用法

```json
{
  "componentName": "HarmonyTabBar",
  "props": {
    "style": "position: fixed; left: 0; bottom: 0; width: calc(100% - 24px); border-top: 1px solid rgba(0, 0, 0, 0.33); z-index: 1; background: #fff;", // 设置导航菜单的样式。
    "className": "",
    "ref": "tabbar", // 可以设置需要的ref引用类名
  },
  "children": [
    {
      "componentName": "HarmonyTabBarItem",
      "props": {
        "to": {
          "name": "",
          "path": ""
        },
        "iconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/home.svg",
        "selectedIconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/home-active.svg",
        "text": "首页"
      },
      "id": "23425346"
    },
    {
      "componentName": "HarmonyTabBarItem",
      "props": {
        "to": {
          "name": "",
          "path": ""
        },
        "iconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/menu.svg",
        "selectedIconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/menu-active.svg",
        "text": "菜单"
      },
      "id": "54253623"
    },
    {
      "componentName": "HarmonyTabBarItem",
      "props": {
        "to": {
          "name": "",
          "path": ""
        },
        "iconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/cart.svg",
        "selectedIconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/cart-active.svg",
        "text": "购物车"
      },
      "id": "54539326"
    },
    {
      "componentName": "HarmonyTabBarItem",
      "props": {
        "to": {
          "name": "",
          "path": ""
        },
        "iconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/my.svg",
        "selectedIconPath": "https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/harmony/images/tabbar/my-active.svg",
        "text": "我的"
      },
      "id": "23655268"
    }
  ],
  "id": "3c543344"
}
```
