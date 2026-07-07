# 기능 스펙 템플릿

새 기능을 구현하기 전에 이 템플릿을 복사해 기능 스펙을 작성한다. 확정되지 않은 항목은 구현 전에 사용자와 정리한다.

## Feature Name

[기능 이름]

## Goal

[이 기능이 해결할 사용자 문제를 한 문장으로 작성한다.]

## Success Criteria

- [성공 기준 1]
- [성공 기준 2]
- [성공 기준 3]

## In Scope

- [이번 작업에 포함되는 동작]

## Out of Scope

- [이번 작업에서 하지 않을 일]

## API And DTO Changes

- 새 API: [HTTP method] [path]
- 요청 DTO: [생성 또는 수정할 DTO]
- 응답 DTO: [생성 또는 수정할 DTO]
- 에러 응답: [기존 예외 처리 사용 또는 새 예외 필요 여부]

## Data And Domain Changes

- 도메인 변경: [엔티티, enum, 필드 변경]
- 저장소 변경: [Repository 추가 또는 쿼리 메서드]
- 마이그레이션: [현재는 별도 마이그레이션 체계 없음. 필요한 경우 구조 예외로 판단]

## File Structure Impact

- 생성 파일:
  - `[path]`: [책임]
- 수정 파일:
  - `[path]`: [변경 이유]
- 구조 계약 영향:
  - [없음 또는 `02-file-structure-contract.md` 갱신 필요]

## Verification Scenarios

- [정상 시나리오]
- [검증 실패 또는 잘못된 입력 시나리오]
- [없는 리소스 접근 시나리오]

## TDD Test Plan

- RED 테스트:
  - `[test path]`: [먼저 실패해야 하는 동작과 기대 실패 이유]
- GREEN 최소 구현:
  - [테스트를 통과시키기 위해 필요한 최소 변경]
- 리팩터링 후보:
  - [테스트 통과 후 정리할 수 있는 중복, 이름, 책임]
- 테스트 제외 또는 대체 검증:
  - [테스트가 어렵다면 이유와 대체 검증 방법. 없으면 "없음"]

## Open Questions

- [구현 전 반드시 결정해야 할 질문]
