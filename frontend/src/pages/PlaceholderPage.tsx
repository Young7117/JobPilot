import { Card, Typography } from 'antd';

type PlaceholderPageProps = {
  title: string;
};

export function PlaceholderPage({ title }: PlaceholderPageProps) {
  return (
    <Card className="soft-card">
      <Typography.Text type="secondary">JobPilot AI</Typography.Text>
      <Typography.Title level={2}>{title}</Typography.Title>
      <Typography.Paragraph>
        该页面已进入路由骨架，后续任务会接入真实数据、表单和操作流程。
      </Typography.Paragraph>
    </Card>
  );
}
