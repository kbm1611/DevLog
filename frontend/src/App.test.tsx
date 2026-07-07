import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import App from './App';
import type { Issue, Project, Todo, WeeklyReport, WorkLog } from './types/devlog';

const projects: Project[] = [
  {
    id: 1,
    name: 'Backend',
    description: 'Spring Boot API',
    createdAt: '2026-07-01T09:00:00',
    updatedAt: '2026-07-01T09:00:00',
  },
  {
    id: 2,
    name: 'Frontend',
    description: 'React UI',
    createdAt: '2026-07-02T09:00:00',
    updatedAt: '2026-07-02T09:00:00',
  },
];

const workLogs: WorkLog[] = [
  {
    id: 1,
    workDate: '2026-07-07',
    title: 'Project CRUD 점검',
    status: 'DONE',
    memo: 'API 연동 확인',
    projectId: 1,
    projectName: 'Backend',
    createdAt: '2026-07-07T09:00:00',
    updatedAt: '2026-07-07T09:00:00',
  },
];

const issues: Issue[] = [
  {
    id: 1,
    title: 'H2 콘솔 접속 확인',
    content: '로컬 H2 설정 확인 필요',
    cause: '초기 설정 누락',
    solution: '',
    status: 'OPEN',
    projectId: 1,
    projectName: 'Backend',
    workLogId: 1,
    workLogTitle: 'Project CRUD 점검',
    createdAt: '2026-07-07T10:00:00',
    updatedAt: '2026-07-07T10:00:00',
  },
  {
    id: 2,
    title: '보고서 문구 정리',
    content: '복사용 텍스트 문구 정리',
    cause: '',
    solution: '섹션 제목 정리',
    status: 'RESOLVED',
    projectId: 2,
    projectName: 'Frontend',
    workLogId: null,
    workLogTitle: null,
    createdAt: '2026-07-07T11:00:00',
    updatedAt: '2026-07-07T11:00:00',
  },
];

const todos: Todo[] = [
  {
    id: 1,
    todoDate: '2026-07-07',
    content: 'Issue 화면 연결',
    completed: false,
    projectId: 2,
    projectName: 'Frontend',
    createdAt: '2026-07-07T12:00:00',
    updatedAt: '2026-07-07T12:00:00',
  },
];

const weeklyReport: WeeklyReport = {
  startDate: '2026-07-01',
  endDate: '2026-07-07',
  content: '[주간보고]\n- Project CRUD 점검\n- 보고서 문구 정리',
};

const apiMock = vi.hoisted(() => ({
  listProjects: vi.fn(),
  createProject: vi.fn(),
  updateProject: vi.fn(),
  deleteProject: vi.fn(),
  listWorkLogs: vi.fn(),
  createWorkLog: vi.fn(),
  updateWorkLog: vi.fn(),
  deleteWorkLog: vi.fn(),
  listIssues: vi.fn(),
  createIssue: vi.fn(),
  updateIssue: vi.fn(),
  deleteIssue: vi.fn(),
  listTodos: vi.fn(),
  createTodo: vi.fn(),
  updateTodo: vi.fn(),
  deleteTodo: vi.fn(),
  generateWeeklyReport: vi.fn(),
}));

vi.mock('./api/devlogApi', () => apiMock);

describe('App', () => {
  beforeEach(() => {
    apiMock.listProjects.mockResolvedValue(projects);
    apiMock.createProject.mockResolvedValue(projects[0]);
    apiMock.updateProject.mockResolvedValue(projects[0]);
    apiMock.deleteProject.mockResolvedValue(undefined);
    apiMock.listWorkLogs.mockResolvedValue(workLogs);
    apiMock.createWorkLog.mockResolvedValue(workLogs[0]);
    apiMock.updateWorkLog.mockResolvedValue(workLogs[0]);
    apiMock.deleteWorkLog.mockResolvedValue(undefined);
    apiMock.listIssues.mockResolvedValue(issues);
    apiMock.createIssue.mockResolvedValue(issues[0]);
    apiMock.updateIssue.mockResolvedValue({ ...issues[0], status: 'RESOLVED' });
    apiMock.deleteIssue.mockResolvedValue(undefined);
    apiMock.listTodos.mockResolvedValue(todos);
    apiMock.createTodo.mockResolvedValue(todos[0]);
    apiMock.updateTodo.mockResolvedValue({ ...todos[0], completed: true });
    apiMock.deleteTodo.mockResolvedValue(undefined);
    apiMock.generateWeeklyReport.mockResolvedValue(weeklyReport);
  });

  it('opens the dashboard from the root route', async () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>,
    );

    expect(await screen.findByRole('heading', { name: 'Dashboard' })).toBeInTheDocument();
    expect(screen.getByText('열린 이슈')).toBeInTheDocument();
    expect(screen.getByText('Issue 화면 연결')).toBeInTheDocument();
  });

  it('supports issue filtering and updates through the detail form', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/issues']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Issues' });
    await user.selectOptions(screen.getByLabelText('상태 필터'), 'OPEN');
    await user.type(screen.getByLabelText('키워드'), 'H2');

    expect(apiMock.listIssues).toHaveBeenLastCalledWith({ status: 'OPEN', keyword: 'H2' });

    await user.click(screen.getByRole('button', { name: 'H2 콘솔 접속 확인' }));
    await user.selectOptions(screen.getByLabelText('상태'), 'RESOLVED');
    await user.type(screen.getByLabelText('해결 방법'), '콘솔 경로 확인');
    await user.click(screen.getByRole('button', { name: '수정 완료' }));

    expect(apiMock.updateIssue).toHaveBeenCalledWith(
      1,
      expect.objectContaining({ status: 'RESOLVED', solution: '콘솔 경로 확인' }),
    );
  });

  it('supports todo completion toggles', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/todos']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Todos' });
    await user.click(screen.getByRole('checkbox', { name: 'Issue 화면 연결 완료 여부' }));

    expect(apiMock.updateTodo).toHaveBeenCalledWith(1, expect.objectContaining({ completed: true }));
  });

  it('filters work logs by start and end date with project', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/work-logs']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Work Logs' });
    apiMock.listWorkLogs.mockClear();

    await user.type(screen.getByLabelText('시작일'), '2026-07-01');
    await user.clear(screen.getByLabelText('종료일'));
    await user.type(screen.getByLabelText('종료일'), '2026-07-07');
    await user.selectOptions(screen.getByLabelText('프로젝트 필터'), '2');

    await waitFor(() =>
      expect(apiMock.listWorkLogs).toHaveBeenLastCalledWith({
        startDate: '2026-07-01',
        endDate: '2026-07-07',
        projectId: 2,
      }),
    );
  });

  it("filters todos by today's default range and a changed date range", async () => {
    const user = userEvent.setup();
    const today = new Date().toISOString().slice(0, 10);

    render(
      <MemoryRouter initialEntries={['/todos']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Todos' });
    await waitFor(() =>
      expect(apiMock.listTodos).toHaveBeenCalledWith({
        startDate: today,
        endDate: today,
      }),
    );

    apiMock.listTodos.mockClear();
    await user.clear(screen.getByLabelText('시작일'));
    await user.type(screen.getByLabelText('시작일'), '2026-07-08');
    await user.clear(screen.getByLabelText('종료일'));
    await user.type(screen.getByLabelText('종료일'), '2026-07-10');

    await waitFor(() =>
      expect(apiMock.listTodos).toHaveBeenLastCalledWith({
        startDate: '2026-07-08',
        endDate: '2026-07-10',
      }),
    );
  });

  it('shows project created and updated timestamps in a readable Korean format', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/projects']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Projects' });
    await user.click(await screen.findByRole('button', { name: projects[0].name }));

    expect(screen.getByText('생성일')).toBeInTheDocument();
    expect(screen.getByText('수정일')).toBeInTheDocument();
    expect(screen.getAllByText('2026년 07월 01일')).toHaveLength(2);
  });

  it.each([
    {
      route: '/projects',
      heading: 'Projects',
      createHeading: '새 프로젝트 작성',
      createButton: '프로젝트 생성',
      newButton: '새 프로젝트',
      rowButton: projects[0].name,
      editHeading: '프로젝트 수정 중',
      editingLabel: `현재 수정 중: ${projects[0].name}`,
      deleteButton: '프로젝트 삭제',
    },
    {
      route: '/work-logs',
      heading: 'Work Logs',
      createHeading: '새 작업 기록 작성',
      createButton: '작업 기록 생성',
      newButton: '새 작업 기록',
      rowButton: workLogs[0].title,
      editHeading: '작업 기록 수정 중',
      editingLabel: `현재 수정 중: ${workLogs[0].title}`,
      deleteButton: '작업 기록 삭제',
    },
    {
      route: '/issues',
      heading: 'Issues',
      createHeading: '새 이슈 작성',
      createButton: '이슈 생성',
      newButton: '새 이슈',
      rowButton: issues[0].title,
      editHeading: '이슈 수정 중',
      editingLabel: `현재 수정 중: ${issues[0].title}`,
      deleteButton: '이슈 삭제',
    },
    {
      route: '/todos',
      heading: 'Todos',
      createHeading: '새 할 일 작성',
      createButton: '할 일 생성',
      newButton: '새 할 일',
      rowButton: todos[0].content,
      editHeading: '할 일 수정 중',
      editingLabel: `현재 수정 중: ${todos[0].content}`,
      deleteButton: '할 일 삭제',
    },
  ])(
    'distinguishes create and edit modes on $heading',
    async ({ route, heading, createHeading, createButton, newButton, rowButton, editHeading, editingLabel, deleteButton }) => {
      const user = userEvent.setup();

      render(
        <MemoryRouter initialEntries={[route]}>
          <App />
        </MemoryRouter>,
      );

      await screen.findByRole('heading', { name: heading });

      expect(screen.getByRole('heading', { name: createHeading })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: createButton })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: newButton })).toBeInTheDocument();

      expect(screen.queryByRole('button', { name: `${rowButton} 수정` })).not.toBeInTheDocument();
      await user.click(await screen.findByRole('button', { name: rowButton }));

      expect(screen.getByRole('heading', { name: editHeading })).toBeInTheDocument();
      expect(screen.getByText(editingLabel)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: newButton })).toBeInTheDocument();
      expect(screen.queryByRole('button', { name: '새로 작성' })).not.toBeInTheDocument();

      const actionButtons = screen.getAllByRole('button').map((button) => button.textContent?.trim());
      expect(actionButtons).toEqual(expect.arrayContaining(['수정 완료', deleteButton]));
      expect(actionButtons.indexOf('수정 완료')).toBeLessThan(actionButtons.indexOf(deleteButton));
    },
  );

  it('generates a weekly report and shows copy-ready text', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/weekly-report']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Weekly Report' });
    await user.clear(screen.getByLabelText('시작일'));
    await user.type(screen.getByLabelText('시작일'), '2026-07-01');
    await user.clear(screen.getByLabelText('종료일'));
    await user.type(screen.getByLabelText('종료일'), '2026-07-07');
    await user.click(screen.getByRole('button', { name: '보고서 생성' }));

    expect(apiMock.generateWeeklyReport).toHaveBeenCalledWith('2026-07-01', '2026-07-07');
    await waitFor(() => expect(screen.getByLabelText('보고서 내용')).toHaveValue(weeklyReport.content));
  });

  it('keeps project and work log screens wired to the real API module', async () => {
    render(
      <MemoryRouter initialEntries={['/work-logs']}>
        <App />
      </MemoryRouter>,
    );

    await screen.findByRole('heading', { name: 'Work Logs' });

    const list = screen.getByLabelText('작업 기록 목록');
    expect(within(list).getByText('Project CRUD 점검')).toBeInTheDocument();
    expect(apiMock.listWorkLogs).toHaveBeenCalled();
  });
});
