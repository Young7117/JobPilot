import { Button, Card, Col, Row, Space, Statistic, Typography } from 'antd';
import { ArrowRightOutlined } from '@ant-design/icons';
import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';

type ApiResponse<T> = { data: T };

type DashboardSummary = {
  resumeCount: number;
  jobCount: number;
  battleCardCount: number;
  questionCount: number;
  reviewQueueCount: number;
  applicationStatusStats: Record<string, number>;
};

export function DashboardPage() {
  const [summary, setSummary] = useState<DashboardSummary>({
    resumeCount: 0,
    jobCount: 0,
    battleCardCount: 0,
    questionCount: 0,
    reviewQueueCount: 0,
    applicationStatusStats: {},
  });

  useEffect(() => {
    apiClient.get<ApiResponse<DashboardSummary>>('/dashboard').then((response) => setSummary(response.data.data));
  }, []);

  const stats = [
    ['简历', summary.resumeCount],
    ['岗位', summary.jobCount],
    ['作战卡', summary.battleCardCount],
    ['题目', summary.questionCount],
    ['待复习', summary.reviewQueueCount],
  ];

  return (
    <Space direction="vertical" size={24} className="page-stack">
      <div>
        <Typography.Text type="secondary">Dashboard</Typography.Text>
        <Typography.Title level={2}>围绕目标岗位，组织每一次准备</Typography.Title>
      </div>

      <Row gutter={[16, 16]}>
        {stats.map(([label, value]) => (
          <Col xs={12} md={stats.length === 5 ? 4 : 6} key={label}>
            <Card className="soft-card">
              <Statistic title={label} value={value} />
            </Card>
          </Col>
        ))}
      </Row>

      <Card className="soft-card action-card">
        <div>
          <Typography.Title level={4}>下一步：录入第一份简历和目标岗位</Typography.Title>
          <Typography.Paragraph>
            完成简历和 JD 后，系统会生成岗位作战卡，并优先从公共题库召回练习题。
          </Typography.Paragraph>
        </div>
        <Button type="primary" icon={<ArrowRightOutlined />}>
          开始准备
        </Button>
      </Card>

      <Card className="soft-card" title="投递状态">
        <Space wrap>
          {Object.entries(summary.applicationStatusStats).map(([status, count]) => (
            <Statistic key={status} title={status} value={count} />
          ))}
        </Space>
      </Card>
    </Space>
  );
}
