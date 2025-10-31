# Heading 组件生成schema

## Heading 组件生成schema代码示例

### 基本用法

可配置 children 来展示对应的标题内容

```json
{
  "componentName": "h1",
  "props": {
    "className": "component-base-style"
  },
  "children": "Heading",
  "id": "3261c636"
}
```

### 标题类型

标题分为 h1（一级标题）、h2（二级标题）、h3（三级标题）、h4（四级标题）、h5（五级标题）、h6（六级标题），通过配置 componentName 生成对应的标题

```json
{
  "componentName": "h1",
  "props": {
    "className": "component-base-style"
  },
  "children": "Heading",
  "id": "3261c636"
}
```

### 设置样式

通过对应的 style 属性，可以设置需要的样式。

```json
{
  "componentName": "h1",
  "props": {
    "className": "component-base-style",
    "style": "color: #f000f0;"
  },
  "children": "Heading",
  "id": "3261c636"
}
```

### 设置ref

通过对应的 ref 属性，可以设置需要的ref引用类名。

```json
{
  "componentName": "h1",
  "props": {
    "className": "component-base-style",
    "style": "color: #f000f0;",
    "ref": "heading"
  },
  "children": "Heading",
  "id": "3261c636"
}
```

### id属性

通过对应的 id 属性，可以设置元素id值。

```json
{
  "componentName": "h1",
  "props": {
    "className": "component-base-style",
    "id": "heading"
  },
  "children": "Heading",
  "id": "3261c636"
}
```