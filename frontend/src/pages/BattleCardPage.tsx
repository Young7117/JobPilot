import { Card, Col, List, Progress, Row, Space, Spin, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiClient } from '../api/client';

type ApiResponse<T> = {
  data: T;
};

type BattleCard = {
  id: number;
  matchScore: number;
  coreRequirements: string[];
  skillBreakdown: string[];
  matchedPoints: string[];
  weakPoints: string[];
  resumeSuggestions: string[];
  interviewFocus: string[];
  threeDayPlan: string[];
  sevenDayPlan: string[];
  riskTips: string[];
};

const sections: Array<[keyof BattleCard, string]> = [
  ['coreRequirements', '岗位核心要求'],
  ['skillBreakdown', '技术栈拆解'],
  ['matchedPoints', '已有优势'],
  ['weakPoints', '明显短板'],
  ['resumeSuggestions', '简历优化建议'],
  ['interviewFocus', '面试重点'],
  ['threeDayPlan', '三天补强计划'],
  ['sevenDayPlan', '七天补强计划'],
  ['riskTips', '投递风险提醒'],
];

export function BattleCardPage() {
  const { id } = useParams();
  const [card, setCard] = useState<BattleCard | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiClient
      .get<ApiResponse<BattleCard>>(`/battle-cards/${id}`)
      .then((response) => setCard(response.data.data))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <div className="auth-loading">
        <Spin />
      </div>
    );
  }

  if (!card) {
    return (
      <Card className="soft-card">
        <Typography.Title level={3}>作战卡不存在</Typography.Title>
      </Card>
    );
  }

  return (
    <Space direction="vertical" size={20} className="page-stack">
      <div>
        <Typography.Text type="secondary">Battle Card</Typography.Text>
        <Typography.Title level={2}>岗位作战卡</Typography.Title>
      </div>

      <Card className="soft-card">
        <Row align="middle" gutter={[24, 24]}>
          <Col xs={24} md={8}>
            <Progress type="dashboard" percent={card.matchScore} strokeColor="#6aa6d8" />
          </Col>
          <Col xs={24} md={16}>
            <Typography.Title level={3}>匹配分 {card.matchScore}</Typography.Title>
            <Typography.Paragraph>
              系统基于简历和岗位 JD 生成结构化准备建议。后续题库会优先围绕面试重点和短板召回。
            </Typography.Paragraph>
          </Col>
        </Row>
      </Card>

      <Row gutter={[16, 16]}>
        {sections.map(([key, title]) => (
          <Col xs={24} md={12} key={key}>
            <Card className="soft-card" title={title}>
              <List
                size="small"
                dataSource={(card[key] as string[]) ?? []}
                renderItem={(item) => <List.Item>{item}</List.Item>}
              />
            </Card>
          </Col>
        ))}
      </Row>
    </Space>
  );
}
