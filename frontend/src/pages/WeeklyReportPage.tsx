import { Clipboard, FileText } from 'lucide-react';
import { FormEvent, useState } from 'react';
import { generateWeeklyReport } from '../api/devlogApi';

export function WeeklyReportPage() {
  const [startDate, setStartDate] = useState(todayIso());
  const [endDate, setEndDate] = useState(todayIso());
  const [content, setContent] = useState('');

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const report = await generateWeeklyReport(startDate, endDate);
    setContent(report.content);
  }

  async function copyReport() {
    if (!content || !navigator.clipboard) {
      return;
    }

    await navigator.clipboard.writeText(content);
  }

  return (
    <section className="page-section">
      <header className="page-header">
        <div>
          <h1>Weekly Report</h1>
          <p>기간을 선택해 업무 일지, 이슈, 할 일을 보고서 텍스트로 모읍니다.</p>
        </div>
      </header>

      <form className="filters" onSubmit={handleSubmit}>
        <label className="field inline-field">
          <span>시작일</span>
          <input type="date" value={startDate} onChange={(event) => setStartDate(event.target.value)} />
        </label>
        <label className="field inline-field">
          <span>종료일</span>
          <input type="date" value={endDate} onChange={(event) => setEndDate(event.target.value)} />
        </label>
        <button className="primary-button filter-action" type="submit">
          <FileText aria-hidden="true" size={17} />
          보고서 생성
        </button>
      </form>

      <div className="panel form-panel">
        <div className="panel-heading">
          <h2>생성된 보고서</h2>
          <button className="primary-button compact-button" type="button" onClick={() => void copyReport()}>
            <Clipboard aria-hidden="true" size={17} />
            복사하기
          </button>
        </div>
        <textarea
          aria-label="보고서 내용"
          className="report-textarea"
          value={content}
          onChange={(event) => setContent(event.target.value)}
          rows={18}
        />
      </div>
    </section>
  );
}

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}
