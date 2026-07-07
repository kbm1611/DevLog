# 파일 구조 계약

이 문서는 DevLog 백엔드의 허용된 파일 구조를 계약처럼 정의한다. 작업자는 이 구조 안에서 변경해야 한다.

## Root Structure

```text
DevLog/
  AGENTS.md
  backend/
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
            service/
```

## Allowed Package Responsibilities

- `controller`: HTTP 요청과 응답 경계를 담당한다. 비즈니스 규칙을 직접 구현하지 않는다.
- `service`: 유스케이스와 비즈니스 흐름을 담당한다.
- `repository`: Spring Data JPA 저장소 인터페이스를 둔다.
- `domain`: JPA 엔티티, 도메인 enum, 도메인 기본 타입을 둔다.
- `dto`: API 요청과 응답 DTO를 둔다. 기능별 하위 패키지를 사용할 수 있다.
- `exception`: 공통 예외, 에러 응답, 전역 예외 처리를 둔다.

## File Addition Rules

- 새 API는 기존 기능 단위와 같은 이름 규칙을 따른다.
- 새 도메인 기능은 필요에 따라 `domain`, `repository`, `service`, `controller`, `dto/<feature>`에 추가한다.
- 테스트는 기본적으로 `backend/src/test/java/com/devlog/service` 아래 서비스 테스트로 시작한다.
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
- `com.devlog` 아래 새 최상위 패키지 추가.
- 기존 계층형 구조를 도메인별 구조로 재편.
- 빌드 도구, Java 버전, Spring Boot 주 버전 변경.

구조 예외가 필요하면 사용자 승인 후 `07-decision-log.md`에 날짜, 결정, 이유, 영향, 검증 방법을 기록한다.
