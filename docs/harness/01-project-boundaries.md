# 프로젝트 경계

이 문서는 DevLog 저장소의 현재 작업 범위와 변경 금지 원칙을 정의한다.

## Project Purpose

DevLog는 개발 작업 기록, 프로젝트, 이슈, 할 일, 주간 리포트를 관리하는 백엔드 중심 애플리케이션이다. 현재 저장소의 핵심 구현은 `backend` 디렉터리 아래 Spring Boot 애플리케이션이다.

## Current Scope

- 백엔드 애플리케이션 유지보수와 기능 추가.
- REST 컨트롤러, 서비스, 저장소, 도메인, DTO, 예외 처리 계층 유지.
- Java 17, Spring Boot, Gradle 기반 개발 흐름 유지.
- 현재 테스트 구조를 이용한 서비스 테스트와 빌드 검증.

## Out of Scope By Default

사용자가 명시적으로 요청하지 않는 한 다음 작업은 범위 밖이다.

- 프론트엔드 애플리케이션 생성.
- 모바일 앱, 데스크톱 앱, 별도 CLI 생성.
- Spring Boot 외 프레임워크로 전환.
- Gradle에서 다른 빌드 도구로 전환.
- 데이터베이스 영속성 전략의 대규모 교체.
- 패키지 구조 전체 재편.
- 인증, 배포, 인프라, 모니터링 도입.

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
