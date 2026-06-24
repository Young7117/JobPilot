import { DeleteOutlined } from '@ant-design/icons';
import { Button, Card, Form, Input, List, message, Modal, Select, Space, Tag, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = {
  data: T;
};

type JobPost = {
  id: number;
  companyName: string;
  positionName: string;
  industry: string;
  city?: string;
  salaryRange?: string;
  jobDirection?: string;
  jdContent: string;
  source?: string;
  status: string;
};

type JobFormValues = Omit<JobPost, 'id'>;

const directionOptions = ['Java 后端', '前端开发', 'AI 应用开发', '测试开发', '运维开发', '数据开发'];

export function JobsPage() {
  const [jobs, setJobs] = useState<JobPost[]>([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState<JobPost | null>(null);
  const [direction, setDirection] = useState<string | undefined>();
  const [form] = Form.useForm<JobFormValues>();
  const [editForm] = Form.useForm<JobFormValues>();

  async function loadJobs(nextDirection = direction) {
    setLoading(true);
    try {
      const response = await apiClient.get<ApiResponse<JobPost[]>>('/jobs', {
        params: nextDirection ? { jobDirection: nextDirection } : undefined,
      });
      setJobs(response.data.data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadJobs();
  }, []);

  async function create(values: JobFormValues) {
    await apiClient.post('/jobs', values);
    form.resetFields();
    message.success('岗位已保存');
    await loadJobs();
  }

  async function saveEdit(values: JobFormValues) {
    if (!editing) {
      return;
    }
    await apiClient.put(`/jobs/${editing.id}`, values);
    setEditing(null);
    message.success('岗位已更新');
    await loadJobs();
  }

  async function remove(id: number) {
    await apiClient.delete(`/jobs/${id}`);
    message.success('岗位已删除');
    await loadJobs();
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Jobs</Typography.Text>
        <Typography.Title level={2}>岗位 JD 管理</Typography.Title>
      </div>

      <Card className="soft-card" title="录入岗位">
        <JobForm form={form} onFinish={create} submitText="保存岗位" />
      </Card>

      <Card
        className="soft-card"
        title="岗位列表"
        extra={
          <Select
            allowClear
            placeholder="按方向筛选"
            value={direction}
            options={directionOptions.map((item) => ({ label: item, value: item }))}
            onChange={(value) => {
              setDirection(value);
              void loadJobs(value);
            }}
            style={{ width: 180 }}
          />
        }
      >
        <List
          loading={loading}
          dataSource={jobs}
          renderItem={(job) => (
            <List.Item
              actions={[
                <Button
                  key="edit"
                  onClick={() => {
                    setEditing(job);
                    editForm.setFieldsValue(job);
                  }}
                >
                  编辑
                </Button>,
                <Button key="delete" danger icon={<DeleteOutlined />} onClick={() => void remove(job.id)} />,
              ]}
            >
              <List.Item.Meta
                title={
                  <Space wrap>
                    {job.companyName} · {job.positionName}
                    <Tag color="blue">{job.status}</Tag>
                    {job.jobDirection && <Tag>{job.jobDirection}</Tag>}
                  </Space>
                }
                description={`${job.city ?? '城市未填'} · ${job.salaryRange ?? '薪资未填'} · ${job.jdContent.slice(0, 100)}`}
              />
            </List.Item>
          )}
        />
      </Card>

      <Modal
        title="编辑岗位"
        open={Boolean(editing)}
        onCancel={() => setEditing(null)}
        onOk={() => editForm.submit()}
        width={860}
      >
        <JobForm form={editForm} onFinish={saveEdit} submitText="保存修改" hideSubmit />
      </Modal>
    </Space>
  );
}

function JobForm({
  form,
  onFinish,
  submitText,
  hideSubmit = false,
}: {
  form: ReturnType<typeof Form.useForm<JobFormValues>>[0];
  onFinish: (values: JobFormValues) => Promise<void>;
  submitText: string;
  hideSubmit?: boolean;
}) {
  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={onFinish}
      initialValues={{ industry: '计算机/互联网', status: 'preparing' }}
    >
      <div className="form-grid">
        <Form.Item label="公司名" name="companyName" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item label="岗位名" name="positionName" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item label="行业" name="industry">
          <Input />
        </Form.Item>
        <Form.Item label="岗位方向" name="jobDirection">
          <Select allowClear options={directionOptions.map((item) => ({ label: item, value: item }))} />
        </Form.Item>
        <Form.Item label="城市" name="city">
          <Input />
        </Form.Item>
        <Form.Item label="薪资" name="salaryRange">
          <Input />
        </Form.Item>
        <Form.Item label="来源" name="source">
          <Input />
        </Form.Item>
        <Form.Item label="状态" name="status">
          <Select
            options={[
              { label: '准备中', value: 'preparing' },
              { label: '已投递', value: 'applied' },
              { label: '面试中', value: 'interviewing' },
              { label: '已结束', value: 'closed' },
            ]}
          />
        </Form.Item>
      </div>
      <Form.Item label="岗位 JD" name="jdContent" rules={[{ required: true }]}>
        <Input.TextArea rows={8} />
      </Form.Item>
      {!hideSubmit && (
        <Button type="primary" htmlType="submit">
          {submitText}
        </Button>
      )}
    </Form>
  );
}
