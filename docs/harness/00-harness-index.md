# AI 작업 하네스 인덱스

이 디렉터리는 DevLog 저장소에서 AI 작업자가 따라야 할 문서 기반 하네스다. 작업자는 요청을 수행하기 전에 이 문서를 시작점으로 삼아야 한다.

## Required Reading Order

1. `AGENTS.md`
2. `docs/harness/00-harness-index.md`
3. `docs/harness/01-project-boundaries.md`
4. `docs/harness/02-file-structure-contract.md`
5. `docs/harness/03-workflow-contract.md`
6. 요청 유형에 따라 필요한 템플릿 또는 체크리스트

## Harness Documents

- `01-project-boundaries.md`: 프로젝트 목적, 현재 범위, 하지 않을 일, 변경 금지 원칙.
- `02-file-structure-contract.md`: 허용된 디렉터리와 패키지 구조, 새 파일 추가 규칙, 구조 변경 예외 절차.
- `03-workflow-contract.md`: AI 작업자가 따라야 하는 요청 처리, 계획, 구현, 검증 흐름.
- `04-feature-spec-template.md`: 기능 추가 전 작성하는 스펙 템플릿.
- `05-implementation-plan-template.md`: 구현 전 작성하는 작업 계획 템플릿.
- `06-verification-checklist.md`: 완료 전 확인해야 하는 검증 체크리스트.
- `07-decision-log.md`: 구조 변경, 규칙 변경, 예외 승인 기록.

## Standard Work Flow

1. 요청 이해: 사용자의 목표, 성공 기준, 제외 범위를 파악한다.
2. 관련 문서 확인: 요청이 영향을 주는 하네스 문서를 읽는다.
3. 범위 판정: 기존 구조 안에서 해결 가능한지 판단한다.
4. 계획 작성: 변경 파일, 작업 순서, 검증 명령을 먼저 정리한다.
5. 구현: 계획된 범위 안에서만 파일을 수정한다.
6. 검증: 테스트, 빌드, 문서 체크리스트를 실행한다.
7. 문서 갱신 여부 확인: 구조나 규칙이 바뀌면 하네스 문서를 갱신한다.

## Stop Conditions

다음 상황에서는 구현을 멈추고 사용자 확인을 받아야 한다.

- 현재 문서 계약과 충돌하는 구조 변경이 필요하다.
- 새 최상위 모듈, 새 기술 스택, 새 실행 환경이 필요하다.
- 검증 명령이 실패했고 실패 원인이 현재 작업 때문인지 분명하지 않다.
- 사용자의 요청 범위가 여러 독립 기능으로 나뉘어야 한다.
