# icon 组件生成schema

Icon是一个图标组件。
componentName为渲染时候的组件名称，props为组件绑定的属性，children为子组件列表，id为可以动态生成的唯一id。

## icon 组件生成schema代码示例

```json
{
  "componentName": "Icon",
  "props": {
    "name": "IconDel",
    "className": "component-base-style", // 可通过 name 属性设置图标名称，IconDel（删除），IconChevronRight（向右），IconChevronLeft（向左），IconSmile（笑脸），IconMobileView（手机），IconTelephone（电话），IconLanguage（国际化），IcoSearch（搜索）
     "style": "color: #f0f00f;" // 通过对应的 style 属性，可以设置需要的样式。
  },
  "children": [],
  "id": "5e4451af"
}
```

