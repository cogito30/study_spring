# 04 회원 관리 예제 - 백엔드 개발

## 비즈니스 요구사항 정리
- 데이터: 회원ID, 이름
- 기능: 회원 등록, 조회
- 상황: 데이터 저장소가 선정되지 않음

(일반적인 웹 애플리케이션 계층 구조)    
```
Controller -> Service -> Repository -> DB
    |           |           |
    ------------|------------
              Domain
```
- Controller: 웹 MVC의 컨트롤러 역할
- Service: 핵심 비즈니스 로직 구현
- Repository: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- Domain: 비즈니스 도메인 객체. ex) 회원, 주문, 쿠폰 등 주로 데이터베이스에 저장하고 관리됨

(클래스 의존관계)    
```
MemberService -> <interface>MemberRepository
                            |
                    MemoryMemberRepository
```
- 데이터 저장소가 선정되지 않았기에 인터페이스로 구현 클래스를 변경할 수 있도록 설계
- 데이터 저장소는 RDB, NoSQL 등 다양한 저장소를 고민중인 상황으로 가정
- 개발을 진행하기 위해 초기 단계에서 구현체로 가벼운 메모리 기반의 데이터 저장소 사용

## 회원 도메인과 리포지토리 만들기
1) src > main > java > hello.hellospring.domain(package) 생성
2) src > main > java > hello.hellospring.domain에 Member(class) 생성
3) src > main > java > hello.hellospring.repository(package) 생성
4) src > main > java > hello.hellospring.repository에 MemberRepository(interface) 생성
5) src > main > java > hello.hellospring.repository에 MemoryMemberRepository(class) 생성

+) 구체 클래스에서 opt + enter로 인터페이스 메서드 불러오기

## 회원 리포지토리 테스트 케이스 작성
1) test > java > hello.hellospring.repository(package) 생성
2) test > java > hello.hellospring.repository에 MemoryMemberRepositoryTest(class) 생성
- 테스트 실행 순서는 보장되지 않음. 테스트마다 따로 실행됨
- 테스트별로 영향을 받지 않게 하기 위해 테스트가 끝날 때마다 데이터 초기화 필요
3) src > main > java > hello.hellospring.repository의 MemoryMemberRepository에 코드 추가
4) test > java > hello.hellospring.repository의 MemoryMemberRepositoryTest에 @AfterEach 추가

- Test를 작성후 코드를 구현할 수도 있음. 이를 TDD라고 함

## 회원 서비스 개발
1) src > main > java > hello.hellospring.service(package) 생성
2) src > main > java > hello.hellospring.service에 MemberService(class) 생성
+) ctrl + T로 리팩토링(Extract Method로 선택) -> validateDuplicatemember로 지정
- 서비스 개발시 이름은 비즈니스에 가까운 용어 사용 

## 회원 서비스 테스트
1) src > main > java > hello.hellospring.service의 MemberService(class)에서 cmd + shift + T로 테스트할 멤버 선택후 테스트 생성(test > java > hello.hellospring.service에 MemberServiceTest 생성)
2) src > main > java > hello.hellospring.service의 MemberService(class) 수정(DI 적용)
+) cmd + opt + v로 변수명 자동 완성
+) ctrl + r로 이전 그대로 재실행

(정리)
- Controller: 요청을 받기
- Service: 비즈니스 규칙 구현
- Repository: 데이터 접근 담당
- Dependency Injection: 서비스 객체가 자신이 의존하는 리포지토리 객체의 구현체를 직접 생성하지 않고, 외부에서 전달받아 사용한느 설계 방식