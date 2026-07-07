import { ClipboardList, FileText, FolderKanban, LayoutDashboard, ListTodo, NotebookTabs } from 'lucide-react';
import { NavLink, Outlet } from 'react-router-dom';

const navItems = [
  { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
  { label: 'Projects', path: '/projects', icon: FolderKanban },
  { label: 'Work Logs', path: '/work-logs', icon: ClipboardList },
  { label: 'Issues', path: '/issues', icon: NotebookTabs },
  { label: 'Todos', path: '/todos', icon: ListTodo },
  { label: 'Weekly Report', path: '/weekly-report', icon: FileText },
];

export function Layout() {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <span className="brand-mark">D</span>
          <div>
            <strong>DevLog</strong>
            <span>Work journal</span>
          </div>
        </div>

        <nav className="nav-list" aria-label="주요 메뉴">
          {navItems.map((item) => {
            const Icon = item.icon;

            return (
              <NavLink className="nav-item" key={item.path} to={item.path}>
                <Icon aria-hidden="true" size={18} />
                {item.label}
              </NavLink>
            );
          })}
        </nav>
      </aside>

      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
