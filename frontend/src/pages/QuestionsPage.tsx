import { Card, Input, List, Select, Space, Tag, Typography } from 'antd';
import { useEffect, useMemo, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = {
  data: T;
};

type Question = {
  userQuestionId: number;
  questionId: number;
  title: string;
  content: string;
  questionType: string;
  difficulty: string;
  tags: string[];
  referenceAnswer: string;
  status: string;
  sourceType: string;
};

export function QuestionsPage() {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [keyword, setKeyword] = useState('');
  const [type, setType] = useState<string | undefined>();
  const [difficulty, setDifficulty] = useState<string | undefined>();
  const [status, setStatus] = useState<string | undefined>();
  const [tag, setTag] = useState<string | undefined>();

  useEffect(() => {
    apiClient.get<ApiResponse<Question[]>>('/questions').then((response) => setQuestions(response.data.data));
  }, []);

  const allTags = useMemo(
    () => Array.from(new Set(questions.flatMap((question) => question.tags ?? []))),
    [questions],
  );

  const filtered = useMemo(
    () =>
      questions.filter((question) => {
        const textMatched =
          !keyword ||
          question.title.includes(keyword) ||
          question.content.includes(keyword) ||
          question.tags?.some((item) => item.includes(keyword));
        return (
          textMatched &&
          (!type || question.questionType === type) &&
          (!difficulty || question.difficulty === difficulty) &&
          (!status || question.status === status) &&
          (!tag || question.tags?.includes(tag))
        );
      }),
    [difficulty, keyword, questions, status, tag, type],
  );

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Questions</Typography.Text>
        <Typography.Title level={2}>题库练习</Typography.Title>
      </div>

      <Card className="soft-card">
        <div className="filter-row">
          <Input.Search placeholder="搜索题目或标签" allowClear onSearch={setKeyword} onChange={(event) => setKeyword(event.target.value)} />
          <Select
            allowClear
            placeholder="题目类型"
            value={type}
            onChange={setType}
            options={Array.from(new Set(questions.map((item) => item.questionType))).map((item) => ({ label: item, value: item }))}
          />
          <Select
            allowClear
            placeholder="难度"
            value={difficulty}
            onChange={setDifficulty}
            options={Array.from(new Set(questions.map((item) => item.difficulty))).map((item) => ({ label: item, value: item }))}
          />
          <Select
            allowClear
            placeholder="掌握状态"
            value={status}
            onChange={setStatus}
            options={[
              { label: '未练习', value: 'not_practiced' },
              { label: '需复习', value: 'needs_review' },
              { label: '已掌握', value: 'mastered' },
              { label: '高频重点', value: 'high_priority' },
            ]}
          />
          <Select
            allowClear
            placeholder="技术标签"
            value={tag}
            onChange={setTag}
            options={allTags.map((item) => ({ label: item, value: item }))}
          />
        </div>
      </Card>

      <Card className="soft-card">
        <List
          dataSource={filtered}
          renderItem={(question) => (
            <List.Item>
              <List.Item.Meta
                title={
                  <Space wrap>
                    {question.title}
                    <Tag color={question.sourceType === 'public' ? 'blue' : 'purple'}>{question.sourceType}</Tag>
                    <Tag>{question.questionType}</Tag>
                    <Tag>{question.difficulty}</Tag>
                  </Space>
                }
                description={
                  <Space direction="vertical">
                    <span>{question.content}</span>
                    <Space wrap>{question.tags?.map((item) => <Tag key={item}>{item}</Tag>)}</Space>
                  </Space>
                }
              />
            </List.Item>
          )}
        />
      </Card>
    </Space>
  );
}
