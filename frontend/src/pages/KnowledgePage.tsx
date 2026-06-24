import { Button, Card, Form, Input, List, message, Modal, Select, Space, Tag, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = { data: T };

type KnowledgeItem = {
  id: number;
  title: string;
  content: string;
  category: string;
  tags?: string[];
  masteryLevel: string;
};

type KnowledgeFormValues = {
  title: string;
  content: string;
  category: string;
  tagsText?: string;
  masteryLevel: string;
};

export function KnowledgePage() {
  const [items, setItems] = useState<KnowledgeItem[]>([]);
  const [editing, setEditing] = useState<KnowledgeItem | null>(null);
  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState<string | undefined>();
  const [masteryLevel, setMasteryLevel] = useState<string | undefined>();
  const [form] = Form.useForm<KnowledgeFormValues>();
  const [editForm] = Form.useForm<KnowledgeFormValues>();

  async function load() {
    const response = await apiClient.get<ApiResponse<KnowledgeItem[]>>('/knowledge', {
      params: { keyword, category, masteryLevel },
    });
    setItems(response.data.data);
  }

  useEffect(() => {
    void load();
  }, []);

  async function create(values: KnowledgeFormValues) {
    await apiClient.post('/knowledge', normalize(values));
    form.resetFields();
    message.success('知识条目已保存');
    await load();
  }

  async function update(values: KnowledgeFormValues) {
    if (!editing) return;
    await apiClient.put(`/knowledge/${editing.id}`, normalize(values));
    setEditing(null);
    await load();
  }

  async function remove(id: number) {
    await apiClient.delete(`/knowledge/${id}`);
    await load();
  }

  function normalize(values: KnowledgeFormValues) {
    return {
      ...values,
      tags: values.tagsText?.split(/[,，\s]+/).filter(Boolean) ?? [],
    };
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Knowledge</Typography.Text>
        <Typography.Title level={2}>个人知识库</Typography.Title>
      </div>

      <Card className="soft-card" title="新建知识条目">
        <KnowledgeForm form={form} onFinish={create} submitText="保存条目" />
      </Card>

      <Card className="soft-card">
        <div className="filter-row">
          <Input.Search placeholder="搜索标题或内容" allowClear onSearch={(value) => { setKeyword(value); void load(); }} />
          <Input placeholder="分类" value={category} onChange={(event) => setCategory(event.target.value || undefined)} />
          <Select
            allowClear
            placeholder="掌握程度"
            value={masteryLevel}
            onChange={setMasteryLevel}
            options={[
              { label: '需复习', value: 'needs_review' },
              { label: '已掌握', value: 'mastered' },
              { label: '高频重点', value: 'high_priority' },
            ]}
          />
          <Button onClick={() => void load()}>筛选</Button>
        </div>
      </Card>

      <Card className="soft-card" title="条目列表">
        <List
          dataSource={items}
          renderItem={(item) => (
            <List.Item
              actions={[
                <Button
                  key="edit"
                  onClick={() => {
                    setEditing(item);
                    editForm.setFieldsValue({ ...item, tagsText: item.tags?.join(' ') });
                  }}
                >
                  编辑
                </Button>,
                <Button key="delete" danger onClick={() => void remove(item.id)}>
                  删除
                </Button>,
              ]}
            >
              <List.Item.Meta
                title={
                  <Space wrap>
                    {item.title}
                    <Tag>{item.category}</Tag>
                    <Tag>{item.masteryLevel}</Tag>
                  </Space>
                }
                description={
                  <Space direction="vertical">
                    <span>{item.content.slice(0, 140)}</span>
                    <Space wrap>{item.tags?.map((tag) => <Tag key={tag}>{tag}</Tag>)}</Space>
                  </Space>
                }
              />
            </List.Item>
          )}
        />
      </Card>

      <Modal title="编辑知识条目" open={Boolean(editing)} onCancel={() => setEditing(null)} onOk={() => editForm.submit()} width={760}>
        <KnowledgeForm form={editForm} onFinish={update} submitText="保存修改" hideSubmit />
      </Modal>
    </Space>
  );
}

function KnowledgeForm({
  form,
  onFinish,
  submitText,
  hideSubmit = false,
}: {
  form: ReturnType<typeof Form.useForm<KnowledgeFormValues>>[0];
  onFinish: (values: KnowledgeFormValues) => Promise<void>;
  submitText: string;
  hideSubmit?: boolean;
}) {
  return (
    <Form form={form} layout="vertical" onFinish={onFinish} initialValues={{ masteryLevel: 'needs_review' }}>
      <div className="form-grid">
        <Form.Item label="标题" name="title" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item label="分类" name="category" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item label="标签" name="tagsText">
          <Input placeholder="Redis 缓存 项目追问" />
        </Form.Item>
        <Form.Item label="掌握程度" name="masteryLevel">
          <Select
            options={[
              { label: '需复习', value: 'needs_review' },
              { label: '已掌握', value: 'mastered' },
              { label: '高频重点', value: 'high_priority' },
            ]}
          />
        </Form.Item>
      </div>
      <Form.Item label="内容" name="content" rules={[{ required: true }]}>
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
