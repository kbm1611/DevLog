import { Plus, Save, Trash2 } from 'lucide-react';
import { FormEvent, useEffect, useMemo, useState } from 'react';
import {
  createIssue,
  deleteIssue,
  listIssues,
  listProjects,
  listWorkLogs,
  updateIssue,
} from '../api/devlogApi';
import type { Issue, IssuePayload, IssueStatus, Project, WorkLog } from '../types/devlog';

const statusOptions: IssueStatus[] = ['OPEN', 'RESOLVED', 'HOLD'];

const emptyForm: IssuePayload = {
  title: '',
  content: '',
  cause: '',
  solution: '',
  status: 'OPEN',
  projectId: 1,
  workLogId: null,
};

export function IssuesPage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [workLogs, setWorkLogs] = useState<WorkLog[]>([]);
  const [issues, setIssues] = useState<Issue[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [statusFilter, setStatusFilter] = useState('');
  const [keyword, setKeyword] = useState('');
  const [form, setForm] = useState<IssuePayload>(emptyForm);

  const selectedIssue = useMemo(
    () => issues.find((issue) => issue.id === selectedId) ?? null,
    [issues, selectedId],
  );

  useEffect(() => {
    async function loadOptions() {
      const [loadedProjects, loadedWorkLogs] = await Promise.all([listProjects(), listWorkLogs()]);
      setProjects(loadedProjects);
      setWorkLogs(loadedWorkLogs);
      setForm((current) => ({
        ...current,
        projectId: loadedProjects[0]?.id ?? current.projectId,
      }));
    }

    void loadOptions();
  }, []);

  useEffect(() => {
    void refreshIssues();
  }, [statusFilter, keyword]);

  async function refreshIssues() {
    setIssues(
      await listIssues({
        status: statusFilter ? (statusFilter as IssueStatus) : undefined,
        keyword: keyword.trim() || undefined,
      }),
    );
  }

  function startNewIssue() {
    setSelectedId(null);
    setForm({
      ...emptyForm,
      projectId: projects[0]?.id ?? emptyForm.projectId,
    });
  }

  function selectIssue(issue: Issue) {
    setSelectedId(issue.id);
    setForm({
      title: issue.title,
      content: issue.content,
      cause: issue.cause ?? '',
      solution: issue.solution ?? '',
      status: issue.status,
      projectId: issue.projectId,
      workLogId: issue.workLogId,
    });
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.title.trim() || !form.content.trim()) {
      return;
    }

    if (selectedIssue) {
      await updateIssue(selectedIssue.id, form);
    } else {
      const created = await createIssue(form);
      setSelectedId(created.id);
    }

    await refreshIssues();
  }

  async function handleDelete() {
    if (!selectedIssue) {
      return;
    }

    await deleteIssue(selectedIssue.id);
    startNewIssue();
    await refreshIssues();
  }

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Issues</h1>
          <p>업무 중 만난 문제와 해결 방법을 기록합니다.</p>
        </div>
        <button className="icon-button" type="button" onClick={startNewIssue} aria-label="새 이슈">
          <Plus aria-hidden="true" size={18} />
        </button>
      </header>

      <div className="filters">
        <label className="field inline-field">
          <span>상태 필터</span>
          <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)}>
            <option value="">전체 상태</option>
            {statusOptions.map((status) => (
              <option key={status} value={status}>
                {status}
              </option>
            ))}
          </select>
        </label>
        <label className="field inline-field">
          <span>키워드</span>
          <input value={keyword} onChange={(event) => setKeyword(event.target.value)} />
        </label>
      </div>

      <div className="two-column">
        <div className="panel">
          <div className="panel-heading">
            <h2>목록</h2>
            <span>{issues.length}</span>
          </div>
          <div className="item-list" aria-label="이슈 목록">
            {issues.map((issue) => (
              <button
                className={`list-row ${issue.id === selectedId ? 'selected' : ''}`}
                key={issue.id}
                type="button"
                aria-label={issue.title}
                onClick={() => selectIssue(issue)}
              >
                <strong>{issue.title}</strong>
                <span>
                  {issue.projectName} · {issue.workLogTitle ?? '업무 일지 없음'}
                </span>
                <small className={`status-badge ${issue.status.toLowerCase()}`}>{issue.status}</small>
              </button>
            ))}
          </div>
        </div>

        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-heading">
            <div>
              <h2>{selectedIssue ? '이슈 수정 중' : '새 이슈 작성'}</h2>
              {selectedIssue && <p className="mode-note">현재 수정 중: {selectedIssue.title}</p>}
            </div>
          </div>

          <label className="field">
            <span>이슈 제목</span>
            <input
              value={form.title}
              maxLength={200}
              onChange={(event) => setForm((current) => ({ ...current, title: event.target.value }))}
            />
          </label>

          <label className="field">
            <span>이슈 내용</span>
            <textarea
              value={form.content}
              maxLength={4000}
              rows={5}
              onChange={(event) => setForm((current) => ({ ...current, content: event.target.value }))}
            />
          </label>

          <div className="form-grid">
            <label className="field">
              <span>프로젝트</span>
              <select
                value={form.projectId}
                onChange={(event) =>
                  setForm((current) => ({ ...current, projectId: Number(event.target.value) }))
                }
              >
                {projects.map((project) => (
                  <option key={project.id} value={project.id}>
                    {project.name}
                  </option>
                ))}
              </select>
            </label>

            <label className="field">
              <span>상태</span>
              <select
                value={form.status}
                onChange={(event) =>
                  setForm((current) => ({ ...current, status: event.target.value as IssueStatus }))
                }
              >
                {statusOptions.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </label>
          </div>

          <label className="field">
            <span>관련 업무 일지</span>
            <select
              value={form.workLogId ?? ''}
              onChange={(event) =>
                setForm((current) => ({
                  ...current,
                  workLogId: event.target.value ? Number(event.target.value) : null,
                }))
              }
            >
              <option value="">선택 안 함</option>
              {workLogs.map((workLog) => (
                <option key={workLog.id} value={workLog.id}>
                  {workLog.workDate} · {workLog.title}
                </option>
              ))}
            </select>
          </label>

          <label className="field">
            <span>원인</span>
            <textarea
              value={form.cause}
              maxLength={2000}
              rows={3}
              onChange={(event) => setForm((current) => ({ ...current, cause: event.target.value }))}
            />
          </label>

          <label className="field">
            <span>해결 방법</span>
            <textarea
              value={form.solution}
              maxLength={4000}
              rows={4}
              onChange={(event) => setForm((current) => ({ ...current, solution: event.target.value }))}
            />
          </label>

          <div className="form-actions">
            <button className="primary-button" type="submit">
              <Save aria-hidden="true" size={17} />
              {selectedIssue ? '수정 완료' : '이슈 생성'}
            </button>
            {selectedIssue && (
              <button className="danger-button" type="button" onClick={handleDelete}>
                <Trash2 aria-hidden="true" size={17} />
                이슈 삭제
              </button>
            )}
          </div>
        </form>
      </div>
    </section>
  );
}
