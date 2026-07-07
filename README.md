# DevLog

DevLog는 신입 개발자가 SI/공공기관 프로젝트 업무를 하면서 매일 작성하는 업무 일지, 이슈, 해결 방법, 내일 할 일을 관리하고 주간보고 형태로 정리할 수 있도록 돕는 로컬 개인용 웹 애플리케이션입니다.

이 프로젝트는 배포용 서비스가 아니라 로컬에서 매일 사용하는 MVP를 목표로 합니다. 초기 범위에는 인증, 권한, 다중 사용자, Docker, CI/CD, 클라우드 배포를 포함하지 않습니다.

## MVP Status

- 1단계: Backend Project/WorkLog CRUD 구현 완료
- 2단계: Issue/Todo CRUD와 WeeklyReport 생성 로직 구현 완료
- 3단계: React 라우팅, 사이드바 레이아웃, Project/WorkLog 화면 구현 완료
- 4단계: Issue/Todo/WeeklyReport 화면, Axios API 연동, 기본 CSS 구현 완료
- 5단계: 예외 처리, 입력값 검증, 테스트 코드 보강, README 작성 진행 중

상세 MVP 기준과 구현 단계는 `docs/harness/09-devlog-mvp-spec.md`를 기준으로 관리합니다.

## Tech Stack

### Backend

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Lombok
- Gradle

### Frontend

- React
- Vite
- TypeScript
- Axios
- React Router
- 일반 CSS

## Project Structure

```text
DevLog/
  AGENTS.md
  README.md
  backend/
    build.gradle
    settings.gradle
    gradlew.bat
    src/main/resources/application.yml
    src/main/java/com/devlog/
      controller/     REST API 요청과 응답
      domain/         JPA Entity, BaseEntity, enum
      dto/            기능별 Request/Response DTO
        project/
        worklog/
        issue/
        todo/
        report/
      exception/      공통 예외 처리
      repository/     Spring Data JPA Repository
      service/        비즈니스 로직
    src/test/java/com/devlog/
      controller/     HTTP, 검증, 예외 처리 테스트
      service/        service 계층 테스트
  frontend/
    package.json
    package-lock.json
    vite.config.ts
    tsconfig.json
    src/
      api/            Axios API 호출
      components/     공통 레이아웃 컴포넌트
      pages/          라우팅되는 화면
      types/          API/화면 타입
      test/           Vitest 설정
  docs/harness/       AI 작업 하네스와 MVP 기준 문서
```

## Domain Model

- `Project`: 프로젝트 이름과 설명을 관리합니다.
- `WorkLog`: 날짜별 업무 제목, 진행 상태, 메모, 관련 프로젝트를 관리합니다.
- `Issue`: 이슈 제목, 내용, 원인, 해결 방법, 상태, 관련 프로젝트와 관련 업무 일지를 관리합니다.
- `Todo`: 날짜별 할 일, 완료 여부, 관련 프로젝트를 관리합니다.
- `BaseEntity`: 생성일과 수정일을 공통 관리합니다.

## Main Features

- 프로젝트 등록, 조회, 수정, 삭제
- 날짜와 프로젝트 기준 업무 일지 관리
- 상태와 키워드 기준 이슈 관리
- 날짜별 할 일 관리와 완료 체크
- 시작일/종료일 기준 주간보고 텍스트 생성
- 대시보드에서 오늘 업무, 열린 이슈, 오늘 할 일, 최근 이슈 확인

## API Summary

```text
GET    /api/projects
POST   /api/projects
GET    /api/projects/{id}
PATCH  /api/projects/{id}
DELETE /api/projects/{id}

GET    /api/work-logs
POST   /api/work-logs
GET    /api/work-logs/{id}
PATCH  /api/work-logs/{id}
DELETE /api/work-logs/{id}

GET    /api/issues
POST   /api/issues
GET    /api/issues/{id}
PATCH  /api/issues/{id}
DELETE /api/issues/{id}

GET    /api/todos
POST   /api/todos
PATCH  /api/todos/{id}
DELETE /api/todos/{id}

GET    /api/reports/weekly?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
```

## Local Run

백엔드와 프론트엔드는 각각 별도 터미널에서 실행합니다.

### Backend

```powershell
cd backend
.\gradlew.bat bootRun
```

백엔드는 기본적으로 `http://localhost:8080`에서 실행됩니다. H2 콘솔은 `http://localhost:8080/h2-console`에서 사용할 수 있습니다.

H2 접속 정보:

```text
JDBC URL: jdbc:h2:mem:devlog;MODE=MySQL;DATABASE_TO_UPPER=false
User Name: sa
Password: <empty>
```

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

프론트엔드는 Vite 개발 서버에서 실행됩니다. `/api` 요청은 `frontend/vite.config.ts`의 proxy 설정을 통해 `http://localhost:8080`으로 전달됩니다.

## Build And Test

### Frontend

```powershell
cd frontend
npm install
npm test
npm run build
```

### Backend

```powershell
cd backend
.\gradlew.bat test
.\gradlew.bat build
```

## Development Notes

- Entity는 Controller에서 직접 반환하지 않고 Response DTO로 변환합니다.
- Request DTO와 Response DTO는 분리합니다.
- Controller는 요청/응답 경계를 담당하고, 비즈니스 로직은 Service 계층에 둡니다.
- 생성일/수정일은 `BaseEntity`에서 관리합니다.
- 초기 삭제는 물리 삭제로 구현하며, soft delete는 추후 필요할 때 확장합니다.
- UI 라이브러리는 사용하지 않고 일반 CSS로 MVP 화면을 구성합니다.
- 백엔드 Service 계층의 핵심 로직은 테스트 코드로 검증합니다.
- 예상하지 못한 테스트, 빌드, 검증 실패를 수정했다면 `docs/harness/08-failure-fix-log.md`에 기록합니다.

## Phase 5 Checklist

5단계 마무리는 `docs/harness/10-phase-5-checklist.md`를 기준으로 점검합니다. 주요 확인 항목은 다음과 같습니다.

- 예외 처리 응답 형식과 상태 코드 일관성
- Create/Update DTO 입력값 검증
- Service 테스트와 프론트엔드 API/화면 테스트
- 백엔드/프론트엔드 빌드와 테스트 명령 통과
- README와 하네스 문서 최신성

## Reference Documents

- `docs/harness/00-harness-index.md`: AI 작업 하네스 진입 문서
- `docs/harness/09-devlog-mvp-spec.md`: MVP 기능, API, Entity, 구현 단계 기준
- `docs/harness/10-phase-5-checklist.md`: 5단계 완료 점검 체크리스트
