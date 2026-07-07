# 프로젝트 경계

이 문서는 DevLog 저장소의 현재 작업 범위와 변경 금지 원칙을 정의한다.

## Project Purpose

DevLog는 신입 개발자가 SI/공공기관 프로젝트 업무를 하면서 매일 작성하는 업무 일지, 이슈, 해결 방법, 내일 할 일을 체계적으로 관리하고 주간보고 형태로 정리할 수 있도록 돕는 개인용 웹 애플리케이션이다.

이 프로젝트는 배포용 서비스가 아니라 로컬에서 개인이 사용하는 실무 보조 도구다. 처음부터 인증, 권한, 다중 사용자, 클라우드 배포는 고려하지 않는다.

DevLog는 바이브 프로젝트다. AI 작업자는 빠른 실험 흐름을 지원하되, 저장소 구조와 검증 기준을 흐리지 않도록 하네스 문서와 TDD 절차를 우선한다.

## Current Scope

- 백엔드 애플리케이션 유지보수와 기능 추가.
- React 프론트엔드 애플리케이션 유지보수와 MVP 화면 구현.
- REST 컨트롤러, 서비스, 저장소, 도메인, DTO, 예외 처리 계층 유지.
- Java 17, Spring Boot, H2, Gradle 기반 개발 흐름 유지.
- React, Vite, Axios, 일반 CSS 또는 CSS Module 기반 개발 흐름 유지.
- 현재 테스트 구조를 이용한 서비스 테스트와 빌드 검증.
- TDD 기반 기능 추가와 버그 수정.
- MVP 기준은 `09-devlog-mvp-spec.md`를 따른다.

## Out of Scope By Default

사용자가 명시적으로 요청하지 않는 한 다음 작업은 범위 밖이다.

- 현재 React/Vite 앱 외 별도 프론트엔드 애플리케이션 생성.
- 모바일 앱, 데스크톱 앱, 별도 CLI 생성.
- Spring Boot 외 프레임워크로 전환.
- Gradle에서 다른 빌드 도구로 전환.
- 데이터베이스 영속성 전략의 대규모 교체.
- 초기 단계에서 MySQL을 기본 DB로 사용하는 변경.
- UI 라이브러리 도입.
- 패키지 구조 전체 재편.
- 인증, 배포, 인프라, 모니터링 도입.
- 로그인, JWT, OAuth, 다중 사용자, Docker, CI/CD, MSA 도입.

## Change Control

- 현재 계층형 구조를 기본값으로 유지한다.
- 새 기능은 기존 계층과 이름 규칙에 맞춰 추가한다.
- 구조 변경은 기능 구현의 부수 효과로 처리하지 않는다.
- 구조 변경이 필요하면 먼저 `02-file-structure-contract.md`와 `07-decision-log.md`를 갱신한다.

## Default Verification

백엔드 변경 후 기본 검증 명령은 다음과 같다.

```powershell
cd backend
.\gradlew.bat build
```

프론트엔드 변경 후 기본 검증 명령은 다음과 같다.

```powershell
cd frontend
npm run build
```
