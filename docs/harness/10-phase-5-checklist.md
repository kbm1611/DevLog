# 5단계 점검 체크리스트

이 문서는 DevLog MVP 5단계인 예외 처리, 입력값 검증, 테스트 코드 보강, README 작성을 완료하기 위한 점검 기준이다. 1-4단계 기능이 구현된 뒤 완료 여부를 판단할 때 사용한다.

## Scope

- 예외 처리.
- 입력값 검증.
- 테스트 코드 보강.
- README 작성과 최신화.
- 백엔드와 프론트엔드 검증 명령 확인.

## Backend Exception Handling

- [ ] 존재하지 않는 리소스는 `NotFoundException`으로 처리한다.
- [ ] `NotFoundException`은 일관된 에러 응답과 HTTP 404로 변환된다.
- [ ] Bean Validation 실패는 일관된 에러 응답과 HTTP 400으로 변환된다.
- [ ] 잘못된 요청 값이나 도메인 규칙 위반은 HTTP 400 계열 응답으로 처리한다.
- [ ] Controller는 예외 응답을 직접 조립하지 않고 공통 예외 처리에 위임한다.

## Backend Validation

- [ ] `ProjectCreateRequest`와 `ProjectUpdateRequest`에 이름 필수값과 길이 제한이 있다.
- [ ] `WorkLogCreateRequest`와 `WorkLogUpdateRequest`에 날짜, 제목, 상태, 프로젝트 필수값 검증이 있다.
- [ ] `IssueCreateRequest`와 `IssueUpdateRequest`에 제목, 내용, 상태, 프로젝트 필수값 검증이 있다.
- [ ] `TodoCreateRequest`와 `TodoUpdateRequest`에 날짜와 내용 필수값 검증이 있다.
- [ ] Controller의 생성/수정 요청 DTO에 `@Valid`가 적용되어 있다.

## Backend Tests

- [ ] `ProjectServiceTest`가 등록, 조회, 수정, 삭제 흐름을 검증한다.
- [ ] `WorkLogServiceTest`가 날짜/프로젝트 필터, 수정, 삭제 흐름을 검증한다.
- [ ] `IssueServiceTest`가 상태/키워드 필터, 관련 업무 일지, 수정, 삭제 흐름을 검증한다.
- [ ] `TodoServiceTest`가 날짜 필터, 완료 여부 수정, 삭제 흐름을 검증한다.
- [ ] `WeeklyReportServiceTest`가 기간 내 업무 일지, 해결된 이슈, 남은 이슈, 할 일을 보고서에 포함하는지 검증한다.
- [ ] 필요한 경우 예외 처리 또는 검증 실패 테스트를 추가한다.

## Frontend Tests

- [ ] `devlogApi.test.ts`가 Axios base URL과 주요 REST 엔드포인트 호출을 검증한다.
- [ ] `App.test.tsx`가 Dashboard, Issue, Todo, WeeklyReport, Project/WorkLog 화면 흐름을 검증한다.
- [ ] 4단계 화면에서 API 함수가 실제 모듈 경로로 연결되어 있다.
- [ ] 주간보고 화면은 복사 가능한 텍스트 영역을 제공한다.

## README

- [ ] 프로젝트 목적과 로컬 개인용 MVP 범위를 설명한다.
- [ ] Backend/Frontend 기술 스택을 설명한다.
- [ ] 폴더 구조와 주요 패키지 책임을 설명한다.
- [ ] 주요 기능과 API 요약을 제공한다.
- [ ] 백엔드 실행 방법과 H2 접속 정보를 제공한다.
- [ ] 프론트엔드 실행 방법과 Vite proxy 기준을 제공한다.
- [ ] 테스트와 빌드 명령을 제공한다.
- [ ] 현재 MVP 단계 상태를 제공한다.
- [ ] 관련 하네스 문서 링크를 제공한다.

## Verification Commands

백엔드:

```powershell
cd backend
.\gradlew.bat test
.\gradlew.bat build
```

프론트엔드:

```powershell
cd frontend
npm test
npm run build
```

문서:

```powershell
git diff --check -- README.md docs\harness
rg -n "09-devlog-mvp-spec|10-phase-5-checklist" README.md docs\harness
```

## Completion Criteria

- [ ] 위 체크리스트에서 현재 MVP에 해당하는 항목을 모두 확인했다.
- [ ] 검증 명령이 통과했다.
- [ ] 실패를 수정했다면 `08-failure-fix-log.md`에 기록했다.
- [ ] 남은 항목이 있다면 다음 작업으로 분리했다.
