# 03. 스프링 핵심 원리 이해 1 - 예제
- Java + Jar + SpringBoot + Gradle + IntelliJ

## 프로젝트 생성
1) [Spring Initializr](https://start.spring.io)로 Spring 프로젝트 생성
- Project(Gradle), Language(Java), SpringBoot(version)
- Project MetaData: Group, Artifact, Package Name, Packaginn, Java

2) IntelliJ 열기
- build.gradle 확인: plugins, group, version, sourceCompatibility, dependencies
- build.gradle 변경시 재로딩 필요

3) 웹 프로젝트 실행
- preference > gradle 설정을 intelliJ로 변경
- Run으로 실행

## 비즈니스 요구사항과 설계
- 기획자 제공 요구사항 분석
- 결정하기 어려운 부분은 인터페이스로 작성
```
회원
- 회원가입을 하고 조회할 수 있다.
- 회원은 일반과 VIP 두가지 등급이 있다.
- 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다.

주문과 할인 정책
- 회원은 상품을 주문할 수 있다.
- 회원등급에 따라 할인 정책을 적용할 수 있다.
- 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용해달라.(나중에 변경될 수 있다)
- 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수 있다.(미확정)
```

## 회원 도메인 설계
- 객체 다이어그램 작성
- UML 다이어그램 표기법

(회원 도메인 요구사항)
```
- 회원가입을 하고 조회할 수 있다.
- 회원은 일반과 VIP 두가지 등급이 있다.
- 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다.
```

(회원 도메인 협력 관계): 기획자도 함꼐 확인
```
클라이언트 -> 회원서비스(회원가입, 회원조회) -> 회원 저장소
                                        ^
                                        |
                        구현(메모리 || DB || 외부시스템) 회원 저장소
```

(회원 클래스 다이어그램): 개발자가 구체화. 실제 클래스 분석. 정적
```
<interface> MemberService
    |
MemberServiceImpl -> <interface>MemberRepository
                                ^
                                |
        MemoryMember Repository || DBMember Repository
```

(회원 객체 다이어그램): 실제 사용을 표시하는 다이어그램. 동적
```
클라이언트 -> 회원 서비스(MemberServiceImpl) -> 메모리 회원 저장소
```

## 회원 도메인 개발
- 회원 엔티티
1) src > main > java > hello.core.member(package) 생성
2) src > main > java > hello.core.member에 Grade(Enum) 생성
3) src > main > java > hello.core.member에 Member(Class) 생성
+) cmd + Enter로 생성자 선택
4) src > main > java > hello.core.member에 MemberRepository(Interface) 생성
5) src > main > java > hello.core.member에 MemoryMemberRepository(Class) 생성
+) opt + enter로 인터페이스 구현
6) src > main > java > hello.core.member에 MemberService(Interface) 생성
6) src > main > java > hello.core.member에 MemberServiceImpl(Class) 생성


## 회원 도메인 실행과 테스트
1) src > main> java > hello.core에 MemberApp(Class) 생성
+) cmd + opt + v 단축키로 변수명 생성

(Junit 사용)
1) src > test > java > hello.core.member에 MemberServiceTest(Class) 생성

(회원 도메일 설계의 문제점)
- OCP 원칙, DIP를 잘 지키고 있는지
- 의존관계가 인터페이스뿐만 아니라 구현까지 모두 의존하는 문제
- MemberServiceImpl은 추상화와 구체화 모두 의존

## 주문과 할인 도메인 설계
- 역할과 구현을 분리해서 자유롭게 구현 객체를 조립할 수 있게 설계

(주문과 할인 정책)
```
- 회원은 상품을 주문할 수 있다.
- 회원등급에 따라 할인 정책을 적용할 수 있다.
- 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용해달라.(나중에 변경될 수 있다)
- 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수 있다.(미확정)
```


(주문 도메인 협력, 역할, 책임)
```
        (1)           (2)|-> 회원 저장소 역할
클라이언트 -> 주문 서비스 역할 -|
        (4)           (3)|-> 할인 정책 역할
```
1) 주문 생성(회원id, 상품명, 상품가격): 클라이언트는 주문 서비스에 주문 생성을 요청
2) 회원 조회: 할인을 위해서는 회원 등급이 필요. 주문 서비스는 회원 저장소에서 회원을 조회
3) 할인 적용: 주문 서비스는 회원 등급에 따른 할인 여부를 할인 정책에 위임
4) 주문 결과 반환: 주문 서비스는 할인 결과를 포함한 주문 결과를 반환

(주문 도메인 전체)
```
        (1)           (2)|-> 회원 저장소 역할  <--- 메모리/DB 회원 저장소
클라이언트 -> 주문 서비스 역할 -|
        (4)     |     (3)|-> 할인 정책 역할 <--- 정액/정률 할인 정책
            주문 서비스 구현체
```

(주문 도메인 클래스 다이어그램)
```
<interface>OrderService
        |                   |-> <interface>MemberRepository <--- MemoryMemberRepository / DbMemberRepository 
    OrderServiceImpl --------
                            |-> <interface>DiscountPolicy <--- FixDiscountPolicy / RatDiscountPolicy
```

(주문 도메인 객체 다이어그램1)
```
                            |-> 메모리 회원 저장소
클라이언트 -> 주문 서비스 구현체 ---
                            |-> 정액 회원 저장소
```

(주문 도메인 객체 다이어그램2)
```
                            |-> DB 회원 저장소
클라이언트 -> 주문 서비스 구현체 ---
                            |-> 정률 회원 저장소
```

## 주문과 할인 도메인 개발
(할인)
1) src > main > java > hello.core.dicount(package) 생성
2) src > main > java > hello.core.dicount에 DiscountPolicy(interface) 생성
3) src > main > java > hello.core.dicount에 FixDicountPolicy(class) 생성

(주문)
1) src > main > java > hello.core.order(package) 생성
2) src > main > java > hello.core.order에 Order(class) 생성
3) src > main > java > hello.core.order에 OrderService(interface) 생성
4) src > main > java > hello.core.order에 OrderServiceImpl(class) 생성


## 주문과 할인 도메인 실행과 테스트
1) src > main > java > hello.core에 OrderApp(class) 생성
2) test > java > hello.core.order(package) 생성
3) test > java > hello.core.order에 OrderServiceTest(class) 생성