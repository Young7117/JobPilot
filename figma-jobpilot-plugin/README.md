# JobPilot AI Figma Prototype Generator

这是一个免费的本地 Figma development plugin，用来绕开 Figma MCP Starter 额度限制，把 JobPilot AI 原型设计稿写入当前打开的 Figma 文件。

## 使用步骤

1. 打开 Figma 桌面版或浏览器版。
2. 打开 drafts 中的 `Jobpilot` 文件，或任意你想写入的 Figma design 文件。
3. 在 Figma 菜单中选择：

```text
Plugins -> Development -> Import plugin from manifest...
```

4. 选择本目录下的文件：

```text
D:\Vibe_coding\figma-jobpilot-plugin\manifest.json
```

5. 导入后运行：

```text
Plugins -> Development -> JobPilot AI Prototype Generator
```

运行成功后，会先清理旧的 `JobPilot / ...` 页面，再重新生成这些页面：

```text
JobPilot / 00 Cover
JobPilot / 01 Design System
JobPilot / 02 User Flows
JobPilot / 03 MVP Screens
JobPilot / 04 AI States
JobPilot / 05 Future Multi-industry
```

## 生成内容

- 封面
- 设计系统色彩和业务组件
- 核心用户流程
- MVP 桌面端页面原型
- AI 交互状态
- 多行业扩展方向

## 当前版本

当前插件是 V4 soft-surface prototype，相比前一版改进：

- 修复封面标题被截断的问题
- 封面增加产品工作台缩略图
- MVP Screens 补齐 Dashboard、简历、岗位、作战卡、题库练习、知识库、投递复盘、设置
- 文字框改为自适应高度，减少截断
- 重复运行时自动清理旧的 `JobPilot / ...` 页面
- 视觉风格升级为浅蓝灰底、柔和阴影、浮起卡片、压印式控件
- 侧边栏从普通深色后台风改成浅色软表面
- 按钮和标签改为更圆的胶囊形态，并修复文字居中
- 封面右侧预览卡移除突兀黑色竖栏，改为浅色浮起仪表面板
- 修复封面预览里的“开始练习”按钮错位
- 主要统计卡和表格容器调整为更柔和、清爽的软表面样式

## 注意事项

- 插件不会调用外部 API。
- 插件不会走 Figma MCP，所以不消耗 MCP tool call 额度。
- 插件会在当前文件中新建 `JobPilot / ...` 页面。
- 如果重复运行，会自动清理旧的 `JobPilot / ...` 页面，然后生成新版本。

## 排错

如果 Figma 导入 manifest 时提示 `documentAccess` 不支持，可以打开 `manifest.json` 删除这一行后再导入：

```json
"documentAccess": "dynamic-page"
```

如果提示插件 ID 冲突，可以修改 `manifest.json` 里的 `id` 为任意新的数字字符串。

## 关联本地设计文件

- `D:\Vibe_coding\design.md`
- `D:\Vibe_coding\preview.html`
- `D:\Vibe_coding\docs\design\figma-screen-brief.md`
