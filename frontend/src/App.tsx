import { Navigate, Route, Routes } from 'react-router-dom';
import { Layout } from './components/Layout';
import { DashboardPage } from './pages/DashboardPage';
import { IssuesPage } from './pages/IssuesPage';
import { ProjectsPage } from './pages/ProjectsPage';
import { TodosPage } from './pages/TodosPage';
import { WeeklyReportPage } from './pages/WeeklyReportPage';
import { WorkLogsPage } from './pages/WorkLogsPage';

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<DashboardPage />} />
        <Route path="dashboard" element={<DashboardPage />} />
        <Route path="projects" element={<ProjectsPage />} />
        <Route path="work-logs" element={<WorkLogsPage />} />
        <Route path="issues" element={<IssuesPage />} />
        <Route path="todos" element={<TodosPage />} />
        <Route path="weekly-report" element={<WeeklyReportPage />} />
        <Route path="*" element={<Navigate replace to="/" />} />
      </Route>
    </Routes>
  );
}
