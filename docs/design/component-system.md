# JobPilot AI 组件系统

更新时间：2026-06-24

## 1. 设计系统目标

第一版前端使用 Ant Design，优先复用成熟组件，不自造复杂组件。自定义组件只用于 JobPilot AI 的业务语义，例如岗位作战卡、能力标签矩阵、AI 评分块、知识条目卡片。

长期目标是沉淀一套跨行业可扩展的求职备战设计系统。

## 2. 视觉基调

关键词：

- 专业
- 克制
- 清晰
- 高信息密度
- 可反复使用

不要做成营销站，不使用夸张 hero，不使用大面积渐变或装饰图形。

## 3. 色彩

推荐语义色：

```text
primary:   #1677ff  主要行动、链接、选中态
success:   #16a34a  优势、已掌握、通过
warning:   #d97706  风险、需复习、待处理
danger:    #dc2626  错误、短板、失败
info:      #0891b2  AI 信息、系统提示
neutral-0: #ffffff  页面和卡片背景
neutral-1: #f8fafc  页面底色
neutral-2: #eef2f7  分割和弱背景
neutral-6: #475569  次级文字
neutral-9: #0f172a  主文字
```

使用原则：

- 页面主背景使用浅中性色。
- 主按钮使用 primary。
- 优势和短板必须用不同语义色区分。
- 风险提醒使用 warning，不和错误态混用。
- 避免整站只由蓝紫色或单一色相构成。

## 4. 字体与排版

字体：

```text
font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
```

字号建议：

```text
页面标题: 24px / 32px / 600
区块标题: 18px / 26px / 600
卡片标题: 16px / 24px / 600
正文:     14px / 22px / 400
辅助文字: 12px / 20px / 400
数据数字: 28px / 36px / 600
```

排版原则：

- 工作台内不要使用过大的展示字体。
- 表格、列表和筛选器优先保证可扫描性。
- 长文本内容使用 680px 到 820px 的舒适阅读宽度。

## 5. 间距

推荐间距 token：

```text
space-1: 4px
space-2: 8px
space-3: 12px
space-4: 16px
space-5: 20px
space-6: 24px
space-8: 32px
```

使用原则：

- 页面内容区 padding 使用 24px。
- 卡片内部 padding 使用 16px 或 20px。
- 表单项之间使用 16px。
- 筛选器之间使用 8px 或 12px。

## 6. 圆角和阴影

圆角：

```text
radius-sm: 4px
radius-md: 6px
radius-lg: 8px
```

使用原则：

- 普通卡片最大 8px。
- 按钮、输入框跟随 Ant Design 默认风格。
- 不使用过度圆润的装饰性卡片。

阴影：

- 默认页面不依赖重阴影。
- 抽屉、弹窗和浮层可使用 Ant Design 默认阴影。

## 7. 基础组件映射

```text
按钮                 Ant Button
输入框               Ant Input / TextArea
选择器               Ant Select
日期                 Ant DatePicker
筛选                 Ant Select / Segmented / Checkbox
列表                 Ant Table / List
统计                 Ant Statistic
标签                 Ant Tag
进度                 Ant Progress
步骤                 Ant Steps
抽屉                 Ant Drawer
弹窗                 Ant Modal
提示                 Ant Alert / Message / Notification
折叠参考答案          Ant Collapse
上传简历              Ant Upload
```

## 8. 业务组件

### 8.1 `BattleCardSummary`

用途：作战卡顶部摘要。

内容：

- 公司和岗位
- 匹配分
- 简历版本
- 生成时间
- 生成来源：缓存或 AI 新生成

### 8.2 `MatchScoreMeter`

用途：展示岗位匹配分。

分段：

```text
0-59   风险较高
60-74  可尝试
75-89  匹配良好
90-100 高度匹配
```

### 8.3 `CapabilityTagMatrix`

用途：展示岗位能力标签和用户掌握情况。

字段：

- 标签名
- 类型：专业知识、工具技能、项目经验、业务能力
- 匹配状态：已具备、需补强、缺失

第一版标签类型可以先偏计算机：

- 编程语言
- 框架
- 数据库
- 中间件
- 部署
- 前端
- AI 应用

### 8.4 `QuestionSourceBadge`

用途：标识题目来源。

状态：

```text
公共题库
AI 补充
候选题
个人收藏
```

### 8.5 `PracticeEvaluationPanel`

用途：展示 AI 评分。

分区：

- 总分
- 回答优点
- 回答问题
- 优化回答
- 面试官追问

### 8.6 `KnowledgeItemCard`

用途：知识库列表卡片。

内容：

- 标题
- 分类
- 标签
- 掌握程度
- 来源
- 更新时间

### 8.7 `ApplicationStatusTimeline`

用途：展示投递状态流转。

状态：

```text
准备中
已投递
笔试中
面试中
已通过
已拒绝
已放弃
```

## 9. 图标

建议使用 `lucide-react` 或 Ant Design Icons。

图标用途：

- 上传：Upload
- 新建：Plus
- 生成：Sparkles
- 评分：Gauge
- 知识库：BookOpen
- 投递：Send
- 风险：TriangleAlert
- 成功：CheckCircle
- 搜索：Search
- 筛选：SlidersHorizontal

图标按钮必须有 tooltip 或可访问名称。

## 10. 空状态

空状态不写长段说明，只给一个明确动作。

示例：

```text
还没有简历
录入第一份简历
```

```text
还没有岗位
创建目标岗位
```

```text
还没有作战卡
生成岗位作战卡
```

## 11. 多行业扩展组件注意事项

组件命名避免写死技术语境。

推荐：

- `CapabilityTag`
- `JobDirectionSelect`
- `IndustrySelect`
- `QuestionTypeSelect`

避免：

- `TechStackOnlySelect`
- `DeveloperQuestionCard`
- `JavaTagMatrix`

