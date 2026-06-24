INSERT INTO question_bank
  (title, content, question_type, difficulty, tags, reference_answer, quality_score, usage_count, source)
VALUES
  ('HashMap 和 ConcurrentHashMap 有什么区别？', '请说明 HashMap 与 ConcurrentHashMap 的线程安全、锁粒度和使用场景差异。', 'Java 八股题', '中等', JSON_ARRAY('Java', '集合', '并发'), 'HashMap 不是线程安全的；ConcurrentHashMap 通过分段/桶级并发控制提升并发读写安全性。回答时结合 JDK8 的 CAS、synchronized 和业务使用场景。', 92, 0, 'seed'),
  ('Spring Boot 自动配置原理是什么？', '请解释 Spring Boot 自动配置如何工作，以及如何排查某个 Bean 为什么生效或未生效。', 'Spring Boot 题', '中等', JSON_ARRAY('Spring Boot', '自动配置'), '核心围绕 @SpringBootApplication、@EnableAutoConfiguration、AutoConfiguration.imports 和条件注解。排查可看 condition evaluation report。', 93, 0, 'seed'),
  ('MySQL 索引失效有哪些常见原因？', '结合项目说明 MySQL 索引可能失效的场景和排查方式。', 'MySQL 专项题', '中等', JSON_ARRAY('MySQL', '索引', '性能优化'), '常见原因包括函数包裹、隐式类型转换、前置模糊匹配、联合索引不满足最左前缀、低选择性字段等。用 explain 验证。', 94, 0, 'seed'),
  ('如何解决 Redis 缓存击穿？', '结合你的项目说明 Redis 缓存击穿如何产生，以及如何解决。', 'Redis 专项题', '中等', JSON_ARRAY('Redis', '缓存击穿', '项目追问'), '缓存击穿通常是热点 key 过期后大量请求打到数据库。可用互斥锁、逻辑过期、热点 key 预热和过期时间抖动。', 95, 0, 'seed'),
  ('Docker 部署 Spring Boot 项目要注意什么？', '请说明从打包、镜像构建、环境变量到日志排查的完整部署思路。', 'Docker 部署题', '基础', JSON_ARRAY('Docker', '部署', 'Spring Boot'), '回答应覆盖多阶段构建、端口映射、环境变量、网络服务名、日志、健康检查和数据卷边界。', 90, 0, 'seed'),
  ('React 中 useEffect 常见坑有哪些？', '说明 useEffect 的依赖数组、清理函数和请求竞态问题。', 'React 前端题', '中等', JSON_ARRAY('React', 'useEffect', '前端'), '关注依赖完整性、清理订阅/计时器、避免闭包旧值、处理异步请求竞态。', 91, 0, 'seed'),
  ('如何设计一个岗位题库优先检索再 AI 补题的系统？', '从召回、排序、去重、候选审核和成本控制角度说明。', 'AI 应用题', '困难', JSON_ARRAY('AI 应用', 'Embedding', 'Qdrant', '题库'), '先根据作战卡提取标签和短板，用向量库召回公共题，结合质量分排序；不足时 AI 补题，进入候选库并做去重和质量评分。', 96, 0, 'seed'),
  ('请介绍你最有代表性的一个项目。', '面试官希望听到项目背景、职责、技术方案、难点、结果和复盘。', '项目追问题', '基础', JSON_ARRAY('项目表达', 'STAR', '项目追问'), '建议用背景-目标-方案-难点-结果-复盘结构，突出个人贡献和量化结果。', 94, 0, 'seed'),
  ('如何设计一个限流系统？', '请从接口限流目标、算法、Redis 实现和降级策略说明。', '场景设计题', '困难', JSON_ARRAY('系统设计', 'Redis', '限流'), '可讲固定窗口、滑动窗口、令牌桶、漏桶；Redis 可用 Lua 保证原子性，同时要考虑降级和观测。', 93, 0, 'seed'),
  ('你为什么投递这个岗位？', '请结合岗位 JD、个人经历和职业规划回答。', 'HR 行为题', '基础', JSON_ARRAY('HR', '求职动机'), '回答要避免泛泛而谈，连接公司业务、岗位要求、已有经历和短期成长目标。', 90, 0, 'seed');
