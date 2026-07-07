import { Save, Trash2 } from 'lucide-react';
import { FormEvent, useEffect, useMemo, useState } from 'react';
import {
  createWorkLog,
  deleteWorkLog,
  listProjects,
  listWorkLogs,
  updateWorkLog,
} from '../api/devlogApi';
import type { Project, WorkLog, WorkLogPayload, WorkLogStatus } from '../types/devlog';

const statusOptions: WorkLogStatus[] = ['PLANNED', 'IN_PROGRESS', 'DONE', 'BLOCKED'];

const emptyForm: WorkLogPayload = {
  workDate: '2026-07-07',
  title: '',
  status: 'PLANNED',
  memo: '',
  projectId: 1,
};

export function WorkLogsPage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [workLogs, setWorkLogs] = useState<WorkLog[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [dateFilter, setDateFilter] = useState('');
  const [projectFilter, setProjectFilter] = useState('');
  const [form, setForm] = useState<WorkLogPayload>(emptyForm);

  const selectedWorkLog = useMemo(
    () => workLogs.find((workLog) => workLog.id === selectedId) ?? null,
    [workLogs, selectedId],
  );

  useEffect(() => {
    async function loadInitialData() {
      const loadedProjects = await listProjects();
      setProjects(loadedProjects);
      setForm((current) => ({
        ...current,
        projectId: loadedProjects[0]?.id ?? current.projectId,
      }));
    }

    void loadInitialData();
  }, []);

  useEffect(() => {
    void refreshWorkLogs();
  }, [dateFilter, projectFilter]);

  async function refreshWorkLogs() {
    setWorkLogs(
      await listWorkLogs({
        date: dateFilter || undefined,
        projectId: projectFilter ? Number(projectFilter) : undefined,
      }),
    );
  }

  function selectWorkLog(workLog: WorkLog) {
    setSelectedId(workLog.id);
    setForm({
      workDate: workLog.workDate,
      title: workLog.title,
      status: workLog.status,
      memo: workLog.memo,
      projectId: workLog.projectId,
    });
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.title.trim()) {
      return;
    }

    if (selectedWorkLog) {
      await updateWorkLog(selectedWorkLog.id, form);
    } else {
      const created = await createWorkLog(form);
      setSelectedId(created.id);
    }

    await refreshWorkLogs();
  }

  async function handleDelete() {
    if (!selectedWorkLog) {
      return;
    }

    await deleteWorkLog(selectedWorkLog.id);
    setSelectedId(null);
    setForm({
      ...emptyForm,
      projectId: projects[0]?.id ?? emptyForm.projectId,
    });
    await refreshWorkLogs();
  }

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Work Logs</h1>
          <p>날짜와 프로젝트별 작업 기록을 관리합니다.</p>
        </div>
      </header>

      <div className="filters">
        <label className="field inline-field">
          <span>날짜 필터</span>
          <input
            type="date"
            value={dateFilter}
            onChange={(event) => setDateFilter(event.target.value)}
          />
        </label>
        <label className="field inline-field">
          <span>프로젝트 필터</span>
          <select value={projectFilter} onChange={(event) => setProjectFilter(event.target.value)}>
            <option value="">전체 프로젝트</option>
            {projects.map((project) => (
              <option key={project.id} value={project.id}>
                {project.name}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="two-column">
        <div className="panel">
          <div className="panel-heading">
            <h2>목록</h2>
            <span>{workLogs.length}</span>
          </div>
          <div className="item-list" aria-label="작업 기록 목록">
            {workLogs.map((workLog) => (
              <button
                className={`list-row ${workLog.id === selectedId ? 'selected' : ''}`}
                key={workLog.id}
                type="button"
                aria-label={workLog.title}
                onClick={() => selectWorkLog(workLog)}
              >
                <strong>{workLog.title}</strong>
                <span>
                  {workLog.workDate} · {workLog.projectName}
                </span>
                <small className={`status-badge ${workLog.status.toLowerCase()}`}>{workLog.status}</small>
              </button>
            ))}
          </div>
        </div>

        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-heading">
            <h2>{selectedWorkLog ? '상세' : '새 작업 기록'}</h2>
          </div>

          <label className="field">
            <span>작업일</span>
            <input
              type="date"
              value={form.workDate}
              onChange={(event) =>
                setForm((current) => ({ ...current, workDate: event.target.value }))
              }
            />
          </label>

          <label className="field">
            <span>제목</span>
            <input
              value={form.title}
              onChange={(event) => setForm((current) => ({ ...current, title: event.target.value }))}
              maxLength={200}
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
                  setForm((current) => ({ ...current, status: event.target.value as WorkLogStatus }))
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
            <span>메모</span>
            <textarea
              value={form.memo}
              onChange={(event) => setForm((current) => ({ ...current, memo: event.target.value }))}
              maxLength={2000}
              rows={7}
            />
          </label>

          <div className="form-actions">
            <button className="primary-button" type="submit">
              <Save aria-hidden="true" size={17} />
              작업 기록 저장
            </button>
            {selectedWorkLog && (
              <button className="danger-button" type="button" onClick={handleDelete}>
                <Trash2 aria-hidden="true" size={17} />
                작업 기록 삭제
              </button>
            )}
          </div>
        </form>
      </div>
    </section>
  );
}
