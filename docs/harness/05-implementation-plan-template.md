# 구현 계획 템플릿

기능 스펙이 확정된 뒤 이 템플릿을 복사해 구현 계획을 작성한다.

## Summary

- 목표:
- 기준 스펙:
- 기준 MVP 항목: `09-devlog-mvp-spec.md`의 [섹션 또는 단계]
- 변경 범위:
- 구현 단계: [1단계 | 2단계 | 3단계 | 4단계 | 5단계 | 기타]

## Files

- 생성:
  - `[path]`: [책임]
- 수정:
  - `[path]`: [변경 내용]
- 테스트:
  - `[path]`: [검증 내용]

## TDD Cycle

- RED:
  - 먼저 추가할 테스트:
  - 기대 실패 이유:
  - 실행 명령:
- GREEN:
  - 최소 구현 범위:
  - 통과 확인 명령:
- REFACTOR:
  - 테스트 통과 후 정리할 항목:
  - 리팩터링 후 재실행 명령:

## Steps

1. 현재 관련 구현과 테스트를 확인한다.
2. RED 테스트를 추가한다.
3. RED 테스트가 기대한 이유로 실패하는지 실행 결과를 확인한다.
4. 최소 구현으로 테스트를 통과시킨다.
5. 필요한 경우 테스트가 계속 통과하는 상태에서만 리팩터링한다.
6. 컨트롤러, 서비스, DTO, 도메인 책임이 구조 계약과 맞는지 확인한다.
7. 전체 빌드를 실행한다.
8. 하네스 문서 갱신 필요 여부를 확인한다.

## Verification Commands

```powershell
cd backend
.\gradlew.bat build
```

## Expected Results

- RED 단계에서 테스트가 기대한 이유로 실패한다.
- GREEN 단계에서 같은 테스트가 통과한다.
- Gradle build succeeds.
- All tests pass.
- No package or file violates `02-file-structure-contract.md`.
- Documentation changes are reflected in the harness if structure or workflow changed.

## Rollback Criteria

- 빌드 실패 원인이 현재 변경에서 발생했고 즉시 수정할 수 없다.
- 구조 계약을 위반하는 변경이 사용자 승인 없이 필요하다.
- 기능 스펙의 성공 기준을 충족할 수 없다.
