import { Button, Card, DatePicker, Form, Input, InputNumber, List, message, Select, Space, Tag, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = { data: T };

type ApplicationRecord = {
  id: number;
  jobPostId: number;
  resumeId?: number;
  battleCardId?: number;
  status: string;
  result?: string;
  failureReason?: string;
  note?: string;
  aiReviewSuggestions?: string[];
};

export function ApplicationsPage() {
  const [records, setRecords] = useState<ApplicationRecord[]>([]);
  const [form] = Form.useForm();

  async function load() {
    const response = await apiClient.get<ApiResponse<ApplicationRecord[]>>('/applications');
    setRecords(response.data.data);
  }

  useEffect(() => {
    void load();
  }, []);

  async function create(values: any) {
    await apiClient.post('/applications', {
      ...values,
      applyDate: values.applyDate?.format('YYYY-MM-DD'),
      interviewDate: values.interviewDate?.toISOString(),
    });
    form.resetFields();
    message.success('投递记录已保存');
    await load();
  }

  async function review(id: number) {
    await apiClient.post(`/applications/${id}/review`);
    message.success('AI 复盘已生成');
    await load();
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Applications</Typography.Text>
        <Typography.Title level={2}>投递复盘</Typography.Title>
      </div>

      <Card className="soft-card" title="新增投递记录">
        <Form form={form} layout="vertical" onFinish={create} initialValues={{ status: 'planned' }}>
          <div className="form-grid">
            <Form.Item label="岗位 ID" name="jobPostId" rules={[{ required: true }]}>
              <InputNumber min={1} style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item label="简历 ID" name="resumeId">
              <InputNumber min={1} style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item label="作战卡 ID" name="battleCardId">
              <InputNumber min={1} style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item label="状态" name="status">
              <Select
                options={[
                  { label: '计划投递', value: 'planned' },
                  { label: '已投递', value: 'applied' },
                  { label: '面试中', value: 'interviewing' },
                  { label: '已结束', value: 'closed' },
                ]}
              />
            </Form.Item>
            <Form.Item label="投递日期" name="applyDate">
              <DatePicker style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item label="结果" name="result">
              <Input />
            </Form.Item>
          </div>
          <Form.Item label="失败原因 / 复盘线索" name="failureReason">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item label="备注" name="note">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Button type="primary" htmlType="submit">
            保存记录
          </Button>
        </Form>
      </Card>

      <Card className="soft-card" title="记录列表">
        <List
          dataSource={records}
          renderItem={(record) => (
            <List.Item actions={[<Button onClick={() => void review(record.id)}>AI 复盘</Button>]}>
              <List.Item.Meta
                title={
                  <Space>
                    投递 #{record.id}
                    <Tag>{record.status}</Tag>
                    {record.result && <Tag color="blue">{record.result}</Tag>}
                  </Space>
                }
                description={
                  <Space direction="vertical">
                    <span>岗位 ID：{record.jobPostId}</span>
                    <span>{record.failureReason || record.note}</span>
                    {record.aiReviewSuggestions?.map((item) => <Tag key={item}>{item}</Tag>)}
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
