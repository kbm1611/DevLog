# 구현 계획 템플릿

기능 스펙이 확정된 뒤 이 템플릿을 복사해 구현 계획을 작성한다.

## Summary

- 목표:
- 기준 스펙:
- 변경 범위:

## Files

- 생성:
  - `[path]`: [책임]
- 수정:
  - `[path]`: [변경 내용]
- 테스트:
  - `[path]`: [검증 내용]

## Steps

1. 현재 관련 구현과 테스트를 확인한다.
2. 실패해야 하는 테스트 또는 검증 시나리오를 먼저 추가한다.
3. 최소 구현으로 테스트를 통과시킨다.
4. 컨트롤러, 서비스, DTO, 도메인 책임이 구조 계약과 맞는지 확인한다.
5. 전체 빌드를 실행한다.
6. 하네스 문서 갱신 필요 여부를 확인한다.

## Verification Commands

```powershell
cd backend
.\gradlew.bat build
```

## Expected Results

- Gradle build succeeds.
- All tests pass.
- No package or file violates `02-file-structure-contract.md`.
- Documentation changes are reflected in the harness if structure or workflow changed.

## Rollback Criteria

- 빌드 실패 원인이 현재 변경에서 발생했고 즉시 수정할 수 없다.
- 구조 계약을 위반하는 변경이 사용자 승인 없이 필요하다.
- 기능 스펙의 성공 기준을 충족할 수 없다.
