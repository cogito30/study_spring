# 10 빈 스코프

## 빈 스코프란?
- 스프링 빈이 스프링 컨테이너의 시작과 함께 생성되어서 스프링 컨테이너가 종료될 때까지 유지된다고 학습
- 스프링 빈이 기본적으로 싱글톤 스코프로 생성되기 때문. 스코프는 번역 그대로 빈이 존재할 수 있는 범위를 의미

(스프링의 지원 스코프 종류)
- **싱글톤**: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
- 프로토타입: 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 짧은 범위의 스코포
- 웹 관련 스코프: 스프링 웹 관련 기능이 들어갈 떄 사용하는 스코프
  - request: 웹 요청이 들어오고 나갈 때까지 유지되는 스코프
  - session: 웹 세션이 생성되고 종료될 떄까지 유지되는 스코프
  - application: 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프

(빈 스코프 지정 방법)
```java
// 컴포넌트 스캔 자동 등록
@Scope("prototype")
@Component
public class HelloBean {}

// 수동 등록
@Scope("prototype")
@Bean
PrototypeBean HelloBean() {
    return new HelloBean();
}
```

## 프로토타입 스코프
- 싱글톤 스코프의 빈을 조회하면 스프링 컨테이너는 항상 같은 인스턴스의 스프링 빈을 반환
- 프로토타입 스코프를 스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환

(싱글톤 빈 요청)
1) 싱글톤 스코프의 빈을 스프링 컨테이너에 요청
2) 스프링 컨테이너는 본인이 관리하는 스프링 빈을 반환
3) 이후에 스프링 컨테이너에 같은 요청이 와도 같은 객체 인스턴스의 스프링 빈을 반환

(프로토타입 빈 요청)
1) 프로토타입 스코프의 빈을 스프링 컨테이너에 요청
2) 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고 필요한 의존관계를 주입
3) 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환
4) 이후에 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환

(정리)
- 스프링 컨테이너는 프로토타입 빈을 생성하고 의존관계 주입, 초기화까지만 처리
- 클라이언트에 빈을 반환하고, 이후 스프링 컨테이너는 생성된 프로토타입 빈을 관리하지 않음
- 프로토타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에 있음. 그래서 @PreDestroy 같은 종료 메서드가 호출되지 않음

(코드 확인)
1) src > test > java > hello.core.scope(package) 생성
2) src > test > java > hello.core.scope에 SingletonTest(class) 생성
3) src > test > java > hello.core.scope에 PrototypeTest(class) 생성
- prototype 사용시 destroy가 동작하지 않음
- 싱글톤 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행되지만, 프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을 조회할 때 생성되고, 초기화 메서드도 실행된다
- 프로토타입 빈은 2번 조회했으므로 완전히 다른 스프링 빈이 생성되고, 초기화도 2번 실행된 것을 확인할 수 있음
- 싱글톤 빈은 스프링 컨테이너가 관리하기 때문에 스프링 컨테이너가 종료될 때 빈의 종료 메서드가 실행되지만 프로토타입 빈은 스프링 컨테이너가 생성과 의존관계 중비 그리고 초기화까지만 관여하고 더는 관리하지 않음. 따라서 프로토타입 빈은 스프링 컨테이너가 종료될 때 @PreDestroy 같은 종료 메서드가 전혀 실행되지 않음

(프로토타입 빈의 특징 정리)
- 스프링 컨테이너에 요청할 때마다 새로 생성됨
- 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 중비 그리고 초기화까지만 관여
- 종료 메서드가 호출되지 않음
- 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 함. 종료 메서드에 대한 호출도 클라이언트가 직접 해야함

## 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점
- 스프링 컨네이너에 프로토타입 스코프의 빈을 요청하면 항상 새로운 객체 인스턴스를 생성해서 반환. 하지만 싱글톤 빈과 함께 사용할 때는 의도한대로 잘 동작하지 않으므로 주의해야 함

(스프링 컨테이너에 프로토타입 빈 직접 요청)
1) 클랑이언트A는 스프링 컨테이너에 프로토타입 빈을 요청
2) 스프링 컨테이너는 프로토타입 빈(x01)을 새로 생성해서 반환함. 해당 빈의 count 필드 값은 0이다
3) 클라이언트는 조회한 프로토타입 빈에 addCount()를 호출하면서 count필드를 +1한다
- 결과적으로 프로토타입 빈(x01)의 count는 1이 됨
4) 클라이언트B는 스프링 컨테이너에 프로토타입 빈을 요청
5) 스프링 컨테이너는 프로토타입 빈(x02)을 생성해서 반환함. 해당 빈의 count 필드 값은 0이다
6) 클라이언트는 조회한 프로토타입빈에 addCount()를 호출하면서 count 필드를 +1함
- 결과적으로 프로토타입 빈(x02)의 count는 1이 됨

(코드 작성)
1) src > test > java > hello.core.scope에 SingletonWithPrototypeTest1(class) 생성

(싱글톤에서 프로토타입 빈 사용)
- clientBean은 싱글톤이므로 보통 스플이 컨테이너 생성 시점에 함께 생성되고, 의존관계 주입도 발생
1) clientBean은 의존관계 자동 주입을 사용함. 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청함
2) 스프링 커테이너는 프로토타입 빈을 생성해서 clientBean에 반환. 프로토타입 빈은 count 필드 값은 0이다
- 이제 clientBean은 프로토타입 빈을 내부 필드에 보관(정확히는 참조값을 보관)
3) 클라이언트A는 clientBean.logic()을 호출
- 클라이언트A는 clientBean을 스프링 컨테이너에 요청해서 받음. 싱글톤이므로 같은 clientBean이 반환
4) clientBean은 prototybeBean의 addCount()를 호출해서 프로토타입 빈의 count를 증가. count값이 1이 됨
5) 클라이언트 B는 clientBean.logic()을 호출
- 클라리언트B는 clientBeanㅇ르 스프링 컨테이너에 요청해서 받음. 싱글톤이므로 항상 같은 clientBean이 반환
- 중요한 점은 clientBean이 내부에 가지고 있는 프로토타입 빈은 이미 과거에 중비이 끝난 빈. 주입 시점에 스프링 컨테이너에 요청해서 프로토타입 빈이 새로 생성이 된 것이지, 사용할 때마다 새로 생성되는 것은 아님
6) clientBean은 prototypeBean의 addCount()를 호출해서 프로토타입 빈의 count를 증가. 원래 count가 1이였으므로 2가 됨

(코드 작성)
1) src > test > java > hello.core.scope에 SingletonWithPrototypeTest1(class) 코드 추가

- 스프링 빈은 일반적으로 싱글톤 빈을 사용하므로 싱글톤 빈이 프로토타입 빈을 사용하게 됨. 싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에 프로토타입 빈이 새로 생성되기는 하지만 싱글콘 빈과 함꼐 계속 유지되는 것이 문제
- 아마 원하는 것이 이런 것은 아닐 것이다. 프로토 타입 빈을 주입 시점에만 새로 생성하는 것이 아니라 사용할 떄마다 새로 생성해서 사용하는 것을 원할 것이다.

+) 여러 빈에서 같은 프로토타입 빈을 주입받으면 주입 받는 시점에 각각 새로운 프로토타입 빈이 생성됨. 예를 들어서 clientA, clientB가 각각 의존관계 주입을 받으면 각각 다른 인스턴스의 프로토타입 빈을 주입 받음
- clientA -> prototypeBean@x01
- clientB -> prototypeBean@x02
- 사용할 때마다 새로 생성되는 것은 아님

## 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider로 문제 해결
- 싱글톤 빈과 프로토타입 빈을 함꼐 사용할 떄, 어떻게하면 사용할 떄마다 항상 새로운 프로토타입 빈을 생성할 수 있을까

(스프링 컨테이너에 요청)
- 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청하는 것ㄴ
```java
@Autowired
private ApplicationContext ac;

public int logic() {
    PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```
- 실행해보면 ac.getBean()을 통해서 항상 새롱누 프로토타입 빈이 생성되는 것을 확인할 수 있음
- 의존관계를 외부에서 주입(DI)받는 것이 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 Dependency Lookup(DL) 의존관계 조회(탐색)이라함
- 이렇게 스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스플이 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워짐
- 필요한 기능은 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는 DL 정도의 기능만 제공하는 무언가가 있음녀 됨

(ObjectFactory, ObjectProvider)
- 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 제공하는것이 ObjectProvider.
- 과거에는 ObjectFactory가 있었는데 여기에 편의 기능을 추가해서 ObjectProvider가 만들어짐

1) src > test > java > hello.core.scope의 SingletonWIthPrototypeTest2(class) 생성
- 프로토타입 빈 2개 생성
- 실행해보면 prototypeBeanProvider.getObject()을 통해서 항상 새롱누 프로토타입 빈이 생성되는 것을 확인할 수 있음
- ObjectProvider의 getObject()를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환함(DL)
- 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워짐
- ObjectProvider는 필요한 DL 정도의 기능만 제공

(특징)
- ObjectFactory: 기능이 단순, 별도의 라이브러리 필요 없음. 스프링에 의존
- ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리 등 편의 기능이 많고, 별도의 라이브러리 필요 없음. 스플이에 의존

(JSR-330 Provider)
- javax.inject.Provider라는 JSR-330 자바 표준을 사용하는 방법
- 이 방법을 사용하려면 `javax.inject:javax.inject:1` 라이브러리를 gradle에 추가해야 함

1) build.gradle에 라이브러리 추가
2) src > test > java > hello.core.scope의 SingletonWIthPrototypeTest2(class) 코드 변경. ObjectProvider를 Provider로 변환. getObject() -> get() 수정
- 실행해보면 provider.get()을 통해서 항상 새롱누 프로토타입 빈이 생성되는 것을 확인할 수 있음
- provider의 get()을 호출하면 내부에서는 스플이 컨테이너를 통해 해당 빈을 찾아서 반환(DL)
- 자바 표준이고 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워짐
- Provider는 지금 딱 필요한 DL 정도의 기능만 제공

(Provider 특징)
- get() 메서드 하나로 기능이 매우 단순
- 별도의 라이브러리가 필요
- 자바 표준이이므로 스프링이 아닌 다른 컨테이너에서도 사용 가능 

(정리)
- 프로토타입 빈은 언제 사용할까? 매번 사용할 때마다 의존관계 주입이 완료된 새로운 객체가 필요하면 사용하면 됨. 그런데 실무에서 웹 애플리케이션을 개발해보면 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일이 드물다
- ObjectProvider, JSR303 Provider 등은 프로토타입 뿐만 아니라 DL이 필요한 경우는 언제든지 사용할 수 있음

+) 스프링이 제공하는 메서드에 @Lookup 애노테이션을 사용하는 방법도 있지만, 이전 방법들로 충분하고 고려해야할 내용도 많아서 생략

+) 실무에서 자바 표준인 JSR-330 Provider를 사용할 것인지, 아니면 스프링이 제공하는 ObjectProvider를 사용할지 고민이 될 것이다. ObjectProvider는 DL을 위한 편의 기능을 많이 제공해주고 스프링 외에 별도의 의존관계 추가가 필요없기 떄문에 편리하다. 만약 코드를 스프링이 아닌 다른 컨테이너에서도 사용할 수 있어야 한다면 
JSR-330 Provider를 사용해야한다

+) 스프링을 사용하다보면 이 기능 뿐만 아니라 다른 기능들도 자바 표준과 스프링이 제공하는 기능이 겹칠때가 많이 있다. 대부분 스프링이 더 다양하고 편리한 기능을 제공해주기 때문에, 특별히 다른 컨테이너를 사용할 일이 없다면 스프링이 제공하는 기능을 사용하면 됨

+) JPA는 자바 표준이 승리했기에 Hibernate는 JPA 표준 구현체 내로 들어감.
+) Spring의 경우 사실상 기술 표준이 되어 Spring 컨테이너 기능을 사용. 기능이 비슷할 경우 Spring 사용. 기능상 java 표준을 추천한다면 java 표준을 사용

## 웹 스코프
- 싱글톤: 스프링 컨테이너의 시작과 끝을 함께하는 매우 긴 스코프
- 프로토타입: 생성과 의존관계 중비, 초기화까지만 진행하는 특별한 스코프

(웹 스코프의 특징)
- 웹 스코프는 웹 환경에서만 동작
- 웹 스코프는 프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리. 종료 메서드가 호출됨

(웹 스코프 종류)
- request: HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프. 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리됨
- session: HTTP Session과 동일한 생명주기를 가지는 스코프
- application: 서블릿 컨텍스트(ServletContext)와 동일한 생명주기를 가지는 스코프
- websocket: 웹 소켓과 동일한 생명주기를 가지는 스코프

## requese 스코프 예제 만들기
- 웹 스코프는 웹 환경에서만 동작하므로 web 환경이 동작하도록 라이브러리 추가

1) build.gradle에 추가
```java
implementation 'org.springframework.boo:spring-boot-starter-web'
```
2) src > main > java > hello.core의 CoreApplication 실행
- `spring-boot-starter-web`라이브러리를 추가하면 스프링 부트는 내장 톰켓 서버를 활용해서 웹 서버와 스프링을 함께 실행시킴
- 스프링 부트는 웹 라이브러리가 없으면 우리가 지금까지 학습한 AnnotationConfigApplicationContextㅇ르 기반으로 기본 애플리케이션을 구동. 웹 라이브러리가 추가되면 웹 관련 추가 설정과 환경들이 필요하므로 AnnotationConfigServletWebServerApplicationContext를 기반으로 애플리케이션을 구동
+) 기본 포트인 8080 포트를 다른 곳에서 사용중이여서 오루가 발생한다면 application.properties 파일에 server.port=9090 설정 추가

(request scope 예제 개발)
- 동시에 여러 HTTP 요청이 오면 정확히 어떤 요청이 남긴 로그인지 구분이 어려움
- 이럴 때 사용하기 좋은 것이 request 스코프
- request scope를 활용해서 로그가 남도록 추가 기능을 개발
  - 기대하는 공통 포맷: `[UUID][requestURL]{message}`
  - UUID를 사용해서 HTTP 요청을 구분하자
  - requestURL 정보도 추가로 넣어서 어떤 URL을 요청해서 남은 로그인지 확인하자

(코드 확인)
1) src > main > java > hello.core.common(package) 생성
2) src > main > java > hello.core.common에 MyLogger(class) 생성
- 로그를 출력하기 위한 MyLogger 클래스이다
- @Scope(value = "reqeust")를 사용해서 request 스코프로 지정했다. 이게 이 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다
- 이 빈이 생성되는 시점에 자동으로 @PostConstruct 초기화 메서드를 사용해서 uuid를 생성해서 저장해 둔다. 이 빈은 HTTP 요청 당 하나씩 생성되므로 uuid를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
- 이 빈이 소멸되는 시점에 @PreDestroy를 사용해서 종료메시지를 남긴다
- requestURL은 이 빈이 생성되는 시점에는 알 수 없으므로 외부에서 setter로 입력 받는다.

3) src > main > java > hello.core.web(package) 생성
4) src > main > java > hello.core.web에 LogDemoController(class) 생성
- 로거가 잘 작동하는지 확인하는 테스트용 컨트롤러다
- 여기서 HttpServiceRequest를 통해서 요청 URL을 받았다
  - reqeustURL값: http://localhost:8080/log-demo
- 이렇게 받은 requestURL 값을 myLogger에 저장해둔다. myLogger는 HTTP 요청 당 각각 구분되므로 다른 HTTP 요청 때문에 값이 섞이는 걱정은 하지 않아도 된다
- 컨트롤러에서 controller test라는 로그를 남긴다

+) requestURL을 MyLogger에 저장하는 부분은 컨트롤러보다는 공통처리가 가능한 스프링 인터셉터나 서블릿 필터 같은 곳을 활용하는 것이 좋다. 여기서는 예제를 단순화하고, 아직 스프링 인터셉트를 학습하지 않은 분들을 위해서 컨트롤러를 사용함. 스프링 웹에 익숙하다면 인터셉터를 사용해서 구현

4) src > main > java > hello.core.web에 LogDemoService(class) 생성
- 비즈니스 로직이 있는 서비스 계층에서도 로그를 출력
- request scope를 사용하지 않고 파라미터로 이 모든 정보를 서비스 계층에 넘긴다면, 파라미터가 많아서 지저분해진다. 더 문제는 requestURL 같은 웹과 관련된 정보가 웹과 관련없는 서비스 계층까지 넘어가게된다. 웹과 관련된 부분은 컨트롤러까지만 사용해야 함. 서비스 계층은 웹 기술에 종속되지 않고 가급적 순수하게 유지하는 것이 유지보수 관점에서 좋음
- reqeust scope의 MyLogger 덕분에 이런 부분을 파라미터로 넘기지 않고 MyLogger의 멤버 변수에 저장해서 코드와 계층을 깔끔하게 유지 할 수 있음

- 실행시 기대와 다른 오류 발생
- 스프링 애플리케이션ㅇ르 실행 시키면 오류가 발생함. 메시지 마지막에 싱글톤이라는 단어가 나오고 스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입이 가능하지만 request 스코프 빈은 아직 생성되지 않는다. 이 빈은 실제 고객의 요청이 와야 생성할 수 있다.

## 스코프와 Provider
- Provider를 사용해여 스코프 문제 해결
1) src > main > java > hello.core.web의 LogDemoController(class) 코드 수정
2) src > main > java > hello.core.web의 LogDemoServer(class) 코드 수정
- 동시에 여러 요청이 와도 객체마다 따로 처리
- main() 메서드로 스프링을 실행하고, 웹 브라우저에 http://localhost:8080/log-demo를 입력하여 작동 확인

- ObjectProvider 덕분에 ObjectProvider.getObject()를 호출하는 시점까지 request scope 빈의 싱성을 지연할 수 있음
- ObjectProvider.getObject()를 호출하시는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리
- ObjectProvider.getObject()를 LogDemoController, LogDemoService에서 각각 한번씩 따로 호출해다 같은 HTTP 요청이면 같은 스프링 빈이 반환됨

## 스코프와 프록시
