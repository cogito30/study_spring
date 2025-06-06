# 컴포넌트 스캔

## 컴포넌트 스캔과 의존관계 자동 주입 시작하기
- 스프링 빈 등록시 자바코드의 @Bean이나 XML의 <bean> 등을 통해 설정 정보에 직접 등록
- 설정 정보가 커질 경우 반복 등록의 문제가 발생
- 스프링에서는 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공
- 의존관계도 자동으로 주입하는 @Autowired 기능을 제공

1) src > main > java > hello.core에 AutoAppConfig(class) 생성
- 컴포넌트 스캔을 사용하려면 @ComponentScan을 설정 정보에 붙임
- 기존의 AppConfig와 다르게 @Bean으로 등록한 클래스가 없음
- 컴포넌트 스캔을 사용하면 @Configuration이 붙은 설정 정보도 자동으로 등록되므로 excludeFilters를 사용하여 제외
- 컴포넌트 스캔은 @Component가 붙은 클래스를 스캔해서 스프링 빈으로 등록
2) src > main > java > hello.core.member에 MemoryMemberRepository에 @Component를 붙임
3) src > main > java > hello.core.discount에 RateDicountPolicy에 @Component를 붙임
4) src > main > java > hello.core.member에 MemberServiceImpl에 @Component를 붙임
4) src > main > java > hello.core.member에 OrderServiceImpl에 @Component를 붙임
- 자동 의존관계 주입을 위해 생성자에 @Autowired를 붙임

- AppConfig에서는 @Bean으로 직접 설정 정보를 작성했고 의존관계도 직접 명시
- @Component사용시 설정 정보 자체가 없기 때문에 의존 관계 주입도 클래스 안에서 해결. @Autowired는 의존관계를 자동으로 주입

5) test > java > hello.core.scan(package) 생성
6) test > java > hello.core.scan에 AutoAppConfigTest(class) 생성
- AnnotationConfigApplicationContext를 사용하는 것은 기존과 동일
- 설정정보로 AutoAppConfig 클래스를 넘겨줌

(컴포넌트 스캔과 자동 의존관계 주입의 동작과정)
1) @ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록
- 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용
- 스피링 빈의 이름 직접 지정: @Component("이름")
2) @Autowired 의존관계 자동 주입
- 생성자에 @Autowired를 지정하면 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입
- 기본 조회 전략은 타입이 같은 빈을 찾아서 주입. getBean()과 동일

## 탐색 위치와 기본 스캔 대상
- 탐색할 패키지의 시작 위치 지정

```java
@ComponentScan(
    basePackages = "hello.core.member",
)
```
- 

## 필터

## 중복 등록과 충돌
