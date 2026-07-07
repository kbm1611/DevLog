# AI 작업 하네스 인덱스

이 디렉터리는 DevLog 저장소에서 AI 작업자가 따라야 하는 문서 기반 하네스다. 작업자는 요청을 수행하기 전에 이 문서를 시작점으로 읽어야 한다.

## Project Identity

- 프로젝트명: DevLog
- 프로젝트 성격: 바이브 프로젝트
- 중심 프로젝트: `backend`
- 제품 기준: `docs/harness/09-devlog-mvp-spec.md`
- 기본 개발 방식: TDD
- 기본 검증 명령: `backend\gradlew.bat build`

바이브 프로젝트이므로 빠른 실험과 반복을 허용한다. 다만 코드 변경은 실패 테스트를 먼저 확인하는 TDD 흐름과 하네스 문서의 구조 계약 안에서 진행한다.

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
- `03-workflow-contract.md`: AI 작업자가 따라야 하는 요청 처리, TDD, 계획, 구현, 검증 흐름.
- `04-feature-spec-template.md`: 기능 추가 전 작성하는 스펙 템플릿.
- `05-implementation-plan-template.md`: 구현 전 작성하는 작업 계획 템플릿.
- `06-verification-checklist.md`: 완료 전 확인해야 하는 검증 체크리스트.
- `07-decision-log.md`: 구조 변경, 규칙 변경, 예외 승인 기록.
- `08-failure-fix-log.md`: 실패한 테스트, 빌드, 검증을 수정한 이력 기록.
- `09-devlog-mvp-spec.md`: DevLog MVP 기능, API, Entity, 기술 기준, 구현 단계 기준.
- `10-phase-5-checklist.md`: 예외 처리, 입력값 검증, 테스트 보강, README 작성을 점검하는 5단계 체크리스트.

## Standard Work Flow

1. 요청 이해: 사용자의 목표, 성공 기준, 제외 범위를 파악한다.
2. 관련 문서 확인: 요청에 영향을 주는 하네스 문서를 읽는다.
3. 제품 기준 확인: MVP 기능, API, Entity, 단계 계획이 관련되면 `09-devlog-mvp-spec.md`를 확인한다.
4. 범위 판정: 기존 구조 안에서 해결 가능한지 판단한다.
5. 계획 작성: 변경 파일, 작업 순서, 먼저 실패해야 하는 테스트, 검증 명령을 정리한다.
6. RED: 코드 변경 전 실패 테스트를 먼저 작성하고, 기대한 이유로 실패하는지 확인한다.
7. GREEN: 테스트를 통과시키는 최소 구현만 수행한다.
8. REFACTOR: 테스트가 통과한 뒤에만 중복 제거와 이름 정리를 수행한다.
9. 검증: 테스트, 빌드, 문서 체크리스트를 실행한다.
10. 5단계 점검: MVP 마무리 작업이면 `10-phase-5-checklist.md`를 확인한다.
11. 실패 수정 기록 확인: 예상하지 못한 테스트, 빌드, 검증 실패를 수정했다면 `08-failure-fix-log.md`에 기록한다.
12. 문서 갱신 여부 확인: 구조나 규칙이 바뀌면 하네스 문서를 갱신한다.

## Documentation-Only Work Flow

1. 문서의 독자와 목적을 확인한다.
2. 기존 문서 구조와 링크를 확인한다.
3. 하네스 흐름에서 연결되는 위치를 명확히 한다.
4. 코드 변경 없이 문서만 수정한다.
5. 링크, 목차, 경로가 실제 파일 구조와 맞는지 확인한다.

## Stop Conditions

다음 상황에서는 구현을 멈추고 사용자 확인을 받아야 한다.

- 현재 문서 계약과 충돌하는 구조 변경이 필요하다.
- 새 최상위 모듈, 새 기술 스택, 새 실행 환경이 필요하다.
- 검증 명령이 실패했고 실패 원인이 현재 작업 때문인지 분명하지 않다.
- 사용자의 요청 범위가 여러 독립 기능으로 분해되어야 한다.
- TDD 테스트를 작성할 수 없고 대체 검증 방법도 명확하지 않다.
