CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(128) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_users_username (username),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS resume (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  target_role VARCHAR(128),
  content MEDIUMTEXT NOT NULL,
  file_type VARCHAR(32) NOT NULL,
  file_url VARCHAR(512),
  version INT NOT NULL DEFAULT 1,
  is_default TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_resume_user_id (user_id),
  CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS job_post (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  company_name VARCHAR(128) NOT NULL,
  position_name VARCHAR(128) NOT NULL,
  industry VARCHAR(96) NOT NULL DEFAULT '计算机/互联网',
  city VARCHAR(96),
  salary_range VARCHAR(96),
  job_direction VARCHAR(96),
  jd_content MEDIUMTEXT NOT NULL,
  source VARCHAR(128),
  status VARCHAR(32) NOT NULL DEFAULT 'preparing',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_job_post_user_id (user_id),
  KEY idx_job_post_direction (job_direction),
  CONSTRAINT fk_job_post_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS battle_card (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  resume_id BIGINT NOT NULL,
  job_post_id BIGINT NOT NULL,
  match_score INT NOT NULL DEFAULT 0,
  core_requirements JSON,
  skill_breakdown JSON,
  matched_points JSON,
  weak_points JSON,
  resume_suggestions JSON,
  interview_focus JSON,
  three_day_plan JSON,
  seven_day_plan JSON,
  risk_tips JSON,
  raw_ai_result JSON,
  request_hash VARCHAR(128),
  prompt_version VARCHAR(64),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_battle_card_user_id (user_id),
  KEY idx_battle_card_job_post_id (job_post_id),
  CONSTRAINT fk_battle_card_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_battle_card_resume FOREIGN KEY (resume_id) REFERENCES resume(id),
  CONSTRAINT fk_battle_card_job_post FOREIGN KEY (job_post_id) REFERENCES job_post(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS question_bank (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  question_type VARCHAR(64) NOT NULL,
  difficulty VARCHAR(32) NOT NULL,
  tags JSON,
  reference_answer MEDIUMTEXT,
  quality_score DECIMAL(5,2) NOT NULL DEFAULT 0,
  usage_count INT NOT NULL DEFAULT 0,
  source VARCHAR(64) NOT NULL DEFAULT 'seed',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_question_bank_type (question_type),
  KEY idx_question_bank_quality (quality_score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS candidate_question (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  question_type VARCHAR(64) NOT NULL,
  difficulty VARCHAR(32) NOT NULL,
  tags JSON,
  reference_answer MEDIUMTEXT,
  quality_score DECIMAL(5,2) NOT NULL DEFAULT 0,
  duplicate_score DECIMAL(5,2) NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL DEFAULT 'pending',
  source VARCHAR(64) NOT NULL DEFAULT 'ai',
  raw_ai_result JSON,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_candidate_question_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS user_question (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  question_id BIGINT,
  candidate_question_id BIGINT,
  job_post_id BIGINT NOT NULL,
  battle_card_id BIGINT NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'not_practiced',
  source_type VARCHAR(32) NOT NULL DEFAULT 'public',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_user_question_user_id (user_id),
  KEY idx_user_question_job_post_id (job_post_id),
  CONSTRAINT fk_user_question_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_question_question FOREIGN KEY (question_id) REFERENCES question_bank(id),
  CONSTRAINT fk_user_question_candidate FOREIGN KEY (candidate_question_id) REFERENCES candidate_question(id),
  CONSTRAINT fk_user_question_job_post FOREIGN KEY (job_post_id) REFERENCES job_post(id),
  CONSTRAINT fk_user_question_battle_card FOREIGN KEY (battle_card_id) REFERENCES battle_card(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS question_practice (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  user_question_id BIGINT NOT NULL,
  user_answer MEDIUMTEXT NOT NULL,
  ai_score INT,
  ai_feedback MEDIUMTEXT,
  ai_optimized_answer MEDIUMTEXT,
  ai_follow_up JSON,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_question_practice_user_question_id (user_question_id),
  CONSTRAINT fk_question_practice_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_question_practice_user_question FOREIGN KEY (user_question_id) REFERENCES user_question(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS personal_knowledge_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  content MEDIUMTEXT NOT NULL,
  category VARCHAR(64) NOT NULL,
  tags JSON,
  source_type VARCHAR(64),
  source_id BIGINT,
  source_question_id BIGINT,
  mastery_level VARCHAR(32) NOT NULL DEFAULT 'needs_review',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_personal_knowledge_user_id (user_id),
  KEY idx_personal_knowledge_category (category),
  CONSTRAINT fk_personal_knowledge_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS application_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  job_post_id BIGINT NOT NULL,
  resume_id BIGINT,
  battle_card_id BIGINT,
  status VARCHAR(32) NOT NULL DEFAULT 'planned',
  apply_date DATE,
  interview_date DATETIME,
  result VARCHAR(64),
  failure_reason VARCHAR(512),
  note MEDIUMTEXT,
  ai_review_suggestions JSON,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_application_record_user_id (user_id),
  KEY idx_application_record_status (status),
  CONSTRAINT fk_application_record_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_application_record_job_post FOREIGN KEY (job_post_id) REFERENCES job_post(id),
  CONSTRAINT fk_application_record_resume FOREIGN KEY (resume_id) REFERENCES resume(id),
  CONSTRAINT fk_application_record_battle_card FOREIGN KEY (battle_card_id) REFERENCES battle_card(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS ai_call_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  scene VARCHAR(64) NOT NULL,
  request_hash VARCHAR(128),
  prompt_tokens INT,
  completion_tokens INT,
  model_name VARCHAR(128),
  cost DECIMAL(12,6),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_ai_call_log_user_id (user_id),
  KEY idx_ai_call_log_scene (scene),
  CONSTRAINT fk_ai_call_log_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
