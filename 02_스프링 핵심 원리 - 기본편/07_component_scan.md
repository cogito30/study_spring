# 07 컴포넌트 스캔

## 컴포넌트 스캔과 의존관계 자동 주입 시작하기
- 스프링 빈 등록시 자바코드의 @Bean이나 XML의 <bean> 등을 통해 설정 정보에 직접 등록
- 설정 정보가 커질 경우 반복 등록의 문제가 발생
- 스프링에서는 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공
- 의존관계도 자동으로 주입하는 @Autowired 기능을 제공
- @ComponentScan은 @Compoent를 찾아 자동으로 Spring Bean에 등록

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
- 로그를 잘 보면 컴포넌트 클래스가 잘 표시되는 것을 확인할 수 있음
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

1) src > main > java > hello.core의 AutoAppConfig 코드 추가
```java
@ComponentScan(
    basePackages = {"hello.core.member", "hello.core.service"}  // 멤버의 하위 컴포넌트만 등록
)
```
- basePackages: 탐색할 패키지의 시작 위치를 지정. 지정 패키니르 포함해서 하위 패키지는 모두 탐색. 여러 패키지 지정 가능
- basePackageClasses: 지정한 클래스의 패키지를 탐색 시작 위치로 지정. 지정하지 않는 경우 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 됨

- 권장 방법: 패키지 위치를 지정하지 않고 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것. 스프링 부트도 해당 방법을 기본으로 제공
+) 스프링 부트 사용시 스프링 부트의 대표 시작 정보인 @SpringBootApplication을 프로젝트 시작 루트에 두는 것이 관례(해당 설정 안에 @ComponentScan이 들어 있음)

(컴포넌트 스캔 기본 대상)
- 컴포넌트 스캔은 @Component뿐만 아니라 다음과 같은 내용도 추가로 대상에 포함
  - @Component: 컴포넌트 스캔에서 사용
  - @Controller: 스프링 MVC 컨트롤러에서 사용
  - @Service: 스프링 비즈니스 로직에서 사용
  - @Repository: 스프링 데이터 접근 계층에서 사용
  - @Configuration: 스프링 설정 정보에서 사용 

+) 애노테이션에는 상속관계라는 것이 없음. 특정 애노페이션을 들고 있는 것을 인식할 수 있는 것은 자바 언어가 지원하는 기능이 아닌 스프링이 지원하는 기능

(컴포넌트 스캔 용도 외에 애노테이션이 있으면 스프링 부가기능을 수행)
- @Controller: 스프링 MVC 컨트롤러로 인식
- @Repository: 스프링 데이터 접근 계층으로 인식하고 데이터 계층의 예외를 스프링 예외(추상화)로 변환
- @Configuration: 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리
- @Service: 특별한 처리를 하지 않는 대신 개발자들이 핵심 비즈니스 로직이 있다고 비즈니스 계층을 인식하는데 도움이 됨

+) useDefaultFIlters 옵션이 기본으로 켜져 있는데 옵션을 끄면 기본 스캔 대상들이 제외됨

## 필터
- includeFilters: 컴포넌트 스캔 대상을 추가로 지정
- excludeFilters: 컴포넌트 스캔에서 제외할 대상을 지정
1) src > test > java > hello.core.scan.filter(package) 생성
2) src > test > java > hello.core.scan.filter에 MyIncludeCompoent(annotation) 생성
3) src > test > java > hello.core.scan.filter에 MyExcludeCompoent(annotation) 생성
4) src > test > java > hello.core.scan.filter에 BeanA(class) 생성
5) src > test > java > hello.core.scan.filter에 BeanB(class) 생성
6) src > test > java > hello.core.scan.filter에 ComponentFilterAppConfigTest(class) 생성 
- includeFilters에 MyIncludeComponent 애노테이션을 추가해서 BeanA가 스프링 빈에 등록
- excludeFilters에 MyExcludeComponent 애노테이션을 추가해서 BeanB가 스프링 빈에 등록되지 않음

(FilterType 5가지 옵션)
- ANNOTATION: 기본값, 애노테이션을 인식해서 동작
- ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작
- ASPECT: AspectJ 패턴 사용
- REGEX: 정규 표현식
- CUSTOM: TypeFilter라는 인터페이스르 구현해서 처리

+) @Component면 충분하기 때문에 includeFilters를 사용할 일은 거의 없음
+) 스프링 부트는 컴포넌트 스캔을 기본으로 제공

## 중복 등록과 충돌
- 컴포넌트 스캔에서 같은 빈 이름을 등록하면 어떻게 도리지
  - 자동 빈 등록 vs 자동 빈 등록: ConflictingBeanDefinitionException 예외 발생
  - 수동 빈 등록 vs 자동 빈 등록: 수동 빈 등록이 우선권을 가짐
  - 수동 빈 등록시 Overriding bean 로그가 남음
  - 스프링 부트는 수동 빈 등록과 자동 빈 등록이 충돌이 나면 오류가 발생하도록 기본 값이 변경 됨
