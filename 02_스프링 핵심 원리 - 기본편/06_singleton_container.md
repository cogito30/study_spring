# 06 싱글톤 컨테이너

## 웹 애플리에키션과 싱글톤
- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생
- 대부분 스프링 애플리케이션은 웹 애플리케이션
- 웹 애플리케이션은 보통 여러 고객이 동시에 요청

(순수한 DI 컨테이너)
- AppConfig는 요청할 때마다 객체를 새로 생성. 메모리 낭비가 심함
1) test > java > hello.core.singleton(package) 생성
2) test > java > hello.core.singleton에서 SingletonTest(class) 생성
=> 객체를 1개만 생성하고 공유해서 사용하도록 설계 변경(싱글톤 패턴)

## 싱글톤 패턴
- 클래스의 인스턴스가 1개만 생성되는 것을 보장하는 디자인 패턴
- private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 방지
1) test > java > hello.core.singleton에 SingletonService(class) 생성
- static 영역에 객체 instance를 미리 하나 생성해서 올림
- 객체 인스턴스가 필요하면 getInstance() 메서드를 통해서만 조회. 호출시 항상 같은 인스턴스를 반환
- 1개의 객체 인스턴스만 존재해야하므로 생성자를 private로 막아서 외부에서 객체 인스턴스 생성되는 것을 방지

2) test > java > hello.core.singleton > SingletonTest 하위에 singletonServiceTest 코드 작성
- private로 new 키워드 사용 방지. 호출시마다 같은 객체 인스턴스 반환 확인
- 스프링 컨테이너를 사용하면 자동으로 싱글톤 패턴으로 객체를 만들어줌
- 싱글톤 패턴은 미리 생성해두는 방법을 사용

(싱글톤 패턴 문제점)
- 싱클톤 패턴을 구현하는 코드 자체가 많음
- 의존관계상 클라이언트가 구체 클래스에 의존 -> DIP를 위반
- 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다
- 테스트하기 어렵다
- 내부 속성을 변경하거나 초기화하기 어렵다
- private 생성자로 자식 클래스를 만들기 어렵다
- 유연성이 떨어진다
- 안티 패턴이라고 불린다

## 싱글톤 컨테이너
- 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리
- 스프링 빈이 싱글톤으로 관리되는 빈

(싱글톤 컨테이너)
- 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도 객체 인스턴스를 싱글톤으로 관리
- 스프링 컨테이너는 싱글톤 컨테이너 역할. 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 함
- 스프링 컨테이너의 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있음
- DIP, OCP, 테스트, private 생성자로부터 자유롭게 싱글톤을 사용할 수 있음
1) test > java > hello.core.singleton > SingletonTest 하위에 springContainer 코드 작성
- 스프링 컨테이너 덕분에 고객의 요청이 올 때마다 객체를 생성하는 것이 아니라 이미 만들어진 객체를 공유해서 효율적으로 재사용
+) 스프링의 기본 빈 등록 방식은 싱글톤이지만 요청할 때마다 새로운 객체를 생성해서 반환하는 다른 방식 기능도 제공

## 싱글톤 방식의 주의점
- 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 떄문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안됨
- **무상태(stateless)**로 설계
  - 특정 클라이언트에 의존적인 필드가 있으면 안됨
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안됨
  - 가급적 읽기만 가능해야 함
  - 필드 대신에 자바에서 공유되지 않는 지역변수, 파라미터, ThreadLocal 등을 사용해야 함
- 스프링 빈의 필드에 공유 값을 설정하면 장애가 발생할 가능이 있음!!!
1) test > java > hello.core.sigleton에 StatefulService(class) 생성
+) cmd + shift + T(테스트 코드 생성)
2) test > java > hello.core.sigleton에 StatefulServiceTest(class) 생성
- ThreadA, ThreadB에서 같은 공유 필드를 사용하는데 특정 클라이언트가 값을 변경하는 문제가 생김
- 스프링 빈은 항상 무상태(stateless)로 설계

## @Configuration과 싱글톤
- AppConfig에서 memberService로 빈을 만드는 코드를 보면 memberRepository()를 호출
- AppConfig에서 orderService로 빈을 만드는 코드를 보면 memberRepository()를 호출
- 다른 2개의 MemoryMemberRepository가 생성됨
1) src > main> java > hello.core.member > MemberServiceImpl에 코드 추가
2) src > main > java > hello.core.order > OrderServiceImple에 코드 추가
3) test > java > hello.core.singlegton에 ConfigurationSingletonTest(class) 생성
- memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용됨

## @Configuration과 바이트코드 조작의 마법
- 스프링 컨테이너는 싱글톤 레지스트리. 스프링 빈이 싱글톤이 되도록 보장해주어야 함
1) src > main> java > hello.core.singleton의 ConfigurationSingletonTest에 코드 추가 
- AnnotationConfigApplicationContext에 파라미터로 넘긴 값은 스프링 빈으로 등록됨
- 순수한 클래스하면 `class hello.core.AppConfig`라고 출력되어야 함
- 클래스명에 xxxCGLIB가 붙어서 출력되는 것은 사용자가 만든 클래스가 아니라 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고 그 다른 클래스를 스프링 빈으로 등록한 것
- 이 임의의 다른 클래스가 싱글콘이 되도록 보장해줌

(AppConfig@CGLIB 예상 코드)

```java
@Bean
public MemberRepository memberRepository() {
  if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면) {
    return 스프링 컨테이너에서 찾아서 반환;
  } else {
    기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
    return 반환
  }
}
```

- @Bean이 붙은 메서드마다 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어짐 -> 싱글톤 보장
+) AppConfig@CGLIB은 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 가능

(@Configuration을 적용하지 않고 @Bean만 적용할 경우)
- @Configuration을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장
- @Bean만 적용할 경우 CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록은 되지만 싱글톤이 보장되지 않음. 각각의 인스턴스 역시 동일하지 않음
+) @Autowired로 의존관계 주입을 사용하여 해결수도 있음

(정리)
- @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤은 보장되지 않음
- 스프링 설정 정보는 항상 @Configuration을 사용하자