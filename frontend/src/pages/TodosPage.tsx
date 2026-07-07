import { Plus, Save, Trash2 } from 'lucide-react';
import { FormEvent, useEffect, useMemo, useState } from 'react';
import { createTodo, deleteTodo, listProjects, listTodos, updateTodo } from '../api/devlogApi';
import type { Project, Todo, TodoPayload } from '../types/devlog';

const emptyForm: TodoPayload = {
  todoDate: todayIso(),
  content: '',
  completed: false,
  projectId: null,
};

export function TodosPage() {
  const [projects, setProjects] = useState<Project[]>([]);
  const [todos, setTodos] = useState<Todo[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [dateFilter, setDateFilter] = useState(todayIso());
  const [form, setForm] = useState<TodoPayload>(emptyForm);

  const selectedTodo = useMemo(
    () => todos.find((todo) => todo.id === selectedId) ?? null,
    [todos, selectedId],
  );

  useEffect(() => {
    void listProjects().then(setProjects);
  }, []);

  useEffect(() => {
    void refreshTodos();
  }, [dateFilter]);

  async function refreshTodos() {
    setTodos(await listTodos({ date: dateFilter || undefined }));
  }

  function startNewTodo() {
    setSelectedId(null);
    setForm({
      ...emptyForm,
      todoDate: dateFilter || todayIso(),
    });
  }

  function selectTodo(todo: Todo) {
    setSelectedId(todo.id);
    setForm({
      todoDate: todo.todoDate,
      content: todo.content,
      completed: todo.completed,
      projectId: todo.projectId,
    });
  }

  async function toggleTodo(todo: Todo) {
    await updateTodo(todo.id, {
      todoDate: todo.todoDate,
      content: todo.content,
      completed: !todo.completed,
      projectId: todo.projectId,
    });
    await refreshTodos();
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.content.trim()) {
      return;
    }

    if (selectedTodo) {
      await updateTodo(selectedTodo.id, form);
    } else {
      const created = await createTodo(form);
      setSelectedId(created.id);
    }

    await refreshTodos();
  }

  async function handleDelete() {
    if (!selectedTodo) {
      return;
    }

    await deleteTodo(selectedTodo.id);
    startNewTodo();
    await refreshTodos();
  }

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Todos</h1>
          <p>날짜별 내일 할 일과 완료 여부를 관리합니다.</p>
        </div>
        <button className="icon-button" type="button" onClick={startNewTodo} aria-label="새 할 일">
          <Plus aria-hidden="true" size={18} />
        </button>
      </header>

      <div className="filters">
        <label className="field inline-field">
          <span>날짜</span>
          <input type="date" value={dateFilter} onChange={(event) => setDateFilter(event.target.value)} />
        </label>
      </div>

      <div className="two-column">
        <div className="panel">
          <div className="panel-heading">
            <h2>목록</h2>
            <span>{todos.length}</span>
          </div>
          <div className="item-list" aria-label="할 일 목록">
            {todos.map((todo) => (
              <div className="todo-row" key={todo.id}>
                <input
                  aria-label={`${todo.content} 완료 여부`}
                  checked={todo.completed}
                  type="checkbox"
                  onChange={() => void toggleTodo(todo)}
                />
                <button
                  className={`list-row ${todo.id === selectedId ? 'selected' : ''}`}
                  type="button"
                  aria-label={todo.content}
                  onClick={() => selectTodo(todo)}
                >
                  <strong>{todo.content}</strong>
                  <span>{todo.projectName ?? '프로젝트 없음'}</span>
                </button>
              </div>
            ))}
          </div>
        </div>

        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-heading">
            <div>
              <h2>{selectedTodo ? '할 일 수정 중' : '새 할 일 작성'}</h2>
              {selectedTodo && <p className="mode-note">현재 수정 중: {selectedTodo.content}</p>}
            </div>
          </div>

          <label className="field">
            <span>할 일 날짜</span>
            <input
              type="date"
              value={form.todoDate}
              onChange={(event) => setForm((current) => ({ ...current, todoDate: event.target.value }))}
            />
          </label>

          <label className="field">
            <span>할 일</span>
            <input
              value={form.content}
              maxLength={200}
              onChange={(event) => setForm((current) => ({ ...current, content: event.target.value }))}
            />
          </label>

          <label className="field">
            <span>프로젝트</span>
            <select
              value={form.projectId ?? ''}
              onChange={(event) =>
                setForm((current) => ({
                  ...current,
                  projectId: event.target.value ? Number(event.target.value) : null,
                }))
              }
            >
              <option value="">선택 안 함</option>
              {projects.map((project) => (
                <option key={project.id} value={project.id}>
                  {project.name}
                </option>
              ))}
            </select>
          </label>

          <label className="checkbox-field">
            <input
              checked={form.completed}
              type="checkbox"
              onChange={(event) =>
                setForm((current) => ({ ...current, completed: event.target.checked }))
              }
            />
            완료
          </label>

          <div className="form-actions">
            <button className="primary-button" type="submit">
              <Save aria-hidden="true" size={17} />
              {selectedTodo ? '수정 완료' : '할 일 생성'}
            </button>
            {selectedTodo && (
              <button className="danger-button" type="button" onClick={handleDelete}>
                <Trash2 aria-hidden="true" size={17} />
                할 일 삭제
              </button>
            )}
          </div>
        </form>
      </div>
    </section>
  );
}

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}
