# DevLog Agent Instructions

이 저장소에서 작업하는 모든 AI 작업자는 코드나 문서를 수정하기 전에 이 파일을 먼저 읽고 따라야 한다.

## Mandatory Harness Entry

1. 작업 시작 전 `docs/harness/00-harness-index.md`를 읽는다.
2. 요청이 코드 변경을 포함하면 `docs/harness/01-project-boundaries.md`, `docs/harness/02-file-structure-contract.md`, `docs/harness/03-workflow-contract.md`를 함께 확인한다.
3. 새 기능은 `docs/harness/04-feature-spec-template.md` 형식의 스펙을 먼저 확정한 뒤 구현한다.
4. 구현 계획은 `docs/harness/05-implementation-plan-template.md` 형식을 따른다.
5. 완료 전 `docs/harness/06-verification-checklist.md`를 기준으로 검증한다.

## Non-Negotiable Rules

- 문서에 정의된 구조를 벗어나는 패키지, 디렉터리, 모듈을 임의로 만들지 않는다.
- 구조 변경이 필요하면 먼저 `docs/harness/02-file-structure-contract.md`를 갱신하고, 이유와 승인 근거를 `docs/harness/07-decision-log.md`에 기록한다.
- 사용자가 명시하지 않은 리팩터링, 기술 스택 교체, 대규모 구조 변경은 하지 않는다.
- 기존 사용자 변경사항을 되돌리지 않는다.
- 작업 완료를 주장하기 전에 검증 명령과 체크리스트 결과를 확인한다.

## Current Project Default

- 중심 프로젝트: `backend`
- 프로젝트 성격: 바이브 프로젝트
- Backend 기본 기술: Java 17, Spring Boot, Spring Web, Spring Data JPA, H2, Gradle
- Frontend 기본 기술: React, Vite, Axios, 일반 CSS 또는 CSS Module
- 기본 검증 명령: `backend\gradlew.bat build`, 프론트엔드 변경 시 `frontend\npm run build`
- 기본 개발 방식: TDD. 코드 변경은 실패하는 테스트를 먼저 작성하고, 실패 이유를 확인한 뒤 최소 구현으로 통과시킨다.
