import {
  BookOutlined,
  DashboardOutlined,
  FileTextOutlined,
  QuestionCircleOutlined,
  ReadOutlined,
  SendOutlined,
  SettingOutlined,
  SnippetsOutlined,
  LogoutOutlined,
} from '@ant-design/icons';
import { Button, Layout, Menu, Space, Typography } from 'antd';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const navItems = [
  { key: '/dashboard', icon: <DashboardOutlined />, label: '工作台' },
  { key: '/resumes', icon: <FileTextOutlined />, label: '简历' },
  { key: '/jobs', icon: <SnippetsOutlined />, label: '岗位' },
  { key: '/questions', icon: <QuestionCircleOutlined />, label: '题库练习' },
  { key: '/knowledge', icon: <BookOutlined />, label: '知识库' },
  { key: '/applications', icon: <SendOutlined />, label: '投递复盘' },
  { key: '/settings', icon: <SettingOutlined />, label: '设置' },
];

export function AppLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  return (
    <Layout className="app-shell">
      <Layout.Sider width={240} className="app-sider">
        <div className="brand">
          <ReadOutlined />
          <div>
            <Typography.Text strong>JobPilot AI</Typography.Text>
            <Typography.Text type="secondary">岗位作战与面试知识库</Typography.Text>
          </div>
        </div>
        <Menu
          mode="inline"
          selectedKeys={[navItems.find((item) => location.pathname.startsWith(item.key))?.key ?? '/dashboard']}
          items={navItems}
          onClick={({ key }) => navigate(key)}
        />
      </Layout.Sider>
      <Layout className="app-main">
        <Layout.Header className="app-header">
          <Typography.Text type="secondary">{user?.email}</Typography.Text>
          <Space>
            <Button icon={<LogoutOutlined />} onClick={() => void logout()}>
              退出
            </Button>
          </Space>
        </Layout.Header>
        <Layout.Content className="app-content">
          <Outlet />
        </Layout.Content>
      </Layout>
    </Layout>
  );
}
