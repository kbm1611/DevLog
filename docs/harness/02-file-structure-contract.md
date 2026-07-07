# 파일 구조 계약

이 문서는 DevLog 백엔드의 허용된 파일 구조를 계약처럼 정의한다. 작업자는 이 구조 안에서 변경해야 한다.

## Root Structure

```text
DevLog/
  AGENTS.md
  README.md
  .gitignore
  backend/
  frontend/
  docs/
    harness/
```

## Backend Structure

```text
backend/
  build.gradle
  settings.gradle
  gradlew
  gradlew.bat
  gradle/
  src/
    main/
      java/
        com/
          devlog/
            DevLogApplication.java
            controller/
            domain/
            dto/
            exception/
            repository/
            service/
      resources/
        application.yml
    test/
      java/
        com/
          devlog/
            DatabaseConfigurationTest.java
            controller/
            service/
```

## Frontend Structure

```text
frontend/
  index.html
  package.json
  package-lock.json
  tsconfig.json
  vite.config.ts
  src/
    App.tsx
    App.test.tsx
    main.tsx
    styles.css
    api/
      devlogApi.ts
      devlogApi.test.ts
    components/
    pages/
    types/
    test/
```

## Allowed Package Responsibilities

- `controller`: HTTP 요청과 응답 경계를 담당한다. 비즈니스 규칙을 직접 구현하지 않는다.
- `service`: 유스케이스와 비즈니스 흐름을 담당한다.
- `repository`: Spring Data JPA 저장소 인터페이스를 둔다.
- `domain`: JPA 엔티티, `BaseEntity`, 도메인 enum, 도메인 기본 타입을 둔다.
- `dto`: API 요청과 응답 DTO를 둔다. Request와 Response는 분리하고, 기능별 하위 패키지를 사용할 수 있다.
- `exception`: 공통 예외, 에러 응답, 전역 예외 처리를 둔다.
- `frontend/src/api`: Axios 기반 API 호출 코드를 둔다.
- `frontend/src/components`: 여러 화면에서 재사용하는 UI 컴포넌트를 둔다.
- `frontend/src/pages`: 라우팅되는 페이지 단위 컴포넌트를 둔다.
- `frontend/src/types`: API 응답, 요청, 화면 상태 타입을 둔다.
- `frontend/src/test`: Vitest와 React Testing Library 공통 테스트 설정을 둔다.

## File Addition Rules

- 새 API는 기존 기능 단위와 같은 이름 규칙을 따른다.
- 새 도메인 기능은 필요에 따라 `domain`, `repository`, `service`, `controller`, `dto/<feature>`에 추가한다.
- Entity는 Controller에서 직접 반환하지 않고 Response DTO로 변환한다.
- 생성일/수정일 공통 필드는 `BaseEntity`로 분리한다.
- 날짜 타입은 `LocalDate`, 날짜시간 타입은 `LocalDateTime`을 사용한다.
- 서비스 로직 테스트는 기본적으로 `backend/src/test/java/com/devlog/service` 아래에서 시작한다.
- HTTP 요청/응답, 예외 처리, 입력값 검증 실패 테스트는 `backend/src/test/java/com/devlog/controller` 아래에 둔다.
- 애플리케이션 설정과 Spring profile 검증 테스트는 `backend/src/test/java/com/devlog` 아래에 둘 수 있다.
- 프론트엔드 기능은 `frontend/src` 아래에서 `api`, `components`, `pages`, `types` 책임에 맞춰 추가한다.
- 프론트엔드 테스트 파일은 검증 대상과 가까운 `frontend/src` 하위에 `*.test.ts` 또는 `*.test.tsx`로 둔다.
- 프론트엔드 테스트 유틸리티는 `frontend/src/test` 아래에 둔다.
- 새 패키지는 기존 책임으로 설명할 수 없을 때만 추가한다.

## Package Move Rules

- 기존 파일 이동은 공개 API, 테스트, import 영향이 크므로 기본적으로 금지한다.
- 파일 이동이 필요하면 구현 전에 다음을 수행한다.
  1. 이동 이유를 `07-decision-log.md`에 기록한다.
  2. `Backend Structure` 섹션을 갱신한다.
  3. 검증 계획에 전체 빌드 명령을 포함한다.

## Exception Procedure

다음 변경은 구조 예외로 본다.

- `backend` 밖 새 실행 모듈 추가.
- `frontend` 밖 새 실행 모듈 추가.
- `com.devlog` 아래 새 최상위 패키지 추가.
- 기존 계층형 구조를 도메인별 구조로 재편.
- 빌드 도구, Java 버전, Spring Boot 주 버전 변경.

구조 예외가 필요하면 사용자 승인 후 `07-decision-log.md`에 날짜, 결정, 이유, 영향, 검증 방법을 기록한다.
