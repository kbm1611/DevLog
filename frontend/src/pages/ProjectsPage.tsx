import { Plus, Save, Trash2 } from 'lucide-react';
import { FormEvent, useEffect, useMemo, useState } from 'react';
import {
  createProject,
  deleteProject,
  listProjects,
  updateProject,
} from '../api/devlogApi';
import type { Project, ProjectPayload } from '../types/devlog';

const emptyForm: ProjectPayload = {
  name: '',
  description: '',
};

export function ProjectsPage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [form, setForm] = useState<ProjectPayload>(emptyForm);

  const selectedProject = useMemo(
    () => projects.find((project) => project.id === selectedId) ?? null,
    [projects, selectedId],
  );

  useEffect(() => {
    void refreshProjects();
  }, []);

  async function refreshProjects() {
    setProjects(await listProjects());
  }

  function startNewProject() {
    setSelectedId(null);
    setForm(emptyForm);
  }

  function selectProject(project: Project) {
    setSelectedId(project.id);
    setForm({
      name: project.name,
      description: project.description,
    });
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.name.trim()) {
      return;
    }

    if (selectedProject) {
      await updateProject(selectedProject.id, form);
    } else {
      const created = await createProject(form);
      setSelectedId(created.id);
    }

    await refreshProjects();
  }

  async function handleDelete() {
    if (!selectedProject) {
      return;
    }

    await deleteProject(selectedProject.id);
    setSelectedId(null);
    setForm(emptyForm);
    await refreshProjects();
  }

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Projects</h1>
          <p>개발 작업을 묶는 프로젝트를 관리합니다.</p>
        </div>
        <button className="icon-button" type="button" onClick={startNewProject} aria-label="새 프로젝트">
          <Plus aria-hidden="true" size={18} />
        </button>
      </header>

      <div className="two-column">
        <div className="panel">
          <div className="panel-heading">
            <h2>목록</h2>
            <span>{projects.length}</span>
          </div>
          <div className="item-list" aria-label="프로젝트 목록">
            {projects.map((project) => (
              <button
                className={`list-row ${project.id === selectedId ? 'selected' : ''}`}
                key={project.id}
                type="button"
                aria-label={project.name}
                onClick={() => selectProject(project)}
              >
                <strong>{project.name}</strong>
                <span>{project.description || '설명 없음'}</span>
              </button>
            ))}
          </div>
        </div>

        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-heading">
            <div>
              <h2>{selectedProject ? '프로젝트 수정 중' : '새 프로젝트 작성'}</h2>
              {selectedProject && <p className="mode-note">현재 수정 중: {selectedProject.name}</p>}
            </div>
          </div>

          <label className="field">
            <span>프로젝트 이름</span>
            <input
              value={form.name}
              onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
              maxLength={100}
            />
          </label>

          <label className="field">
            <span>설명</span>
            <textarea
              value={form.description}
              onChange={(event) =>
                setForm((current) => ({ ...current, description: event.target.value }))
              }
              maxLength={1000}
              rows={8}
            />
          </label>

          {selectedProject && (
            <dl className="meta-grid">
              <div>
                <dt>Created</dt>
                <dd>{formatDateTime(selectedProject.createdAt)}</dd>
              </div>
              <div>
                <dt>Updated</dt>
                <dd>{formatDateTime(selectedProject.updatedAt)}</dd>
              </div>
            </dl>
          )}

          <div className="form-actions">
            <button className="primary-button" type="submit">
              <Save aria-hidden="true" size={17} />
              {selectedProject ? '수정 완료' : '프로젝트 생성'}
            </button>
            {selectedProject && (
              <button className="danger-button" type="button" onClick={handleDelete}>
                <Trash2 aria-hidden="true" size={17} />
                프로젝트 삭제
              </button>
            )}
          </div>
        </form>
      </div>
    </section>
  );
}

function formatDateTime(value: string) {
  return value.replace('T', ' ');
}
