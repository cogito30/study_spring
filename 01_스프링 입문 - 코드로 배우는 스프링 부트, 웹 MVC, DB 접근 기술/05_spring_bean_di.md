# 05 스프링 빈과 의존관계

## 컴포넌트 스캔과 자동 의존관계 설정
- Controller와 View Template가 필요
- MemberController는 MemberService를 통해서 회원가입과 데이터를 조회할 수 있어야 함(의존관계)
1) src > main > java > hello.hellospring.controller에 MemberController(class) 생성
- spring container에 @Controller가 붙은 클래스 객체를 만들어서 스프링에서 관리(빈 관리)
- 스프링 컨테이너에서 객체를 받아서 사용하도록 코드 구현을 추천
- 생성자에 @Autowired가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌.(DI: 의존성 주입)
2) src > main > java > hello.hellospring.controller의 MemberService(class)에 @Service, @Autowired 코드 추가
- @Service로 Spring Container에 등록
3) src > main > java > hello.hellospring.repository의 MemoryMemberRepository(class)에 @Repository 코드 추가
- @Repository로 Spring Container에 등록

- 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본적으로 싱글톤으로 등록
- @Component는 springboot 패키지내 하위의 파일들만 가능

(스프링 빈을 등록하는 2가지 방법)
1) 컴포넌트 스캔과 자동 의존관계 설정: @Controller, @Service, @Repository내에 @Component가 포함
2) 자바 코드로 직접 스프링 빈 등록하기

## 자바 코드로 직접 스프링 빈 등록하기
1) src > main > java > hello.hellospring.controller의 MemberService(class)에서 @Autowired, @Service 제거
2) src > main > java > hello.hellospring.repository의 MemoryMemberRepository(class)에서 @Repository 제거
3) src > main > java > hello.hellospring엥 SpringConfig(class) 생성

- 과거에는 XML을 사용하였지만 현재는 java 코드로 설정하는 추세
- DI에는 필드 주입, setter 주입, **생성자 주입** 3가지 방법이 존재. 의존관계가 실행중에 동적으로 변경하는 경우는 거의 없으므로 생성자 주입을 권장
- setter 주입의 경우 public으로 열려있어야 하기에 중간에 접근하는 단점이 존재
- 실무에서 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용. 정형화되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록
- `@Autowired`를 통한 DI는 스프링이 관리하는 객체에서만 동작(반드시 스프링 빈으로 등록)