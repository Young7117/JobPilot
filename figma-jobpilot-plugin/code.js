(async () => {
  const font = { family: "Inter", style: "Regular" };
  await figma.loadFontAsync(font);

  const colors = {
    page: "#EAF2FA",
    surface: "#F1F7FC",
    soft: "#E3EDF7",
    subtle: "#F6FAFD",
    navy: "#14213D",
    text: "#101D33",
    secondary: "#405166",
    muted: "#718299",
    border: "#D8E4EF",
    borderStrong: "#BFD0E1",
    primary: "#6EA6D8",
    primaryDark: "#417FAF",
    blueBg: "#E4F0FA",
    blueBorder: "#C4DCEF",
    green: "#16A34A",
    greenBg: "#EAF7F1",
    greenBorder: "#BFE4D2",
    amber: "#D97706",
    amberBg: "#FFF7E8",
    amberBorder: "#F4D598",
    red: "#DC2626",
    redBg: "#FFF0F0",
    redBorder: "#F0C4C4",
    cyan: "#0891B2",
    cyanBg: "#E6F6F8"
  };

  const created = [];

  function rgb(hex) {
    const value = hex.replace("#", "");
    return {
      r: parseInt(value.slice(0, 2), 16) / 255,
      g: parseInt(value.slice(2, 4), 16) / 255,
      b: parseInt(value.slice(4, 6), 16) / 255
    };
  }

  function solid(hex, opacity = 1) {
    return { type: "SOLID", color: rgb(hex), opacity };
  }

  function rgba(hex, alpha) {
    const value = rgb(hex);
    return { r: value.r, g: value.g, b: value.b, a: alpha };
  }

  function raisedEffects() {
    return [
      {
        type: "DROP_SHADOW",
        visible: true,
        color: rgba("#FFFFFF", 0.86),
        offset: { x: -10, y: -10 },
        radius: 22,
        spread: 0,
        blendMode: "NORMAL"
      },
      {
        type: "DROP_SHADOW",
        visible: true,
        color: rgba("#9DB5CC", 0.28),
        offset: { x: 12, y: 14 },
        radius: 28,
        spread: 0,
        blendMode: "NORMAL"
      }
    ];
  }

  function subtleRaisedEffects() {
    return [
      {
        type: "DROP_SHADOW",
        visible: true,
        color: rgba("#FFFFFF", 0.7),
        offset: { x: -5, y: -5 },
        radius: 12,
        spread: 0,
        blendMode: "NORMAL"
      },
      {
        type: "DROP_SHADOW",
        visible: true,
        color: rgba("#9DB5CC", 0.18),
        offset: { x: 6, y: 8 },
        radius: 16,
        spread: 0,
        blendMode: "NORMAL"
      }
    ];
  }

  function parentNode(parent, node) {
    parent.appendChild(node);
    created.push(node);
    return node;
  }

  function makeFrame(parent, name, x, y, width, height, fill = colors.surface, radius = 8, stroke = colors.border) {
    const node = parentNode(parent, figma.createFrame());
    node.name = name;
    node.x = x;
    node.y = y;
    node.resize(width, height);
    node.fills = fill ? [solid(fill)] : [];
    node.strokes = stroke ? [solid(stroke)] : [];
    node.strokeWeight = stroke ? 1 : 0;
    node.cornerRadius = radius;
    node.clipsContent = false;
    if (fill && stroke && radius >= 8 && width > 48 && height > 30) {
      node.effects = width > 260 || height > 120 ? raisedEffects() : subtleRaisedEffects();
    }
    return node;
  }

  function makeRect(parent, name, x, y, width, height, fill, radius = 6, stroke = null) {
    const node = parentNode(parent, figma.createRectangle());
    node.name = name;
    node.x = x;
    node.y = y;
    node.resize(width, height);
    node.fills = [solid(fill)];
    node.strokes = stroke ? [solid(stroke)] : [];
    node.strokeWeight = stroke ? 1 : 0;
    node.cornerRadius = radius;
    return node;
  }

  function makeText(parent, name, value, x, y, width, height, size = 14, color = colors.text, lineHeight = 20) {
    const node = parentNode(parent, figma.createText());
    node.name = name;
    node.fontName = font;
    node.characters = value;
    node.fontSize = size;
    node.lineHeight = { unit: "PIXELS", value: lineHeight };
    node.letterSpacing = { unit: "PIXELS", value: 0 };
    node.fills = [solid(color)];
    node.x = x;
    node.y = y;
    node.resize(width, height);
    node.textAutoResize = "HEIGHT";
    return node;
  }

  function makePill(parent, label, x, y, options = {}) {
    const width = options.width || Math.max(64, label.length * 12 + 24);
    const bg = options.bg || colors.soft;
    const fg = options.fg || colors.secondary;
    const stroke = options.stroke || colors.border;
    const height = options.height || 28;
    const pill = makeFrame(parent, "Tag / " + label, x, y, width, height, bg, height / 2, stroke);
    const labelNode = makeText(pill, "Tag label", label, 0, 5, width, 18, 12, fg, 18);
    labelNode.textAlignHorizontal = "CENTER";
    return pill;
  }

  function makeButton(parent, label, x, y, options = {}) {
    const variant = options.variant || "primary";
    const width = options.width || Math.max(92, label.length * 14 + 32);
    const height = options.height || 40;
    const bg = variant === "primary" ? colors.primary : colors.surface;
    const fg = variant === "primary" ? "#FFFFFF" : colors.text;
    const stroke = variant === "primary" ? colors.primary : colors.borderStrong;
    const button = makeFrame(parent, "Button / " + label, x, y, width, height, bg, height / 2, stroke);
    const labelNode = makeText(button, "Button label", label, 0, Math.round((height - 20) / 2), width, 20, 14, fg, 20);
    labelNode.textAlignHorizontal = "CENTER";
    return button;
  }

  function makeSectionTitle(parent, title, subtitle, x, y, width) {
    makeText(parent, "Section title / " + title, title, x, y, width, 32, 22, colors.text, 30);
    if (subtitle) {
      makeText(parent, "Section subtitle / " + title, subtitle, x, y + 38, width, 54, 14, colors.secondary, 22);
    }
  }

  function makeStat(parent, label, value, hint, x, y, width = 172) {
    const card = makeFrame(parent, "Stat card / " + label, x, y, width, 104, colors.surface, 16, null);
    makeText(card, "Label", label, 16, 14, width - 32, 18, 12, colors.muted, 18);
    makeText(card, "Value", value, 16, 38, width - 32, 36, 28, colors.text, 34);
    makeText(card, "Hint", hint, 16, 78, width - 32, 18, 12, colors.secondary, 18);
    return card;
  }

  function makeProgress(parent, label, percent, x, y, width, color = colors.primary) {
    makeText(parent, "Progress label / " + label, label, x, y, 150, 20, 13, colors.text, 20);
    makeRect(parent, "Progress track / " + label, x + 160, y + 7, width, 8, "#E2E8F0", 4);
    makeRect(parent, "Progress fill / " + label, x + 160, y + 7, width * percent, 8, color, 4);
    makeText(parent, "Progress pct / " + label, Math.round(percent * 100) + "%", x + width + 172, y, 46, 18, 12, colors.secondary, 18);
  }

  function makeTable(parent, name, x, y, width, rows) {
    const rowHeight = 58;
    const height = 44 + rows.length * rowHeight;
    const table = makeFrame(parent, name, x, y, width, height, colors.surface, 16, null);
    makeRect(table, "Table header", 0, 0, width, 44, colors.subtle, 16);
    makeText(table, "Header / item", "项目", 16, 13, width * 0.42, 18, 12, colors.secondary, 18);
    makeText(table, "Header / status", "状态", width * 0.48, 13, width * 0.18, 18, 12, colors.secondary, 18);
    makeText(table, "Header / action", "下一步", width * 0.68, 13, width * 0.26, 18, 12, colors.secondary, 18);

    rows.forEach((row, index) => {
      const yy = 44 + index * rowHeight;
      if (index > 0) makeRect(table, "Row divider", 0, yy, width, 1, colors.border, 0);
      makeText(table, "Row title", row[0], 16, yy + 10, width * 0.4, 20, 14, colors.text, 20);
      makeText(table, "Row meta", row[1], 16, yy + 32, width * 0.4, 18, 12, colors.muted, 18);
      const done = row[2].includes("已") || row[2].includes("面试");
      makePill(table, row[2], width * 0.48, yy + 16, {
        width: 76,
        bg: done ? colors.greenBg : colors.blueBg,
        fg: done ? colors.green : colors.primary,
        stroke: done ? colors.greenBorder : colors.blueBorder
      });
      makeText(table, "Row action", row[3], width * 0.68, yy + 18, width * 0.26, 18, 13, colors.primary, 18);
    });

    return table;
  }

  function makeFlowNode(parent, label, x, y, width = 156, height = 62, fill = colors.surface) {
    const node = makeFrame(parent, "Flow node / " + label, x, y, width, height, fill, 8, colors.border);
    makeText(node, "Flow label", label, 12, 12, width - 24, height - 24, 14, colors.text, 20);
    return node;
  }

  function makeLine(parent, x, y, width) {
    const line = parentNode(parent, figma.createLine());
    line.name = "Connector";
    line.x = x;
    line.y = y;
    line.resize(width, 0);
    line.strokes = [solid(colors.borderStrong)];
    line.strokeWeight = 2;
    return line;
  }

  function makeDesktopShell(page, name, x, y, title, subtitle) {
    const screen = makeFrame(page, "Screen / " + name, x, y, 1440, 1024, colors.page, 0, colors.border);
    const sidebar = makeFrame(screen, "Sidebar / soft surface", 0, 0, 236, 1024, colors.surface, 0, colors.border);
    makeRect(sidebar, "Brand mark", 18, 18, 34, 34, colors.primary, 8);
    makeText(sidebar, "Brand initials", "JP", 26, 26, 24, 16, 13, "#FFFFFF", 16);
    makeText(sidebar, "Brand name", "JobPilot AI", 62, 18, 130, 20, 16, colors.text, 20);
    makeText(sidebar, "Brand subtitle", "岗位作战台", 62, 40, 120, 18, 12, colors.muted, 18);

    const navs = ["Dashboard", "简历", "岗位", "作战卡", "题库练习", "知识库", "投递复盘", "设置"];
    navs.forEach((nav, index) => {
      const active = nav === name || (name === "作战卡详情" && nav === "作战卡") || (name === "题目练习" && nav === "题库练习");
      makeRect(sidebar, "Nav item / " + nav, 14, 84 + index * 44, 208, 38, active ? colors.blueBg : colors.surface, 9, active ? colors.blueBorder : null);
      makeRect(sidebar, "Nav icon / " + nav, 26, 98 + index * 44, 10, 10, active ? colors.primary : colors.borderStrong, 3);
      makeText(sidebar, "Nav label / " + nav, nav, 50, 94 + index * 44, 130, 18, 14, active ? colors.primaryDark : colors.secondary, 18);
    });

    const top = makeFrame(screen, "Top context bar", 236, 0, 1204, 62, colors.page, 0, colors.border);
    makePill(top, "默认简历：Java 后端实习版", 24, 18, { width: 220, bg: colors.surface });
    makePill(top, "活跃岗位：后端开发工程师", 258, 18, { width: 226, bg: colors.surface });
    makePill(top, "上海 · 12-18K", 498, 18, { width: 118, bg: colors.surface });
    makeButton(top, "上传简历", 790, 12, { variant: "secondary", width: 94 });
    makeButton(top, "新建岗位", 894, 12, { variant: "secondary", width: 94 });
    makeButton(top, "生成作战卡", 998, 12, { variant: "primary", width: 118 });

    makeText(screen, "Page title", title, 260, 90, 520, 34, 24, colors.text, 32);
    makeText(screen, "Page subtitle", subtitle, 260, 126, 800, 24, 14, colors.secondary, 22);
    return screen;
  }

  async function makePages() {
    const coverPage = figma.createPage();
    coverPage.name = "JobPilot / 00 Cover";
    await figma.setCurrentPageAsync(coverPage);
    const pagesToRemove = figma.root.children.filter((page) => page !== coverPage && page.name.startsWith("JobPilot /"));
    pagesToRemove.forEach((page) => page.remove());

    return {
      cover: coverPage,
      system: createPage("JobPilot / 01 Design System"),
      flows: createPage("JobPilot / 02 User Flows"),
      screens: createPage("JobPilot / 03 MVP Screens"),
      states: createPage("JobPilot / 04 AI States"),
      future: createPage("JobPilot / 05 Future Multi-industry")
    };
  }

  function createPage(name) {
    const page = figma.createPage();
    page.name = name;
    return page;
  }

  const pages = await makePages();

  // Cover
  const cover = makeFrame(pages.cover, "Cover / JobPilot AI", 0, 0, 1440, 1024, colors.page, 0, null);
  makeRect(cover, "Primary rail", 0, 0, 16, 1024, colors.primary, 0);
  makeText(cover, "Eyebrow", "JOBPILOT AI · 岗位作战与面试知识库系统", 96, 86, 620, 24, 14, colors.primary, 22);
  makeText(cover, "Title", "围绕一个岗位，\n打一场有准备的仗", 96, 138, 650, 132, 48, colors.text, 58);
  makeText(
    cover,
    "Subtitle",
    "长期面向各行各业求职者，MVP 先聚焦计算机领域。设计稿包含产品工作台、岗位作战卡、题库优先检索、AI 补题、练习评分、知识库沉淀和投递复盘。",
    100,
    308,
    650,
    92,
    18,
    colors.secondary,
    28
  );
  makeButton(cover, "MVP 桌面原型", 100, 432, { width: 150 });
  makePill(cover, "计算机领域 MVP", 270, 438, { width: 140, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
  makePill(cover, "长期多行业扩展", 420, 438, { width: 150, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });

  const preview = makeFrame(cover, "Product preview / Soft dashboard", 760, 96, 600, 360, colors.surface, 18, colors.border);
  makeRect(preview, "Preview signal dot", 28, 28, 34, 34, colors.primary, 17, null);
  makeText(preview, "Preview nav signal", "SIGNAL", 360, 34, 62, 18, 12, colors.secondary, 18);
  makeText(preview, "Preview nav surface", "SURFACE", 432, 34, 72, 18, 12, colors.secondary, 18);
  makeText(preview, "Preview nav system", "SYSTEM", 516, 34, 60, 18, 12, colors.secondary, 18);

  const previewMain = makeFrame(preview, "Preview main surface", 28, 92, 280, 226, colors.subtle, 22, null);
  makeText(previewMain, "Preview dashboard label", "JOB READINESS", 24, 26, 160, 18, 12, colors.muted, 18);
  makeText(previewMain, "Preview dashboard title", "Dashboard", 24, 54, 180, 30, 24, colors.text, 32);
  [["简历", "3"], ["岗位", "8"], ["题目", "42"], ["待复习", "9"]].forEach((item, index) => {
    const card = makeFrame(previewMain, "Preview stat / " + item[0], 24 + (index % 2) * 112, 104 + Math.floor(index / 2) * 64, 94, 48, colors.surface, 16, null);
    makeText(card, "Preview stat label", item[0], 14, 8, 44, 14, 11, colors.muted, 14);
    makeText(card, "Preview stat value", item[1], 14, 24, 40, 22, 20, colors.text, 24);
  });

  const previewSide = makeFrame(preview, "Preview insight surface", 334, 92, 238, 226, colors.subtle, 22, null);
  makeText(previewSide, "Preview insight eyebrow", "PRIMARY MOVE", 24, 24, 150, 18, 12, colors.muted, 18);
  makeText(previewSide, "Preview insight title", "补齐 Redis 与 Docker 部署短板", 24, 54, 174, 56, 18, colors.primaryDark, 26);
  makeText(previewSide, "Preview insight desc", "匹配分 78，公共题库召回 18 道题，AI 补充 6 道项目追问题。", 24, 120, 180, 58, 13, colors.secondary, 20);
  makeButton(previewSide, "开始练习", 104, 174, { width: 96, height: 36 });

  const coverPanel = makeFrame(cover, "Core loop panel", 760, 490, 600, 300, colors.surface, 12, colors.border);
  makeSectionTitle(coverPanel, "核心闭环", "从简历解析到投递复盘，所有内容都围绕目标岗位组织。", 28, 26, 520);
  const flowLabels = ["简历解析", "JD 分析", "岗位作战卡", "公共题库检索", "AI 补题", "练习评分", "个人知识库", "投递复盘"];
  flowLabels.forEach((label, index) => {
    makeFlowNode(coverPanel, label, 30 + (index % 4) * 138, 118 + Math.floor(index / 4) * 88, 116, 54, index === 2 ? colors.blueBg : colors.surface);
  });
  const coverNote = makeFrame(cover, "MVP scope note", 100, 560, 560, 230, colors.surface, 12, colors.border);
  makeText(coverNote, "Note title", "第一版先做完整闭环，再做细节优化", 24, 24, 420, 26, 20, colors.text, 28);
  makeText(coverNote, "Note content", "当前版本聚焦计算机领域：Java 后端、前端、AI 应用、测试、运维和数据开发。系统字段保留行业、岗位方向和能力标签，后续可扩展到产品、运营、销售、金融、教育、医药、制造等方向。", 24, 68, 500, 110, 14, colors.secondary, 22);
  makePill(coverNote, "公共题库优先", 24, 184, { width: 112, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
  makePill(coverNote, "AI 题进入候选库", 146, 184, { width: 128, bg: colors.amberBg, fg: colors.amber, stroke: colors.amberBorder });
  makePill(coverNote, "个人知识库分离", 286, 184, { width: 128, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });
  makeText(cover, "Footer", "Drafts / Jobpilot · Free local Figma development plugin · V4 soft-surface prototype", 96, 914, 760, 24, 13, colors.muted, 20);

  // Design System
  const ds = makeFrame(pages.system, "Design System / Foundations", 0, 0, 1440, 1600, colors.page, 0, null);
  makeSectionTitle(ds, "JobPilot AI 设计系统", "克制、专业、信息密度适中的工作台视觉语言。第一版复用 Ant Design 思路，后续沉淀跨行业求职备战系统。", 64, 56, 980);
  const swatches = [
    ["Primary", colors.primary],
    ["Success", colors.green],
    ["Warning", colors.amber],
    ["Danger", colors.red],
    ["Info", colors.cyan],
    ["Navy", colors.navy],
    ["Page", colors.page],
    ["Surface", colors.surface]
  ];
  swatches.forEach((item, index) => {
    const x = 64 + (index % 4) * 310;
    const y = 180 + Math.floor(index / 4) * 132;
    const card = makeFrame(ds, "Color token / " + item[0], x, y, 280, 100, colors.surface, 8, colors.border);
    makeRect(card, "Swatch", 16, 16, 56, 56, item[1], 8);
    makeText(card, "Name", item[0], 88, 20, 160, 20, 15, colors.text, 20);
    makeText(card, "Value", item[1], 88, 46, 160, 18, 12, colors.muted, 18);
  });

  makeSectionTitle(ds, "业务组件", "作战卡摘要、匹配分、能力矩阵、题目来源、AI 评分和知识库卡片。", 64, 468, 980);
  const compNames = ["BattleCardSummary", "MatchScoreMeter", "CapabilityTagMatrix", "PracticeEvaluationPanel"];
  compNames.forEach((name, index) => {
    const card = makeFrame(ds, "Component / " + name, 64 + index * 336, 564, 280, 220, colors.surface, 8, colors.border);
    makeText(card, "Component name", name, 18, 18, 230, 24, 16, colors.text, 24);
    if (index === 0) {
      makePill(card, "缓存命中", 18, 64, { width: 86, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });
      makePill(card, "Java 后端", 112, 64, { width: 88, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
      makeText(card, "Summary", "岗位：后端开发工程师\n匹配：78 分\n下一步：生成专属题目", 18, 112, 224, 70, 13, colors.secondary, 20);
    }
    if (index === 1) {
      makeRect(card, "Score outer", 24, 62, 92, 92, colors.primary, 46);
      makeRect(card, "Score inner", 41, 79, 58, 58, colors.surface, 29);
      makeText(card, "Score", "78", 56, 96, 36, 22, 20, colors.text, 22);
    }
    if (index === 2) {
      makeProgress(card, "Java", 0.88, 18, 74, 116, colors.green);
      makeProgress(card, "Redis", 0.58, 18, 114, 116, colors.amber);
      makeProgress(card, "Docker", 0.52, 18, 154, 116, colors.amber);
    }
    if (index === 3) {
      makeRect(card, "Grade block", 18, 62, 66, 66, colors.primary, 8);
      makeText(card, "Grade", "82", 36, 80, 32, 24, 24, "#FFFFFF", 28);
      makeText(card, "Feedback", "方向正确，项目细节还可以更具体。", 100, 66, 150, 60, 13, colors.secondary, 20);
    }
  });

  makeSectionTitle(ds, "状态语言", "AI 结果必须可重试、可编辑、可保存、可复盘。", 64, 860, 900);
  [
    ["生成中", colors.blueBg, colors.primary, colors.blueBorder],
    ["缓存命中", colors.greenBg, colors.green, colors.greenBorder],
    ["需复习", colors.amberBg, colors.amber, colors.amberBorder],
    ["短板风险", colors.redBg, colors.red, colors.redBorder]
  ].forEach((item, index) => makePill(ds, item[0], 64 + index * 150, 950, { width: 112, bg: item[1], fg: item[2], stroke: item[3] }));

  // User Flows
  const flowPage = makeFrame(pages.flows, "User Flow / Core Loop", 0, 0, 1600, 1200, colors.page, 0, null);
  makeSectionTitle(flowPage, "核心用户流程", "岗位为中心：从简历和 JD 出发，沉淀题目、答案、知识和投递复盘。", 64, 56, 980);
  flowLabels.forEach((label, index) => {
    const x = 70 + index * 185;
    makeFlowNode(flowPage, label, x, 180, 148, 68, index === 2 ? colors.blueBg : colors.surface);
    if (index < flowLabels.length - 1) makeLine(flowPage, x + 150, 214, 32);
  });
  const swim = makeFrame(flowPage, "Flow detail / Question generation", 64, 344, 1350, 600, colors.surface, 12, colors.border);
  makeSectionTitle(swim, "题库优先 + AI 补题", "公共题库和个人知识库分离；AI 生成题进入候选题库，避免污染公共题库。", 34, 34, 1000);
  [
    ["作战卡提取标签", 54, 144],
    ["Qdrant 语义检索", 284, 144],
    ["质量分与难度排序", 514, 144],
    ["覆盖度判断", 744, 144],
    ["专属题目清单", 974, 144],
    ["识别缺口能力点", 744, 284],
    ["AI 补充生成", 974, 284],
    ["候选题库审核", 974, 424],
    ["公共题库沉淀", 744, 424]
  ].forEach((item) => makeFlowNode(swim, item[0], item[1], item[2], 180, 62, item[0].includes("AI") ? colors.blueBg : colors.surface));

  // MVP Screens
  const dash = makeDesktopShell(pages.screens, "Dashboard", 0, 0, "Dashboard", "围绕目标岗位推进简历、作战卡、题目练习和投递复盘。");
  const hero = makeFrame(dash, "Next action panel", 260, 170, 1134, 118, colors.blueBg, 8, colors.blueBorder);
  makeText(hero, "Next action title", "下一步：补齐 Redis 与 Docker 部署短板", 22, 18, 560, 28, 20, colors.text, 28);
  makeText(hero, "Next action desc", "当前岗位匹配分 78，公共题库已召回 18 道题，建议先完成 6 道高频项目追问题。", 22, 52, 760, 26, 14, colors.secondary, 22);
  makeButton(hero, "开始练习", 960, 40, { width: 110 });

  [
    ["简历版本", "3", "1 份默认简历"],
    ["目标岗位", "8", "3 个准备中"],
    ["作战卡", "5", "平均匹配分 76"],
    ["专属题目", "42", "18 道来自公共题库"],
    ["待复习", "9", "本周建议完成"],
    ["投递记录", "6", "2 个面试中"]
  ].forEach((item, index) => makeStat(dash, item[0], item[1], item[2], 260 + index * 188, 318, 172));

  makeTable(dash, "Active jobs table", 260, 452, 660, [
    ["星河云 · 后端开发工程师", "Java 后端 · 上海", "准备中", "看作战卡"],
    ["北辰智能 · AI 应用开发", "LLM 应用 · 杭州", "面试中", "练专项题"],
    ["青桐科技 · 前端开发", "React · 远程", "已投递", "记录进展"]
  ]);
  const review = makeFrame(dash, "Review queue", 946, 452, 448, 300, colors.surface, 8, colors.border);
  makeSectionTitle(review, "本周复习队列", "", 18, 16, 300);
  makeText(review, "Review item 1", "MySQL 索引失效场景\n上次评分 68，需要补充执行计划和联合索引顺序。", 20, 70, 390, 56, 14, colors.secondary, 22);
  makeText(review, "Review item 2", "Redis 缓存击穿处理\n结合项目描述互斥锁和逻辑过期方案。", 20, 140, 390, 56, 14, colors.secondary, 22);
  makeText(review, "Review item 3", "Docker Compose 部署流程\n讲清服务名通信、环境变量和数据卷。", 20, 210, 390, 56, 14, colors.secondary, 22);

  const battle = makeDesktopShell(pages.screens, "作战卡详情", 1500, 0, "岗位作战卡", "基于简历和 JD 生成匹配分析、补强计划和面试重点。");
  const summary = makeFrame(battle, "Battle summary card", 260, 170, 740, 190, colors.surface, 8, colors.border);
  makeRect(summary, "Score outer", 24, 30, 128, 128, colors.primary, 64);
  makeRect(summary, "Score inner", 43, 49, 90, 90, colors.surface, 45);
  makeText(summary, "Score", "78", 70, 70, 42, 32, 30, colors.text, 34);
  makeText(summary, "Battle title", "星河云 · 后端开发工程师", 180, 32, 420, 28, 20, colors.text, 28);
  makeText(summary, "Battle summary", "匹配良好，但 Redis 与 Docker 部署经验需要补强。项目经历能覆盖 Spring Boot、MySQL 和接口设计。", 180, 70, 500, 60, 14, colors.secondary, 22);
  makePill(summary, "Spring Boot 已匹配", 180, 140, { width: 138, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });
  makePill(summary, "Redis 需补强", 330, 140, { width: 110, bg: colors.amberBg, fg: colors.amber, stroke: colors.amberBorder });
  makePill(summary, "Docker 需补强", 452, 140, { width: 118, bg: colors.amberBg, fg: colors.amber, stroke: colors.amberBorder });

  const cap = makeFrame(battle, "Capability matrix", 260, 390, 740, 330, colors.surface, 8, colors.border);
  makeSectionTitle(cap, "能力拆解", "", 18, 18, 300);
  makeProgress(cap, "Java / Spring Boot", 0.88, 22, 78, 380, colors.green);
  makeProgress(cap, "MySQL 索引与事务", 0.76, 22, 128, 380, colors.green);
  makeProgress(cap, "Redis 缓存设计", 0.58, 22, 178, 380, colors.amber);
  makeProgress(cap, "Docker Compose 部署", 0.52, 22, 228, 380, colors.amber);
  makeProgress(cap, "高并发排查", 0.36, 22, 278, 380, colors.red);

  const plan = makeFrame(battle, "Right action panel", 1026, 170, 368, 550, colors.surface, 8, colors.border);
  makeSectionTitle(plan, "三天补强计划", "先补高频短板，再进入题目练习。", 20, 20, 290);
  makeText(plan, "Plan detail", "Day 1 · Redis 高频问题\n缓存击穿、穿透、雪崩、一致性。\n\nDay 2 · MySQL 追问\n索引失效、事务隔离、慢查询。\n\nDay 3 · 项目部署话术\nDocker Compose + Nginx + 环境变量。", 22, 108, 310, 250, 14, colors.secondary, 22);
  makeButton(plan, "生成专属题目", 22, 390, { width: 140 });
  makePill(plan, "投递风险：中等", 22, 444, { width: 126, bg: colors.amberBg, fg: colors.amber, stroke: colors.amberBorder });

  const q = makeDesktopShell(pages.screens, "题目练习", 0, 1120, "题库练习", "公共题库优先召回，题目不足时 AI 补充并进入候选题库。");
  const steps = makeFrame(q, "Question generation chain", 260, 170, 1134, 108, colors.surface, 8, colors.border);
  [
    ["提取标签", "Java、Redis、MySQL、Docker"],
    ["公共题库召回", "18 道高质量题目"],
    ["AI 补充缺口", "生成 6 道项目追问题"],
    ["候选题入库", "去重、评分、审核"]
  ].forEach((step, index) => {
    const card = makeFrame(steps, "AI step / " + step[0], 18 + index * 276, 20, 250, 68, index < 2 ? colors.greenBg : index === 2 ? colors.blueBg : colors.surface, 8, index < 2 ? colors.greenBorder : index === 2 ? colors.blueBorder : colors.border);
    makeText(card, "Step title", step[0], 14, 12, 200, 20, 14, colors.text, 20);
    makeText(card, "Step desc", step[1], 14, 36, 210, 18, 12, colors.secondary, 18);
  });

  const questions = makeFrame(q, "Question list", 260, 310, 680, 410, colors.surface, 8, colors.border);
  makeSectionTitle(questions, "推荐题目", "", 18, 18, 400);
  [
    ["如何解决缓存击穿问题？结合你的项目说明。", "公共题库 · Redis · 中等"],
    ["你的项目如何用 Docker Compose 完成一键部署？", "AI 补充 · Docker · 中等"],
    ["联合索引什么时候会失效？如何用执行计划验证？", "公共题库 · MySQL · 中等"]
  ].forEach((item, index) => {
    makeRect(questions, "Question row", 18, 66 + index * 104, 640, 82, "#FBFDFF", 8, colors.border);
    makeText(questions, "Question title", item[0], 36, 80 + index * 104, 560, 22, 15, colors.text, 22);
    makeText(questions, "Question meta", item[1], 36, 110 + index * 104, 360, 18, 12, colors.muted, 18);
  });

  const practice = makeFrame(q, "Practice panel", 966, 310, 428, 520, colors.surface, 8, colors.border);
  makeSectionTitle(practice, "单题练习", "参考答案默认折叠，先写自己的回答。", 18, 18, 340);
  makeRect(practice, "Answer editor", 18, 92, 392, 148, "#FBFDFF", 8, colors.border);
  makeText(practice, "Answer text", "我会先判断是不是热点 key 过期导致大量请求打到数据库，然后用互斥锁限制并发重建缓存...", 32, 108, 360, 92, 13, colors.secondary, 21);
  makeButton(practice, "提交 AI 评分", 18, 260, { width: 128 });
  const evalCard = makeFrame(practice, "Evaluation result", 18, 318, 392, 150, colors.blueBg, 8, colors.blueBorder);
  makeRect(evalCard, "Grade block", 16, 20, 70, 70, colors.primary, 8);
  makeText(evalCard, "Grade score", "82", 36, 38, 30, 24, 24, "#FFFFFF", 28);
  makeText(evalCard, "Eval title", "回答方向正确", 104, 22, 220, 22, 16, colors.text, 22);
  makeText(evalCard, "Eval desc", "项目细节还可以更具体：补充锁超时、逻辑过期和旧数据取舍。", 104, 54, 250, 58, 13, colors.secondary, 20);

  const k = makeDesktopShell(pages.screens, "知识库", 1500, 1120, "个人知识库", "沉淀自己的答案、项目话术、错题总结和投递复盘。");
  const cat = makeFrame(k, "Knowledge categories", 260, 170, 220, 500, colors.surface, 8, colors.border);
  makeSectionTitle(cat, "分类", "", 18, 18, 140);
  ["全部 28", "项目话术 9", "技术笔记 7", "错题总结 6", "面试复盘 4", "HR 回答 2"].forEach((label, index) => {
    makeRect(cat, "Category row", 18, 70 + index * 48, 184, 36, index === 0 ? colors.blueBg : colors.surface, 6, index === 0 ? colors.blueBorder : colors.border);
    makeText(cat, "Category label", label, 32, 78 + index * 48, 140, 18, 14, index === 0 ? colors.primary : colors.secondary, 18);
  });

  const kList = makeFrame(k, "Knowledge list", 502, 170, 520, 500, colors.surface, 8, colors.border);
  makeSectionTitle(kList, "知识条目", "", 18, 18, 320);
  [
    ["缓存击穿的项目回答版本", "项目话术 · 高频重点 · Redis"],
    ["Docker Compose 一键部署讲法", "技术笔记 · 已掌握 · Docker"],
    ["星河云一面复盘：索引追问不完整", "面试复盘 · 需复习 · MySQL"]
  ].forEach((item, index) => {
    makeRect(kList, "Knowledge row", 18, 70 + index * 104, 484, 82, "#FBFDFF", 8, colors.border);
    makeText(kList, "Knowledge title", item[0], 34, 84 + index * 104, 430, 22, 15, colors.text, 22);
    makeText(kList, "Knowledge meta", item[1], 34, 114 + index * 104, 360, 18, 12, colors.muted, 18);
  });

  const detail = makeFrame(k, "Knowledge detail", 1044, 170, 350, 500, colors.surface, 8, colors.border);
  makeSectionTitle(detail, "条目详情", "", 18, 18, 240);
  makePill(detail, "项目话术", 20, 70, { width: 86, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
  makePill(detail, "高频重点", 116, 70, { width: 86, bg: colors.amberBg, fg: colors.amber, stroke: colors.amberBorder });
  makeText(detail, "Knowledge content", "面试回答版本\n在我的项目中，题库和作战卡结果会被缓存。对于热点 key，我会使用互斥锁控制缓存重建，并结合逻辑过期避免大量请求直接打到数据库。\n\n可能追问\n锁超时时间怎么设置？如果缓存重建失败怎么办？", 22, 122, 300, 260, 14, colors.secondary, 22);

  const app = makeDesktopShell(pages.screens, "投递复盘", 0, 2240, "投递复盘", "记录投递结果，并把失败原因转化为下一轮准备计划。");
  [
    ["准备中", "3", "待生成题目"],
    ["已投递", "4", "等待反馈"],
    ["面试中", "2", "需要复习"],
    ["已拒绝", "1", "已生成复盘"]
  ].forEach((item, index) => makeStat(app, item[0], item[1], item[2], 260 + index * 188, 170, 172));
  makeTable(app, "Applications table", 260, 312, 760, [
    ["星河云 · 后端开发工程师", "作战卡 78 分", "准备中", "更新"],
    ["北辰智能 · AI 应用开发", "作战卡 82 分", "面试中", "复盘"],
    ["云帆科技 · Java 实习生", "项目深度不足", "已拒绝", "生成复盘"]
  ]);
  const reviewPanel = makeFrame(app, "Application review AI", 1044, 312, 350, 360, colors.surface, 8, colors.border);
  makeSectionTitle(reviewPanel, "AI 复盘建议", "", 20, 20, 260);
  makeText(reviewPanel, "Review content", "失败原因\n项目讲解停留在功能层，缺少架构取舍、问题排查和性能优化细节。\n\n下一步\n用三道项目追问题补齐：缓存一致性、慢查询定位、Docker 部署排障。", 22, 80, 300, 190, 14, colors.secondary, 22);
  makeButton(reviewPanel, "保存到知识库", 22, 294, { variant: "secondary", width: 128 });

  const resume = makeDesktopShell(pages.screens, "简历", 1500, 2240, "简历", "管理多个简历版本，支持文本型 PDF、DOCX 和文本粘贴。");
  const uploadPanel = makeFrame(resume, "Resume upload choices", 260, 170, 1134, 150, colors.surface, 8, colors.border);
  [
    ["上传 PDF", "支持文本型 PDF，不支持扫描版。"],
    ["上传 DOCX", "自动解析文本并保存为简历版本。"],
    ["粘贴文本", "解析失败时的稳定兜底方式。"]
  ].forEach((item, index) => {
    const tile = makeFrame(uploadPanel, "Upload tile / " + item[0], 20 + index * 368, 22, 340, 106, "#FBFDFF", 8, colors.border);
    makeRect(tile, "Upload icon", 18, 18, 34, 34, colors.blueBg, 8, colors.blueBorder);
    makeText(tile, "Upload title", item[0], 68, 20, 160, 22, 16, colors.text, 22);
    makeText(tile, "Upload desc", item[1], 68, 52, 220, 36, 13, colors.secondary, 20);
  });
  makeTable(resume, "Resume versions table", 260, 350, 620, [
    ["Java 后端实习版", "PDF · v3 · 默认", "已解析", "编辑"],
    ["AI 应用开发版", "DOCX · v2", "已解析", "设默认"],
    ["前端 React 版", "Text · v1", "待优化", "优化"]
  ]);
  const parsePanel = makeFrame(resume, "Resume parse result", 906, 350, 488, 360, colors.surface, 8, colors.border);
  makeSectionTitle(parsePanel, "解析结果", "文件解析失败时，可直接粘贴文本继续。", 20, 20, 400);
  makeRect(parsePanel, "Resume text area", 20, 104, 448, 190, "#FBFDFF", 8, colors.border);
  makeText(parsePanel, "Resume text", "项目经历：JobPilot AI 求职备战系统\n- 使用 Spring Boot、MySQL、Redis、Qdrant 实现岗位作战卡和题库检索。\n- 基于简历与 JD 生成结构化作战卡，使用 Redis 缓存相同分析结果。", 36, 122, 400, 120, 13, colors.secondary, 21);
  makeButton(parsePanel, "保存修改", 20, 316, { width: 106 });

  const jobs = makeDesktopShell(pages.screens, "岗位", 0, 3360, "岗位", "录入目标岗位，围绕 JD 生成作战卡和专属题目。");
  const filter = makeFrame(jobs, "Job filter bar", 260, 170, 1134, 70, colors.surface, 8, colors.border);
  makePill(filter, "计算机 / 互联网", 18, 22, { width: 130, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
  makePill(filter, "Java 后端", 160, 22, { width: 90 });
  makePill(filter, "全部状态", 262, 22, { width: 90 });
  makeText(filter, "Search placeholder", "搜索公司或岗位", 390, 25, 160, 18, 13, colors.muted, 18);
  makeButton(filter, "新建岗位", 1010, 16, { width: 96 });
  makeTable(jobs, "Job list table", 260, 270, 760, [
    ["星河云 · 后端开发工程师", "Java 后端 · 上海 · 12-18K", "准备中", "生成作战卡"],
    ["北辰智能 · AI 应用开发", "LLM 应用 · 杭州 · 15-22K", "面试中", "练题"],
    ["青桐科技 · 前端开发", "React · 远程 · 10-15K", "已投递", "更新状态"]
  ]);
  const jobDetail = makeFrame(jobs, "Job detail panel", 1044, 270, 350, 420, colors.surface, 8, colors.border);
  makeSectionTitle(jobDetail, "岗位详情", "当前活跃岗位", 20, 20, 280);
  makePill(jobDetail, "计算机 / 互联网", 20, 94, { width: 130, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
  makePill(jobDetail, "Java 后端", 160, 94, { width: 90 });
  makeText(jobDetail, "JD summary", "要求熟悉 Java、Spring Boot、MySQL、Redis，具备 Docker 部署经验，能讲清项目中的接口设计、缓存策略和问题排查。", 22, 140, 300, 110, 14, colors.secondary, 22);
  makeButton(jobDetail, "生成岗位作战卡", 22, 286, { width: 150 });
  makePill(jobDetail, "已有作战卡：78 分", 22, 344, { width: 140, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });

  const settings = makeDesktopShell(pages.screens, "设置", 1500, 3360, "设置", "管理账号、默认简历、默认行业和 AI 生成偏好。");
  const account = makeFrame(settings, "Account settings", 260, 170, 520, 360, colors.surface, 8, colors.border);
  makeSectionTitle(account, "账号信息", "", 20, 20, 300);
  makeText(account, "Username label", "用户名", 24, 86, 120, 18, 13, colors.secondary, 18);
  makeRect(account, "Username field", 24, 112, 420, 38, "#FBFDFF", 6, colors.border);
  makeText(account, "Username value", "jobpilot_user", 38, 122, 180, 18, 14, colors.text, 18);
  makeText(account, "Email label", "邮箱", 24, 174, 120, 18, 13, colors.secondary, 18);
  makeRect(account, "Email field", 24, 200, 420, 38, "#FBFDFF", 6, colors.border);
  makeText(account, "Email value", "demo@jobpilot.ai", 38, 210, 220, 18, 14, colors.text, 18);
  makeButton(account, "退出登录", 24, 284, { variant: "secondary", width: 96 });
  const preference = makeFrame(settings, "Preference settings", 806, 170, 588, 360, colors.surface, 8, colors.border);
  makeSectionTitle(preference, "默认偏好", "字段保留多行业扩展能力。", 20, 20, 420);
  [["默认简历", "Java 后端实习版"], ["默认行业领域", "计算机 / 互联网"], ["默认岗位方向", "Java 后端"], ["AI 生成偏好", "题库优先，缺口再补题"]].forEach((item, index) => {
    makeText(preference, "Preference label", item[0], 24, 92 + index * 58, 130, 18, 13, colors.secondary, 18);
    makeRect(preference, "Preference field", 160, 84 + index * 58, 360, 38, "#FBFDFF", 6, colors.border);
    makeText(preference, "Preference value", item[1], 174, 94 + index * 58, 260, 18, 14, colors.text, 18);
  });
  makeButton(preference, "保存设置", 24, 306, { width: 106 });

  // AI States
  const ai = makeFrame(pages.states, "AI States / Interaction Spec", 0, 0, 1440, 1200, colors.page, 0, null);
  makeSectionTitle(ai, "AI 交互状态", "生成、缓存、失败、补题、评分和知识沉淀都需要明确状态与下一步动作。", 64, 56, 980);
  [
    ["作战卡生成中", "正在分析岗位 JD、对比简历经历、整理结构化作战卡。", colors.blueBg, colors.blueBorder],
    ["缓存命中", "相同简历、JD 和 Prompt 版本命中 Redis，避免重复调用大模型。", colors.greenBg, colors.greenBorder],
    ["公共题库不足", "展示缺口能力标签，再调用 AI 补充生成候选题。", colors.amberBg, colors.amberBorder],
    ["AI 返回格式错误", "保留原始结果，允许重试或回到岗位详情。", colors.redBg, colors.redBorder],
    ["答案评分完成", "分块展示总分、优点、问题、优化回答和追问。", colors.blueBg, colors.blueBorder],
    ["保存知识库", "允许编辑标题、分类、标签、内容和掌握程度。", colors.greenBg, colors.greenBorder]
  ].forEach((item, index) => {
    const card = makeFrame(ai, "AI state / " + item[0], 64 + (index % 3) * 430, 170 + Math.floor(index / 3) * 220, 390, 170, item[2], 8, item[3]);
    makeText(card, "State title", item[0], 20, 20, 280, 26, 18, colors.text, 26);
    makeText(card, "State desc", item[1], 20, 62, 330, 70, 14, colors.secondary, 22);
    makeButton(card, item[0].includes("错误") ? "重试" : "查看详情", 20, 120, { variant: item[0].includes("错误") ? "secondary" : "primary", width: 92 });
  });

  // Future Multi-industry
  const future = makeFrame(pages.future, "Future / Multi Industry Expansion", 0, 0, 1440, 1100, colors.page, 0, null);
  makeSectionTitle(future, "多行业扩展方向", "主流程不写死为程序员刷题系统，行业、岗位方向、能力标签和 Prompt 模板可配置。", 64, 56, 1040);
  [
    ["计算机 / 互联网", "Java、前端、AI 应用、测试、运维、数据"],
    ["产品经理", "需求分析、竞品、PRD、业务判断"],
    ["运营", "用户增长、活动策划、数据复盘"],
    ["市场 / 销售", "客户洞察、方案表达、谈判复盘"],
    ["设计", "作品集、设计方法、协作表达"],
    ["金融 / 教育 / 医药 / 制造", "行业知识、业务场景、行为面试"]
  ].forEach((item, index) => {
    const card = makeFrame(future, "Industry card / " + item[0], 64 + (index % 3) * 430, 190 + Math.floor(index / 3) * 210, 390, 160, colors.surface, 8, colors.border);
    makeText(card, "Industry title", item[0], 20, 20, 300, 26, 18, colors.text, 26);
    makeText(card, "Industry desc", item[1], 20, 64, 320, 52, 14, colors.secondary, 22);
    makePill(card, "行业题库", 20, 118, { width: 82, bg: colors.blueBg, fg: colors.primary, stroke: colors.blueBorder });
    makePill(card, "Prompt 模板", 112, 118, { width: 98, bg: colors.greenBg, fg: colors.green, stroke: colors.greenBorder });
  });

  await figma.setCurrentPageAsync(pages.cover);
  figma.viewport.scrollAndZoomIntoView([cover]);
  figma.closePlugin("JobPilot AI 原型设计稿已生成。");
})().catch((error) => {
  figma.closePlugin("生成失败：" + error.message);
});
