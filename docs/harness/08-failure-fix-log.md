# 실패 수정 기록

이 문서는 예상하지 못한 테스트, 빌드, 검증 실패를 수정한 이력을 기록한다. 최신 항목이 위에 오도록 추가한다.

TDD의 의도된 RED 실패는 매번 기록하지 않는다. 다만 RED 단계에서 실제 버그, 잘못된 요구사항, 잘못된 설계가 드러나 수정했다면 기록한다.

## Entry Template

```markdown
## YYYY-MM-DD - [실패 수정 제목]

- 실패 유형: [테스트 실패 | 빌드 실패 | 검증 실패 | 런타임 오류 | 기타]
- 증상:
- 원인:
- 수정:
- 재발 방지:
- 검증:
- 관련 파일:
```

## 기록 기준

- 테스트나 빌드가 예상과 다르게 실패했고, 그 원인을 수정했다.
- 사용자에게 보고할 만한 검증 실패를 수정했다.
- 같은 실패가 반복되어 원인 추적이 필요하다.
- 실패 수정 과정에서 테스트, 스펙, 구현 계획, 하네스 규칙이 보강되었다.

## 기록 제외 기준

- TDD 과정에서 의도한 RED 실패가 기대한 이유로 발생했고, 계획대로 GREEN 단계에서 통과했다.
- 문서 오탈자처럼 검증 실패와 연결되지 않는 단순 수정이다.
- 실패가 현재 작업과 무관하고 수정하지 않았다.

## 2026-07-07 - Gradle test/build 병렬 실행 경합

- 실패 유형: 검증 실패
- 증상: `backend\gradlew.bat test`와 `backend\gradlew.bat build`를 동시에 실행하자 `build/test-results/test/binary/output.bin` 삭제 실패로 `test` 태스크가 실패했다.
- 원인: 두 Gradle 검증 명령이 같은 테스트 결과 디렉터리를 동시에 사용했다.
- 수정: 전체 테스트를 단독으로 재실행했다.
- 재발 방지: Gradle의 `test`와 `build` 검증은 병렬이 아니라 순차 실행한다.
- 검증: `backend\gradlew.bat test --rerun-tasks`
- 관련 파일: 없음

## 2026-07-07 - Controller MVC 예외 처리 테스트 보강 중 실패 수정

- 실패 유형: 테스트 실패
- 증상: 새 `ControllerExceptionHandlingTest` 실행 시 `@WebMvcTest` 컨텍스트가 JPA Auditing의 `jpaMappingContext` 생성 문제로 로드되지 않았고, 이후 invalid enum 요청은 HTTP 400이지만 공통 `ErrorResponse` JSON을 반환하지 않았다.
- 원인: MVC 슬라이스 테스트에서 JPA metamodel이 없는 상태로 auditing 설정이 로드되었고, `HttpMessageNotReadableException`을 공통 예외 처리에서 다루지 않았다.
- 수정: 테스트에서 `JpaMetamodelMappingContext`를 `@MockitoBean`으로 대체하고, `GlobalExceptionHandler`에 `HttpMessageNotReadableException` 처리기를 추가했다.
- 재발 방지: Project, WorkLog, Issue, Todo, WeeklyReport의 Controller 경계에서 404, Bean Validation 400, unreadable JSON 400, IllegalArgumentException 400 응답 형식을 검증하는 MVC 테스트를 추가했다.
- 검증: `backend\gradlew.bat test --tests com.devlog.controller.ControllerExceptionHandlingTest`
- 관련 파일: `backend/src/test/java/com/devlog/controller/ControllerExceptionHandlingTest.java`, `backend/src/main/java/com/devlog/exception/GlobalExceptionHandler.java`

## 2026-07-07 - Gradle wrapper lock 접근 거부

- 실패 유형: 검증 실패
- 증상: sandbox 환경에서 `backend\gradlew.bat test` 실행 시 `gradle-8.13-bin.zip.lck` 파일 접근이 거부되어 Gradle wrapper가 시작되지 않았다.
- 원인: 프로젝트 코드나 테스트 실패가 아니라 사용자 홈 디렉터리 아래 Gradle wrapper distribution lock 파일에 대한 sandbox 접근 제한 때문이다.
- 수정: 동일한 `backend\gradlew.bat test` 명령을 권한 상승으로 재실행해 테스트 검증을 완료했다. 코드 변경은 필요하지 않았다.
- 재발 방지: sandbox에서 같은 lock 접근 오류가 발생하면 코드 실패로 판단하지 않고 동일 명령을 권한 상승으로 재실행한다.
- 검증: `backend\gradlew.bat test`, `backend\gradlew.bat build`
- 관련 파일: 없음
