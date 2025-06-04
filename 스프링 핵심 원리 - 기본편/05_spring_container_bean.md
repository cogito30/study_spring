# 05. 스프링 컨테이너와 스프링 빈

## 스프링 컨테이너 생성
(스프링 컨테이너가 생성되는 과정)

```java
ApplicationContext applicaionContext = new AnnotationConfigApplicationContext(AppConfig.class);
```
- ApplicationContext를 스프링 컨테이너라 함
- ApplicationContext는 인터페이스
- 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있음
- 스프링 컨테이너를 부를 때는 BeanFactory와 ApplicationContext로 구분

(스프링 컨테이너의 생성 과정)
1) 스프링 컨테이너 생성: `new AnnotationConfigApplicationContext(AppConfig.class)`로 스프링 컨테이너 생성(생성시 구성 정보 지정)
2) 스프링 빈 등록: 스프링 빈 저장소에 빈 이름과 빈 객체를 등록. 메서드 이름을 빈 이름으로 지정. 빈 이름은 중복되어서는 안 됨
3) 스프링 빈 의존관계 설정 - 준비: 빈 객체를 생성
4) 스프링 빈 의존관계 설정 - 완료: 설정 정보를 참고하여 의존관계를 주입(DI)
+) 자바 코드로 스프링 빈을 등록하면 생성자 호출시 의존관계 주입도 처리

(정리)
- 스프링 컨테이너를 생성하고 설정(구성) 정보를 참고해서 스프링 빈도 등록하고 의존관계도 설정

## 컨테이너에 등록된 모든 빈 조회
1) test > java > hello.core.beanfind(pakage) 생성
2) test > java > hello.core.beanfind에서 ApplicationContextInfoTest(class) 생성
- `ac.getBeanDefinitionNames()`: 스프링에 등록된 모든 빈 이름을 조회
- `ac.getBean()`: 빈 이름으로 빈 객체(인스턴스)를 조회
- `getRole()`로 스프링 내부에서 사용하는 빈 구분. ROLE_APPLICATION(사용자가 정의한 빈), ROLE_INFRASTRUCTURE(스프링 내부에서 사용하는 빈)

## 스프링 빈 조회 - 기본
- 스프링 컨테이너에서 스프링 빈 조회 방법: `ac.getBean(빈이름, 타입)`, `ac.getBean(타입)`
- 조회 대상 스프링 빈이 없으면 예외 발생: `NoSuchBeanDefinitionException: No bean named 'xxxx' available.`
1) test > java > hello.core.beanfind에서 ApplicationContextContextFindTest(class) 생성
- 인터페이스로 조회시 인터페이스의 구현체가 대상이 됨. 스프링 빈에 등록된 인터페이스 타입으로 조회
- 구체 타입(인터페이스)로 조회시 유연성 떨어짐


## 스프링 빈 조회 - 동일한 타입이 둘 이상
- 타입으로 조회시 동일한 타입의 스프링 빙이 둘 이상이면 오류가 발생하므로 빈 이름을 지정
- `ac.getBeansOfType()`을 사용하면 해당 타입의 모든 빈 조회 가능
1) test > java > hello.core.beanfind에서 ApplicationContextSameBeanFindTest(class) 생성


## 스프링 빈 조회 - 상속 관계
- 부모 타입으로 조회시 자식 타입도 함께 조회
- 모든 자바 객체의 최고 부모인 Object 타입으로 조회하면 모든 스프링 빈을 조회
1) test > java > hello.core.beanfind에서 ApplicationContextExtendsFindTest(class) 생성
- 실제 개발시 애플리케이션 컨텍스트에서 빈을 조회하는 경우는 적음

## BeanFactory와 ApplicationContext

```
<interface>BeanFactory
            |
<interface>ApplicationContext
            |
AnnotationConfigApplicationContext
```

(BeanFactory)
- 스프링 컨테이너의 최상위 인터페이스
- 스프링 빈을 관리하고 조회하는 역할 담당
- `getBean()`을 제공
- 대부분의 기능을 BeanFactory가 제공

(ApplicationConxtext)
- BeanFactory 기능을 모두 상속 받아 제공
- MessageSource: 메시지소스를 활용한 국제화 기능
- EnvironmentCapable: 로컬(PC), 개발(테스트서버), 운영(배포) 등을 구분해서 처리
- ApplicationEventPublisher: 이벤트를 발행하고 구독하는 모델을 편리하게 지원
- ResourceLoader: 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회

(정리)
- ApplicationContext는 BeanFactory의 기능을 상속 받음
- ApplicationContext는 빈 관리기능 + 편리한 부가 기능을 제공
- BeanFactory를 직접 사용할 일은 적음. 부가기능이 포함된 ApplicationContext를 사용
- BeanFactory나 ApplicationContext를 스프링 컨테이너라 함

## 다양한 설정 형식 지원 - 자바 코드, XML
- 스프링 컨테이너는 다양한 형식의 설정 정보를 받아드릴 수 있게 유연하게 설계됨(자바 코드, XML, Groovy)

```
<interface>BeanFacotry
            |
<interface>ApplicationContext
            |
AnnotationConfigApplicationContext(AppConfig.class)
GenericXmlApplicationContext(appConfig.xml)
XxxApplicationContext(appConfig.xxx)
```

(애노테이션 기반 자바 토드 설정 사용)
- `new AnnotationConfigApplicationContext(AppConfig.class)`
- `AnnotationConfigApplicationContext` 클래스를 사용하면서 자바 코드로된 설정 정보를 넘김

(XML 설정 사용)
- 최근에는 스프링 부트를 사용하며 XML을 사용하지 않음. 많은 레거시 프로젝트 들이 XML로 구성
- XML을 사용시 컴파일 없이 빈 설정 정보를 변경할 수 있음
- `GenericXmlApplicationContext`을 사용하면서 xml 설정 파일을 넘김
1) test > java> hello.core.xml(package) 생성
2) test > java> hello.core.xml에 XmlAppContext(class) 생성
3) src > main > resources에 appConfig.xml(Spring config) 생성(우클릭 > New > XML Configuration File > Spring Config)
- xml기반의 `appConfig.xml` 스프링 설정과 자바 코드로 된 `AppConfig.java` 설정 정보를 비교해보면 거의 비슷

## 스프링 빈 설정 메타 정보 - BeanDefinition
- 스프링이 다양한 설정 형식을 지원하는데는 `BeanDefinition`이라는 추상화가 있음
- XML, 자바 코드를 읽어서 `BeanDefinition`을 만듬
- `BeanDefinition`을 빈 설정 메타정보라고 함. `@Bean`, `<bean
>` 당 하나씩 메타 정보가 생성
- 스프링 컨테이너는 해당 메타정보를 기반으로 스프링 빈을 생성

```
스프링 컨테이너 -> <interface>BeanDefinition
                    |           |           |
        AppConfig.class  appConfig.xml  appConfig.xxx
```

```
ApplicationContext
AnnotationConfigApplicationContext -> AnnotatedBeanDefinitionReader -> AppConfig.class -> BeanDefinition
GenericXmlApplicationContextApplicationContext -> XmlBeanDefinitionReader -> appConfig.xml -> BeanDefinition
XxxApplicationContext -> XxxBeanDefinitionReader -> appConfig.xxx -> BeanDefinition
```
- `AnnotationConfigApplicationContext`는 `AnnotatedBeanDefinitionReader`를 사용해서 `AppConfig.class`를 읽고 `BeanDefinition`을 생성한다
- `GenericXmlApplicationContext`는 `XmlBeanDefinitionReader`를 사용해서 `appConfig.xml`설정 정보를 읽고 `BeanDefinition`을 생성
- 새로운 형식의 설정 정보가 추가된다면, XxxBeanDefinitinoReader를 만들어서 BeanDefinition을 생성

1) test > java> hello.core.beandefinition(package) 생성
2) test > java> hello.core.beandefinition에 BeanDefinitinoTest(class) 생성

(BeanDefinition 살펴보기)
- BeanClassName: 생성할 빈의 클래스명(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)
- factoryBeanName: 팩토리 역할의 빈을 사용할 경우 이름. appConfig
- factoryMethodName: 빈을 생성할 메서드 지정. memberService
- Scope: 싱글톤(기본값)
- lazyInit: 스프링 컨테이너를 생성할 떄 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때까지 최대한 생성을 지연처리 하는지 여부
- InitMethodName: 빈을 생성하고 의존관계를 적용한 뒤에 호출되는 초기화 메서드명
- DestroyMethodName: 빈의 생명주기가 끝나서 제거하기 지거전에 호출되는 메서드명
- Constructor arguments, Properties: 의존관계 주입에서 사용(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)

(정리)
- BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수도 있음
- BeanDefinition에 대해서는 스프일이 다양한 형태의 설정 정보를 BeanDefinitino으로 추상화해서 사용하는 것 정도 이해하면 됨
