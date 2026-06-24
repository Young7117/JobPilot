import { Button, Card, Form, Input, message, Typography } from 'antd';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

type LoginFormValues = {
  account: string;
  password: string;
};

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname ?? '/dashboard';

  async function handleFinish(values: LoginFormValues) {
    try {
      await login(values.account, values.password);
      navigate(from, { replace: true });
    } catch {
      message.error('登录失败，请检查账号和密码');
    }
  }

  return (
    <main className="auth-page">
      <Card className="auth-card">
        <Typography.Title level={2}>登录 JobPilot AI</Typography.Title>
        <Form layout="vertical" onFinish={handleFinish}>
          <Form.Item label="用户名或邮箱" name="account" rules={[{ required: true }]}>
            <Input size="large" />
          </Form.Item>
          <Form.Item label="密码" name="password" rules={[{ required: true }]}>
            <Input.Password size="large" />
          </Form.Item>
          <Button type="primary" size="large" htmlType="submit" block>
            登录
          </Button>
        </Form>
        <Typography.Paragraph className="auth-switch">
          还没有账号？<Link to="/register">注册</Link>
        </Typography.Paragraph>
      </Card>
    </main>
  );
}
