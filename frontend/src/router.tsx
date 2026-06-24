import { createBrowserRouter, Navigate } from 'react-router-dom';
import { RequireAuth } from './auth/RequireAuth';
import { AppLayout } from './layouts/AppLayout';
import { DashboardPage } from './pages/DashboardPage';
import { BattleCardPage } from './pages/BattleCardPage';
import { JobsPage } from './pages/JobsPage';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { ResumesPage } from './pages/ResumesPage';
import { PlaceholderPage } from './pages/PlaceholderPage';
import { QuestionsPage } from './pages/QuestionsPage';
import { QuestionPracticePage } from './pages/QuestionPracticePage';
import { KnowledgePage } from './pages/KnowledgePage';
import { ApplicationsPage } from './pages/ApplicationsPage';

export const router = createBrowserRouter([
  { path: '/', element: <Navigate to="/dashboard" replace /> },
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  {
    element: (
      <RequireAuth>
        <AppLayout />
      </RequireAuth>
    ),
    children: [
      { path: '/dashboard', element: <DashboardPage /> },
      { path: '/resumes', element: <ResumesPage /> },
      { path: '/jobs', element: <JobsPage /> },
      { path: '/jobs/:id', element: <PlaceholderPage title="岗位详情" /> },
      { path: '/battle-cards/:id', element: <BattleCardPage /> },
      { path: '/questions', element: <QuestionsPage /> },
      { path: '/questions/:id', element: <QuestionPracticePage /> },
      { path: '/knowledge', element: <KnowledgePage /> },
      { path: '/applications', element: <ApplicationsPage /> },
      { path: '/settings', element: <PlaceholderPage title="设置" /> },
    ],
  },
]);
