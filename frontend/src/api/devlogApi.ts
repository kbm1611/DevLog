import axios from 'axios';
import type {
  Issue,
  IssuePayload,
  IssueStatus,
  Project,
  ProjectPayload,
  Todo,
  TodoPayload,
  WeeklyReport,
  WorkLog,
  WorkLogPayload,
} from '../types/devlog';

type WorkLogFilters = {
  date?: string;
  projectId?: number;
};

type IssueFilters = {
  status?: IssueStatus;
  keyword?: string;
};

type TodoFilters = {
  date?: string;
};

const http = axios.create({ baseURL: '/api' });

export async function listProjects(): Promise<Project[]> {
  const response = await http.get<Project[]>('/projects');
  return response.data;
}

export async function createProject(payload: ProjectPayload): Promise<Project> {
  const response = await http.post<Project>('/projects', payload);
  return response.data;
}

export async function updateProject(id: number, payload: ProjectPayload): Promise<Project> {
  const response = await http.patch<Project>(`/projects/${id}`, payload);
  return response.data;
}

export async function deleteProject(id: number): Promise<void> {
  await http.delete(`/projects/${id}`);
}

export async function listWorkLogs(filters: WorkLogFilters = {}): Promise<WorkLog[]> {
  const response = await http.get<WorkLog[]>('/work-logs', { params: filters });
  return response.data;
}

export async function createWorkLog(payload: WorkLogPayload): Promise<WorkLog> {
  const response = await http.post<WorkLog>('/work-logs', payload);
  return response.data;
}

export async function updateWorkLog(id: number, payload: WorkLogPayload): Promise<WorkLog> {
  const response = await http.patch<WorkLog>(`/work-logs/${id}`, payload);
  return response.data;
}

export async function deleteWorkLog(id: number): Promise<void> {
  await http.delete(`/work-logs/${id}`);
}

export async function listIssues(filters: IssueFilters = {}): Promise<Issue[]> {
  const response = await http.get<Issue[]>('/issues', { params: filters });
  return response.data;
}

export async function createIssue(payload: IssuePayload): Promise<Issue> {
  const response = await http.post<Issue>('/issues', payload);
  return response.data;
}

export async function updateIssue(id: number, payload: IssuePayload): Promise<Issue> {
  const response = await http.patch<Issue>(`/issues/${id}`, payload);
  return response.data;
}

export async function deleteIssue(id: number): Promise<void> {
  await http.delete(`/issues/${id}`);
}

export async function listTodos(filters: TodoFilters = {}): Promise<Todo[]> {
  const response = await http.get<Todo[]>('/todos', { params: filters });
  return response.data;
}

export async function createTodo(payload: TodoPayload): Promise<Todo> {
  const response = await http.post<Todo>('/todos', payload);
  return response.data;
}

export async function updateTodo(id: number, payload: TodoPayload): Promise<Todo> {
  const response = await http.patch<Todo>(`/todos/${id}`, payload);
  return response.data;
}

export async function deleteTodo(id: number): Promise<void> {
  await http.delete(`/todos/${id}`);
}

export async function generateWeeklyReport(startDate: string, endDate: string): Promise<WeeklyReport> {
  const response = await http.get<WeeklyReport>('/reports/weekly', {
    params: { startDate, endDate },
  });
  return response.data;
}
