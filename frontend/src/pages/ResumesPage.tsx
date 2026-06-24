import { DeleteOutlined, StarOutlined, UploadOutlined } from '@ant-design/icons';
import { Button, Card, Form, Input, List, message, Modal, Space, Tag, Typography, Upload } from 'antd';
import type { UploadFile } from 'antd/es/upload/interface';
import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = {
  data: T;
};

type Resume = {
  id: number;
  title: string;
  targetRole?: string;
  content: string;
  fileType: string;
  version: number;
  isDefault: boolean;
};

type ResumeFormValues = {
  title: string;
  targetRole?: string;
  content: string;
};

export function ResumesPage() {
  const [resumes, setResumes] = useState<Resume[]>([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState<Resume | null>(null);
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [textForm] = Form.useForm<ResumeFormValues>();
  const [uploadForm] = Form.useForm<{ title: string; targetRole?: string }>();
  const [editForm] = Form.useForm<ResumeFormValues>();

  async function loadResumes() {
    setLoading(true);
    try {
      const response = await apiClient.get<ApiResponse<Resume[]>>('/resumes');
      setResumes(response.data.data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadResumes();
  }, []);

  async function createText(values: ResumeFormValues) {
    await apiClient.post('/resumes/text', values);
    textForm.resetFields();
    message.success('简历已保存');
    await loadResumes();
  }

  async function upload(values: { title: string; targetRole?: string }) {
    const file = fileList[0]?.originFileObj;
    if (!file) {
      message.warning('请选择 PDF 或 DOCX 文件');
      return;
    }
    const body = new FormData();
    body.append('title', values.title);
    if (values.targetRole) {
      body.append('targetRole', values.targetRole);
    }
    body.append('file', file);
    await apiClient.post('/resumes/upload', body);
    uploadForm.resetFields();
    setFileList([]);
    message.success('简历已解析保存');
    await loadResumes();
  }

  async function saveEdit(values: ResumeFormValues) {
    if (!editing) {
      return;
    }
    await apiClient.put(`/resumes/${editing.id}`, values);
    setEditing(null);
    message.success('简历已更新');
    await loadResumes();
  }

  async function setDefault(id: number) {
    await apiClient.put(`/resumes/${id}/default`);
    await loadResumes();
  }

  async function remove(id: number) {
    await apiClient.delete(`/resumes/${id}`);
    message.success('简历已删除');
    await loadResumes();
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Resumes</Typography.Text>
        <Typography.Title level={2}>简历管理</Typography.Title>
      </div>

      <div className="two-column">
        <Card className="soft-card" title="粘贴文本简历">
          <Form form={textForm} layout="vertical" onFinish={createText}>
            <Form.Item label="标题" name="title" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item label="目标岗位" name="targetRole">
              <Input />
            </Form.Item>
            <Form.Item label="简历内容" name="content" rules={[{ required: true }]}>
              <Input.TextArea rows={8} />
            </Form.Item>
            <Button type="primary" htmlType="submit">
              保存文本简历
            </Button>
          </Form>
        </Card>

        <Card className="soft-card" title="上传 PDF / DOCX">
          <Form form={uploadForm} layout="vertical" onFinish={upload}>
            <Form.Item label="标题" name="title" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item label="目标岗位" name="targetRole">
              <Input />
            </Form.Item>
            <Upload
              beforeUpload={() => false}
              fileList={fileList}
              maxCount={1}
              accept=".pdf,.docx"
              onChange={({ fileList: next }) => setFileList(next)}
            >
              <Button icon={<UploadOutlined />}>选择文件</Button>
            </Upload>
            <Button className="form-submit" type="primary" htmlType="submit">
              解析并保存
            </Button>
          </Form>
        </Card>
      </div>

      <Card className="soft-card" title="简历版本">
        <List
          loading={loading}
          dataSource={resumes}
          renderItem={(resume) => (
            <List.Item
              actions={[
                <Button key="default" icon={<StarOutlined />} onClick={() => void setDefault(resume.id)}>
                  设为默认
                </Button>,
                <Button
                  key="edit"
                  onClick={() => {
                    setEditing(resume);
                    editForm.setFieldsValue(resume);
                  }}
                >
                  编辑
                </Button>,
                <Button key="delete" danger icon={<DeleteOutlined />} onClick={() => void remove(resume.id)} />,
              ]}
            >
              <List.Item.Meta
                title={
                  <Space>
                    {resume.title}
                    {resume.isDefault && <Tag color="blue">默认</Tag>}
                    <Tag>{resume.fileType}</Tag>
                    <Tag>v{resume.version}</Tag>
                  </Space>
                }
                description={`${resume.targetRole ?? '未设置目标岗位'} · ${resume.content.slice(0, 90)}`}
              />
            </List.Item>
          )}
        />
      </Card>

      <Modal
        title="编辑简历"
        open={Boolean(editing)}
        onCancel={() => setEditing(null)}
        onOk={() => editForm.submit()}
        width={760}
      >
        <Form form={editForm} layout="vertical" onFinish={saveEdit}>
          <Form.Item label="标题" name="title" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label="目标岗位" name="targetRole">
            <Input />
          </Form.Item>
          <Form.Item label="简历内容" name="content" rules={[{ required: true }]}>
            <Input.TextArea rows={12} />
          </Form.Item>
        </Form>
      </Modal>
    </Space>
  );
}
