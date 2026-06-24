import { Button, Card, Form, Input, List, message, Select, Space, Tag, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiClient } from '../api/client';

type ApiResponse<T> = { data: T };

type Question = {
  userQuestionId: number;
  title: string;
  content: string;
  referenceAnswer: string;
  questionType: string;
  difficulty: string;
  tags: string[];
  status: string;
};

type Practice = {
  id: number;
  userAnswer: string;
  aiScore?: number;
  aiFeedback?: string;
  aiOptimizedAnswer?: string;
  aiFollowUp?: string[];
};

export function QuestionPracticePage() {
  const { id } = useParams();
  const [question, setQuestion] = useState<Question | null>(null);
  const [practices, setPractices] = useState<Practice[]>([]);
  const [latest, setLatest] = useState<Practice | null>(null);
  const [form] = Form.useForm<{ userAnswer: string }>();

  async function load() {
    const [questionResponse, practiceResponse] = await Promise.all([
      apiClient.get<ApiResponse<Question>>(`/questions/${id}`),
      apiClient.get<ApiResponse<Practice[]>>(`/questions/${id}/practices`),
    ]);
    setQuestion(questionResponse.data.data);
    setPractices(practiceResponse.data.data);
    setLatest(practiceResponse.data.data[0] ?? null);
  }

  useEffect(() => {
    void load();
  }, [id]);

  async function evaluate(values: { userAnswer: string }) {
    const response = await apiClient.post<ApiResponse<Practice>>(`/questions/${id}/evaluate`, values);
    setLatest(response.data.data);
    form.resetFields();
    message.success('AI 评分完成');
    await load();
  }

  async function updateStatus(status: string) {
    await apiClient.put(`/questions/${id}/status`, { status });
    await load();
  }

  async function saveToKnowledge() {
    await apiClient.post(`/questions/${id}/save-to-knowledge`);
    message.success('已加入个人知识库');
  }

  if (!question) {
    return null;
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <Card className="soft-card">
        <Space direction="vertical">
          <Typography.Title level={2}>{question.title}</Typography.Title>
          <Space wrap>
            <Tag>{question.questionType}</Tag>
            <Tag>{question.difficulty}</Tag>
            {question.tags?.map((tag) => <Tag key={tag}>{tag}</Tag>)}
          </Space>
          <Typography.Paragraph>{question.content}</Typography.Paragraph>
          <Select
            value={question.status}
            onChange={updateStatus}
            style={{ width: 180 }}
            options={[
              { label: '未练习', value: 'not_practiced' },
              { label: '需复习', value: 'needs_review' },
              { label: '已掌握', value: 'mastered' },
              { label: '高频重点', value: 'high_priority' },
            ]}
          />
        </Space>
      </Card>

      <Card className="soft-card" title="提交回答">
        <Form form={form} layout="vertical" onFinish={evaluate}>
          <Form.Item name="userAnswer" rules={[{ required: true }]}>
            <Input.TextArea rows={8} placeholder="写下你的面试回答..." />
          </Form.Item>
          <Button type="primary" htmlType="submit">
            AI 评分
          </Button>
        </Form>
      </Card>

      {latest && (
        <Card className="soft-card" title="AI 反馈">
          <Typography.Title level={3}>{latest.aiScore ?? '-'} 分</Typography.Title>
          <Typography.Paragraph>{latest.aiFeedback}</Typography.Paragraph>
          <Typography.Title level={4}>优化回答</Typography.Title>
          <Typography.Paragraph>{latest.aiOptimizedAnswer}</Typography.Paragraph>
          <Typography.Title level={4}>可能追问</Typography.Title>
          <List size="small" dataSource={latest.aiFollowUp ?? []} renderItem={(item) => <List.Item>{item}</List.Item>} />
          <Button className="form-submit" onClick={() => void saveToKnowledge()}>
            加入个人知识库
          </Button>
        </Card>
      )}

      <Card className="soft-card" title="练习历史">
        <List
          dataSource={practices}
          renderItem={(practice) => (
            <List.Item>
              <List.Item.Meta title={`${practice.aiScore ?? '-'} 分`} description={practice.userAnswer} />
            </List.Item>
          )}
        />
      </Card>
    </Space>
  );
}
