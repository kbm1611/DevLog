export type Project = {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
};

export type WorkLogStatus = 'TODO' | 'IN_PROGRESS' | 'DONE' | 'BLOCKED';
export type IssueStatus = 'OPEN' | 'RESOLVED' | 'HOLD';

export type WorkLog = {
  id: number;
  workDate: string;
  title: string;
  status: WorkLogStatus;
  memo: string;
  projectId: number;
  projectName: string;
  createdAt: string;
  updatedAt: string;
};

export type ProjectPayload = {
  name: string;
  description: string;
};

export type WorkLogPayload = {
  workDate: string;
  title: string;
  status: WorkLogStatus;
  memo: string;
  projectId: number;
};

export type Issue = {
  id: number;
  title: string;
  content: string;
  cause: string;
  solution: string;
  status: IssueStatus;
  projectId: number;
  projectName: string;
  workLogId: number | null;
  workLogTitle: string | null;
  createdAt: string;
  updatedAt: string;
};

export type IssuePayload = {
  title: string;
  content: string;
  cause: string;
  solution: string;
  status: IssueStatus;
  projectId: number;
  workLogId: number | null;
};

export type Todo = {
  id: number;
  todoDate: string;
  content: string;
  completed: boolean;
  projectId: number | null;
  projectName: string | null;
  createdAt: string;
  updatedAt: string;
};

export type TodoPayload = {
  todoDate: string;
  content: string;
  completed: boolean;
  projectId: number | null;
};

export type WeeklyReport = {
  startDate: string;
  endDate: string;
  content: string;
};
