import { Button, Card, Form, Input, message, Typography } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

type RegisterFormValues = {
  username: string;
  email: string;
  password: string;
};

export function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  async function handleFinish(values: RegisterFormValues) {
    try {
      await register(values.username, values.email, values.password);
      navigate('/dashboard', { replace: true });
    } catch {
      message.error('注册失败，用户名或邮箱可能已存在');
    }
  }

  return (
    <main className="auth-page">
      <Card className="auth-card">
        <Typography.Title level={2}>创建 JobPilot AI 账号</Typography.Title>
        <Form layout="vertical" onFinish={handleFinish}>
          <Form.Item label="用户名" name="username" rules={[{ required: true }]}>
            <Input size="large" />
          </Form.Item>
          <Form.Item label="邮箱" name="email" rules={[{ required: true, type: 'email' }]}>
            <Input size="large" />
          </Form.Item>
          <Form.Item label="密码" name="password" rules={[{ required: true, min: 8 }]}>
            <Input.Password size="large" />
          </Form.Item>
          <Button type="primary" size="large" htmlType="submit" block>
            注册
          </Button>
        </Form>
        <Typography.Paragraph className="auth-switch">
          已有账号？<Link to="/login">登录</Link>
        </Typography.Paragraph>
      </Card>
    </main>
  );
}
