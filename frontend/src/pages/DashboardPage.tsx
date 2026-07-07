import { useEffect, useMemo, useState } from 'react';
import { listIssues, listTodos, listWorkLogs } from '../api/devlogApi';
import type { Issue, Todo, WorkLog } from '../types/devlog';

export function DashboardPage() {
  const [workLogs, setWorkLogs] = useState<WorkLog[]>([]);
  const [issues, setIssues] = useState<Issue[]>([]);
  const [todos, setTodos] = useState<Todo[]>([]);
  const today = useMemo(() => todayIso(), []);

  useEffect(() => {
    async function loadDashboard() {
      const [todayWorkLogs, loadedIssues, todayTodos] = await Promise.all([
        listWorkLogs({ date: today }),
        listIssues({}),
        listTodos({ date: today }),
      ]);

      setWorkLogs(todayWorkLogs);
      setIssues(loadedIssues);
      setTodos(todayTodos);
    }

    void loadDashboard();
  }, [today]);

  const openIssues = issues.filter((issue) => issue.status === 'OPEN' || issue.status === 'HOLD');
  const recentIssues = issues.slice(0, 4);

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Dashboard</h1>
          <p>오늘 업무와 남은 이슈를 빠르게 확인합니다.</p>
        </div>
      </header>

      <div className="summary-grid">
        <div className="panel summary-panel">
          <span>오늘 업무</span>
          <strong>{workLogs.length}</strong>
        </div>
        <div className="panel summary-panel">
          <span>열린 이슈</span>
          <strong>{openIssues.length}</strong>
        </div>
        <div className="panel summary-panel">
          <span>오늘 할 일</span>
          <strong>{todos.filter((todo) => !todo.completed).length}</strong>
        </div>
      </div>

      <div className="two-column">
        <div className="panel">
          <div className="panel-heading">
            <h2>오늘 할 일</h2>
            <span>{todos.length}</span>
          </div>
          <div className="item-list">
            {todos.map((todo) => (
              <div className="plain-row" key={todo.id}>
                <strong>{todo.content}</strong>
                <span>{todo.projectName ?? '프로젝트 없음'}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="panel">
          <div className="panel-heading">
            <h2>최근 이슈</h2>
            <span>{recentIssues.length}</span>
          </div>
          <div className="item-list">
            {recentIssues.map((issue) => (
              <div className="plain-row" key={issue.id}>
                <strong>{issue.title}</strong>
                <span>
                  {issue.projectName} · {issue.status}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}
