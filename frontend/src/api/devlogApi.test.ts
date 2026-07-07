import axios from 'axios';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import {
  createWorkLog,
  createIssue,
  generateWeeklyReport,
  listIssues,
  listProjects,
  listWorkLogs,
  updateTodo,
} from './devlogApi';

const httpMock = vi.hoisted(() => ({
  get: vi.fn(),
  post: vi.fn(),
  patch: vi.fn(),
  delete: vi.fn(),
}));

vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => httpMock),
  },
}));

describe('devlogApi', () => {
  beforeEach(() => {
    httpMock.get.mockReset();
    httpMock.post.mockReset();
    httpMock.patch.mockReset();
    httpMock.delete.mockReset();
  });

  it('creates an axios client with the /api base URL', async () => {
    httpMock.get.mockResolvedValue({ data: [] });

    await listProjects();

    expect(axios.create).toHaveBeenCalledWith({ baseURL: '/api' });
    expect(httpMock.get).toHaveBeenCalledWith('/projects');
  });

  it('sends work log filters as query parameters', async () => {
    httpMock.get.mockResolvedValue({ data: [] });

    await listWorkLogs({ date: '2026-07-07', projectId: 1 });

    expect(httpMock.get).toHaveBeenCalledWith('/work-logs', {
      params: { date: '2026-07-07', projectId: 1 },
    });
  });

  it('creates work logs with the planned status', async () => {
    httpMock.post.mockResolvedValue({ data: { id: 1 } });

    await createWorkLog({
      workDate: '2026-07-07',
      title: 'API spec review',
      status: 'PLANNED',
      memo: 'Before implementation',
      projectId: 1,
    });

    expect(httpMock.post).toHaveBeenCalledWith('/work-logs', {
      workDate: '2026-07-07',
      title: 'API spec review',
      status: 'PLANNED',
      memo: 'Before implementation',
      projectId: 1,
    });
  });

  it('supports issue search and creation endpoints', async () => {
    httpMock.get.mockResolvedValue({ data: [] });
    httpMock.post.mockResolvedValue({ data: { id: 1 } });

    await listIssues({ status: 'OPEN', keyword: 'H2' });
    await createIssue({
      title: 'H2 설정',
      content: '콘솔 확인',
      cause: '',
      solution: '',
      status: 'OPEN',
      projectId: 1,
      workLogId: null,
    });

    expect(httpMock.get).toHaveBeenCalledWith('/issues', {
      params: { status: 'OPEN', keyword: 'H2' },
    });
    expect(httpMock.post).toHaveBeenCalledWith('/issues', {
      title: 'H2 설정',
      content: '콘솔 확인',
      cause: '',
      solution: '',
      status: 'OPEN',
      projectId: 1,
      workLogId: null,
    });
  });

  it('updates todos and generates weekly reports through REST endpoints', async () => {
    httpMock.patch.mockResolvedValue({ data: { id: 1 } });
    httpMock.get.mockResolvedValue({ data: { content: 'report' } });

    await updateTodo(1, {
      todoDate: '2026-07-07',
      content: '화면 구현',
      completed: true,
      projectId: null,
    });
    await generateWeeklyReport('2026-07-01', '2026-07-07');

    expect(httpMock.patch).toHaveBeenCalledWith('/todos/1', {
      todoDate: '2026-07-07',
      content: '화면 구현',
      completed: true,
      projectId: null,
    });
    expect(httpMock.get).toHaveBeenCalledWith('/reports/weekly', {
      params: { startDate: '2026-07-01', endDate: '2026-07-07' },
    });
  });
});
