# JobPilot AI Figma Screen Brief

更新时间：2026-06-24

## 1. 用途

本文件用于后续把 JobPilot AI 设计写入 Figma。当前先作为设计交付说明和画布清单，不直接绑定某个 Figma 文件。

如果要真正生成 Figma 文件，需要补充：

- 目标 Figma 文件 URL 或新建文件需求
- 是否已有设计系统
- 是否使用 Ant Design 风格组件库
- 需要优先生成的页面范围

## 2. 推荐 Figma 页面结构

```text
00 Cover
01 Foundations
02 Components
03 User Flows
04 MVP Screens
05 AI States
06 Future Multi-industry
```

## 3. 画布尺寸

桌面端：

```text
1440 x 1024
```

移动端：

```text
390 x 844
```

第一版优先完成桌面端，因为 JobPilot AI 是高信息密度工作台产品。移动端保证核心流程可用即可。

## 4. MVP Screen List

### 4.1 Auth

- Login
- Register

### 4.2 Dashboard

- Empty Dashboard
- Active Dashboard

### 4.3 Resume

- Resume List
- Resume Upload Modal
- Resume Text Editor
- Resume Parse Failed

### 4.4 Jobs

- Job List
- Create Job Drawer
- Job Detail

### 4.5 Battle Card

- Battle Card Generating
- Battle Card Detail
- Battle Card With Risks

### 4.6 Questions

- Question Generation Progress
- Question List
- Question Detail
- Practice Evaluation Result

### 4.7 Knowledge

- Knowledge List
- Knowledge Detail Drawer
- Save To Knowledge Modal

### 4.8 Applications

- Application List
- Application Detail
- Review Suggestion Result

## 5. 关键桌面页面布局

### 5.1 Dashboard

```text
┌────────────────────────────────────────────────────┐
│ Top Context Bar                                    │
├──────────┬─────────────────────────────────────────┤
│ Sidebar  │ Welcome + Next Action                   │
│          │ Stats Row                               │
│          │ Active Jobs     | Review Queue          │
│          │ Recent Knowledge | Application Status   │
└──────────┴─────────────────────────────────────────┘
```

### 5.2 Battle Card Detail

```text
┌────────────────────────────────────────────────────┐
│ Job Summary + Match Score                          │
├──────────────────────────────┬─────────────────────┤
│ Core Requirements             │ Action Panel        │
│ Capability Breakdown          │ 3 Day Plan          │
│ Matched Points                │ 7 Day Plan          │
│ Weak Points                   │ Risk Tips           │
│ Interview Focus               │ Generate Questions  │
└──────────────────────────────┴─────────────────────┘
```

### 5.3 Question Practice

```text
┌────────────────────────────────────────────────────┐
│ Question Title + Tags + Difficulty                 │
├──────────────────────────────┬─────────────────────┤
│ Question Content              │ Practice History    │
│ User Answer Editor            │ Mastery Status      │
│ Submit Evaluation             │ Related Job         │
├──────────────────────────────┴─────────────────────┤
│ AI Evaluation Result                                │
│ Optimized Answer                                    │
│ Follow-up Questions                                 │
└────────────────────────────────────────────────────┘
```

## 6. 组件清单

需要在 Figma 中优先建立或复用：

- App Shell
- Sidebar Navigation
- Top Context Bar
- Page Header
- Stat Card
- Filter Bar
- Data Table
- Empty State
- Battle Card Summary
- Match Score Meter
- Capability Tag Matrix
- Question Source Badge
- Practice Evaluation Panel
- Knowledge Item Card
- Application Status Timeline
- AI Generating Stepper

## 7. 设计系统变量建议

颜色变量：

```text
color/bg/page
color/bg/surface
color/text/primary
color/text/secondary
color/border/default
color/action/primary
color/status/success
color/status/warning
color/status/danger
color/status/info
```

间距变量：

```text
space/4
space/8
space/12
space/16
space/20
space/24
space/32
```

圆角变量：

```text
radius/4
radius/6
radius/8
```

## 8. Figma 生成顺序

建议顺序：

1. Foundations
2. Components
3. App Shell
4. Dashboard
5. Resume Flow
6. Job and Battle Card Flow
7. Question Practice Flow
8. Knowledge Flow
9. Application Review Flow
10. AI States

## 9. 设计校验清单

每个 Figma 页面完成后检查：

- 是否使用统一左侧导航和顶部上下文栏
- 主要行动是否清晰
- 空状态是否有下一步按钮
- AI 生成状态是否可理解
- 长文本是否可读
- 表格在窄屏下是否可降级为列表
- 计算机领域标签是否没有写死为唯一行业
- 公共题库和个人知识库是否视觉上清晰区分
- AI 补题和候选题机制是否可见

