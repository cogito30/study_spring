# 08 의존관계 자동 주입

## 다양한 의존관계 주입 방벙
(의존관계 주입 방법 4가지)
1) 생성자 주입
2) 수정자 주입(setter 주입)
3) 필드 주입
4) 일반 메서드 주입

(생성자 주입)
- 생성자를 통해서 의존 관계를 주입 받는 방법
- 특징: 생성자 호출시 딱 1번만 호출되는 것이 보장. `불변, 필수`의존관계에 사용
- 생성자가 1개라면 @Autowired가 없어도 자동으로 의존관계 주입 적용. 스프링 빈에만 해당

(수정자 주입)
- setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법
- setter에 @Autowired를 붙여서 의존관계 주입
- 특징: 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법 . `선택, 변경` 가능성이 있는 의존관계에 사용
- 스프링 빈에 등록되지 않았을 경우도 사용 가능
- 선택적일 때 @Autowired(required=false) 사용
+) @Autowired의 기본 동작은 주입 대상이 없다면 오류 발생. @Autowired(required=false) 사용시 주입 대상이 없어도 동작

```java
private int age;

@Autowired
public void setAge(int age) {
    this.age = age;
}

public int getAge() {
    return age;
}
```

(필드 주입)
- 필드에 바로 주입하는 방법
- 특징: 코드가 간결하지만 외부에서 변경이 불가능하여 테스트하기 힘듬. DI 프레임워크가 없으면 아무것돌 할수 없음
- 사용하지 않기를 추천.
- 애플리케이션의 실제 코드와 관계 없는 테스트 코드 혹은 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용

```java
@Autowired private MemberRepository memberRepository;
@Autowired private DiscountPolicy discountPolicy;
```

(일반 메서드 주입)
- 일반 메서드를 통해서 주입
- 특징: 한 번에 여러 필드를 중비 받을 수 있음. 일반적으로 잘 사용하지 않음
- 메서드에 @Autowired 적용

- 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작.

## 옵션 처리
- 주입할 스프링 빈이 없어도 동작해야 할 경우 @Autowired만 사용하면 required 옵션의 기본값이 true로 되어 있어서 자동 주입 대상이 없으면 오류가 발생

(자동 주입 대상을 옵션으로 처리하는 방법)
- `@Autowired(required=false)`: 자동 중비할 대상이 없으면 수정자 메서드 자체가 호출 안 됨
- `org.springframework.lang.@Nullable`: 자동 주입할 대상이 없으면 null이 입력됨
- `Optional<>`: 자동 주입할 대상이 없으면 Optional.empty가 입력

```java
// 호출 안됨
@Autowired(required = false)
public void setNoBean1(Member member) {
    System.out.println("setNoBean1 = " + member);
}

// null 호출
@Autowired
public void setNoBean2(@Nullable Member member) {
    System.out.println("setNoBean2 = " + member);
}

// Optional.empty 호출
@Autowired
public void setNoBean3(Optional<Member> member) {
    System.out.println("setNoBean = " + member);
}
```
- Member는 스프링 빈이 아님
1) src > test > java > hello.core.autowired(package) 생성
2) src > test > java > hello.core.autowired에 AutoWiredTest(class) 생성

- @Nullable, Optional은 스프링 전반에 걸쳐서 지원. 생성자 자동 주입에서 특정 필드에만 사용 가능

## 생성자 주입을 선택해라
- 과거에는 수정자 주입과 필드 주입을 많이 사용했지만 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장
- 생성자 주입 권장 이유: 불변과 누락

(불변)
- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관께를 변경할 일이 없음. 대부분의 의존관꼐는 애플리케이션 종료 전까지 변하면 안됨(불변해야 함)
- 수정자 주입을 사용하면 ,setXxxx 메서드를 public으로 열어두어야 함
- 누군가 실수로 변경할 수 있고 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아님
- 생성자 주입은 객체를 생성할 때 딱 1버난 호출되므로 이후에 호출되는 일어 없음. 따라서 불변하게 설계 가능

(누락)
- 프레임워크 없이 순수한 자바 코드를 단위 테스트 하는 경우에 수정자 의존일 경우 @Autowired가 프레임워크 안에서 동작할 때는 의존 관계가 없으면 오류가 발생하지만 프레임워크 없이 순수한 자바 코드로만 단위 테스트 수행시 실행이 됨. 결과는 Null Point Excecption 반생
- 생성자 주입을 사용하면 주입 데이터 누락시 컴파일 오류가 발생

1) src > main > java > hello.core.order의 OrderServiceImpl(class) 생성자 코드를 setter로 수정
2) src > test > java > hello.core.order의 OrderServiceImpl(class) 생성
3) src > main > java > hello.core의 AppConfig(class)의 orderService 메서드의 반환을 null로 임시 변경

- 생성자 사용시 final 키워드 사용 가능. 생성자에서 값이 설정되지 않는 오류를 컴파일 시점에 막아줌
- 컴파일 오류는 세상에서 가장 빠르고 좋은 오류다!!

+) 수정자 주입을 포함한 나머지 주입 방식은 모두 생성자 이후에 호출되므로 필드에 final 사용 불가

(정리)
- 생성자 주입 방식을 선택하는 이유는 프레임워크에 의존하지 않고 순수한 자바 언어의 특징을 잘 살리는 방법이기 때문
- 기본으로 생성자 주입을 사용하고 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여. 생성자 주입과 수정자 주입을 동시에 사용 가능
- 항상 생성자 주입을 선택하라! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라.. 필드 주입은 사용하지 않는게 좋다

## 롬복과 최신 트랜드
- 개발을 하다보면 대부분 다 불면이기에 생성자에 final 키워드를 사용하게 됨
- 필드 주입처럽 편리하게 생성자, 값 주입을 하는 방법이 없을까 -> 롬복
- 생성자가 1개라면 @Autowired 생략 가능
- 롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만듬

(롬복 추가 방법)
1) build.gradle 수정. 롬복 설정 추가
2) Preferences > Plugins에 Lombok 추가
3) Preferences > annotation processors에서 Enable annotation processing 체크(재시작)
4) src > main > java > hello.core에 HelloLombok(class) 추가

1) src > main > java > hello.core.order의 OrderServiceImpl(class) 코드 수정
- cmd + F12로 메서드 확인

(정리)
- 최근에는 생성자를 딱 1개 두고, @Autowired를 생략하는 방법을 주로 사용함. Lombok 라이브러리의 @RequiredArgsConstructor와 함께 사용하면 기능은 다 제공하면서 코드는 깔끔하게 사용 가능

## 조회 빈이 2개 이상 - 문제
```java
@Autowired
private DiscountPolicy discountPolicy;
```
- 타입으로 조회하기 때문에 ac.getBean(DiscountPolicy.class)와 유사하게 동작
- 스프링 빈 조회처럼 타입으로 조회하면 선택된 빈이 2개 이상일 때 문제가 발생

1) DiscountPolicy의 하위 타입인 FixDiscountPolicy, RateDiscountPolicy 둘다 스프링 빈으로 선언하자.
```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {}

@Component
public class RateDiscountPolicy implements DiscountPolicy {}
```

2) 의존관계 자동 주입 실행 -> NoUniqueBeanDefinitionException 오류 발생
```java
@Autowired
private DiscountPolicy discountPolicy;
```

- 하위 타입으로 지정할 수 있지만 하위 타입으로 지정하는 것은 DIP를 위배하고 유연성이 떨어짐. 이름만 다르고 같은 타입의 스프링 빈이 2개 있는 경우 해결되지 않음
- 스프링 빈을 수동 등록해서 문제를 해결해도 되지만, 의존 관계 자동 주입에서 해결하는 여러 방법이 존재

## @Autowired 필드명, @Qualifier, @Primary
(조회 대상 빈이 2개 이상일 때 해결 방법)
- @Autowired 필드명 매칭
- @Quilifier -> @Quilifier끼리 매칭 -> 빈 이름 매칭
- @Primary 사용

(@Autowired 필드명 매칭)
- @Autowired는 타입 매칭을 시도하고 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭
```java
// 기존 코드
@Autowired
private DiscountPolicy discountPolicy;

// 필드명을 빈 이름으로 변경
@Autowired
private DiscountPolicy rateDiscountPolicy;
```
- 필드명이 rateDiscountPolicy이므로 정상 주입됨
- 필드명 매칭은 먼저 타입 매칭을 시도하고 그 결과에 여러 빈이 있을 때 추가로 동작하는 기능

1) src > main > java > hello.core.order의 OrderServiceImpl(class)
2) src > test > java > hello.core.scan의 AutoAppConfigTest의 basicScan() 실행시 성공

(@AutoWired 매칭 정리)
1) 타입 매칭
2) 타입 매칭의 결과가 2개 이상일 때 필드명, 파라미터명으로 빈 이름 매칭

(@Quilifier 사용)
- @Quilifier는 추가 구분자를 붙여주는 방법. 중비시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아님
- 주입시에 @Qualifier를 붙여주고 등록한 이름을 적어줌

```java
// 빈 등록시 @Quilifier를 붙여준다
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("fixsDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}

// 생성자 자동 주입 예시
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, 
                    @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) 
{
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}

// 수정자 자동 주입 예시
@Autowired
public DiscountPolicy setDiscountPolicy(@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    return discountPolicy;
}
```

- @Qualifier로 주입할 떄 @Qualifier("mainDiscountPolicy")를 못찾으면 mainDiscountPolicy라는 이름의 스프링 빈을 추가로 탐색

- 직접 빈 등록시에도 @Qualifier를 동일하게 사용
```java
@Bean
@Qualifier("mainDiscountPolicy")
public DiscountPolicy discountPolicy() {
    return new ...
}
```

(@Qualifier 정리)
1) @Qualifier끼리 매칭
2) 빈 이름 매칭
3) `NoSuchBeanDefinitionException` 예외 발생

(@Primary 사용)
- @Primary는 우선순위를 정하는 방법
- @Autowired시에 여러 빈 매칭되면 @Primary가 우선권을 가짐

```java
@Compoent
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

```java
//생성자
public OrderServiceImpl(MemberRepository memberRepository,
                        DiscountPolicy discountPolicy)
{
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}

// 수정자
@Autowired
public DiscountPolicy setDiscountPolicy(DiscountPolicy discountPolicy) {
    return discountPolicy;
}
```

- @Qualifier의 단점은 주입 받을 때 모든 코드에 @Qualifier르 붙여주어야 함
- 반면에 @Primary를 사용하면 @Qualifier를 붙일 필요가 없음

(@Primary, @Qualifier 활용)
- 코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈이 있고, 코드에서 특별한 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈이 있다고 생각해보자. 메인 데이터베이스의 커넥션을 획득하는 스프링 빈은 @Primary를 적용해서 조회하는 곳에서 @Qualifier 지정 없이 편리하게 조회하고, 서브 데이터베이스 커넥션 빈을 획득할 떄는 @Qualifier를 지정해서 명시적으로 획득하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있따. 물론 이때 메인 데이터베이스의 스프링을 등록할 때 @Qualifier를 지정해주는 것을 상관없다

(우선순위)
- @Primary는 기본값처럼 동작하는 것이고, @Qualifier는 매우 상세하게 동작한다. 이런 경우 어떤 것이 우선권을 가져갈까? 스프링은 자동보다는 수동이, 넓은 범위의 선택권보다는 좁은 범위의 선택권 우선순위가 높다. 따라서 여기서도 @Qualifier가 우선권이 높다

## 애노테이션 직접 만들기
- @Qualifier("mainDiscountPolicy")는 컴파일시 타입 체크가 안됨

1) src > main > java > hello.core.annotation(package) 생성
2) src > main > java > hello.core.annotation에 MainDiscountPolicy(annotation) 생성
```java
@MainDiscoutnPolicy
public class RateDiscountPolicy implements DiscountPolicy {}

// 가져다 사용하는 곳
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {}
```

- 애노테이션에는 상속이라는 개념이 없음. 이렇게 애노테이션ㅇ르 모아서 사용하는 기능은 스프링이 지원해주는 기능
- @Qualifier 뿐만이 아니라 다른 애노테이션들도 함께 조합해서 사용할 수 있음. 단적으로 @Autowired도 재정의할 수 있음. 몰론 스프링이 제공하는 기능을 뚜렷한 목적 없이 무분별하게 재정의 하는 것은 유지보수에 더 혼란만 가중

## 조회한 빈이 모두 필요할 때, List, Map
- 의도적으로 해당 타입의 스프링 빈이 모두 필요한 경우가 있음
- ex) 할인 서비스를 제공하는데 클라이언트가 할인의 종류(rate, fix)를 선택할 수 있도고 가정. 스프링을 사용하면 전략 패턴을 매운 간단한게 구현 간으

1) src > test > java > hello.core.autowired에 AllBeanTest(class) 생성
- 아무값도 들어가지 않음
2) DiscountPolicy를 가져오기 위해 AutoAppConfig.class 사용
- policyMap은 fixDiscountPolicy와 rateDiscountPolicy가 들어옴
- policies는 스프링 빈 인스턴스 값이 바로 들어옴

(로직 분석)
- DiscountService는 Map으로 모든 DiscountPolicy를 주입 받음. 이때 fixDiscountPolicy, rateDiscountPolicy가 주입됨
- discount() 메서드는 discountCode로 fixDiscountPolicy가 넘어오면 map에서 fixDiscountPolicy 스프링 빈을 찾아서 실행. 물론 rateDiscountPolicy가 넘어오면 fixDiscountPolicy 스프링 빈을 찾아서 실행

(주입 분석)
- Map<String, DiscountPOlicy>: map의 키에 스프링 빈의 이름을 넣어주고, 그 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌
- List<DiscountPolicy>: DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌
- 만약 해당하는 타입의 스피링 빈이 없으면, 빈 컬렉션이나 Map을 주입함

## 자동, 수동의 올바른 실무 운영 기준
(편리한 자동 기능을 기본으로 사용하자)
- 스프링이 나오고 시간이 갈 수혹 점점 자동을 선호하는 추세.
- 스프링은 @Component 뿐만 아니라 @Controller, @Service, @Repository처럼 계층에 맞추어 일반적인 애플리케이션 로직을 자동으로 스캔할 수 있도록 지원
- 최근 스프링 부트는 컴포넌트 스캔을 기본으로 사용하고 스프링 부트의 다양한 스프링 빈들도 조건이 맞음녀 자동으로 등록하도록 설계
- 설정 정보를 기반으로 애플리케이션을 구성하는 부분과 실제 동작하는 부분을 명확하게 나누는 것이 이상적이지만 개발자 입장에서 스피링 빈을 하나 등록할 때 @Component만 넣어주면 끝나는 일을 @Configuration 설정 정보에 가서 @Bean을 적고 객체를 생성하고 주입할 대상을 일일이 적어주는 과정은 상당히 번거롭다. 또한 관리할 빈이 많아서 설정 정보가 커지면 설정 정보를 관리하는 것 자체가 부담이 된다
- 결정적으로 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있다

(수동 빈 등록 사용하는 경우)
- 애플리케이션은 업무 로직과 기술 지원 로직으로 나눌 수 있음
- 업무 로직 빈: 웹을 지원하는 컨트롤러. 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리 등이 모두 업무 로직. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
- 기술 지원 빈: 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용. 데이터베이스 연결이나 공통 로그 처리처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.

- 업무 로직은 숫자도 매우 많고 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리 처럼 어느정도 유사한 패턴이 있다. 이런 경우 자동 기능을 적극 사용하는 것이 좋다. 보통 문제가 발생해도 어떤 곳에서 문제가 발생했는지 명확하게 파악하기 쉽다
- 기술 지원 로직은 업무 로직과 비교해서 그 수가 매우 적고, 보통 애플리케이션 전반에 걸쳐서 광범위하게 영향을 미친다. 그리고 업무 로직은 문제가 발생했을 때 어디가 문제인지 명확하게 잘 들어나지만, 기술 지원 로직은 적용이 잘 되고 있는지 아닌지 조차 파악하기 어려운 경우가 많다. 그래서 이런 기술 지원 로직들은 가급적 수동 빈 등록을 사용해서 명확하게 들어내는 것이 좋다.

- **애플리케이션에 광범위하게 영향을 미티는 기술 지원 객체는 수동 빈으로 등록해서 딱! 설정 정보에 바로 나타나게 하는 것이 유지보수하기 좋다**

(비즈니스 로직 중에서 다형성을 적극 활용할 때)
- 의존관계 자동 주입: 조회한 빈이 모두 필요할 때, List, Map을 다시보자
- DiscountService가 외존관계 자동 주입으로 Map<String, DiscountPolicy>에 주입을 받는 상황을 생각해보자
- 여기에 어떤 빈들이 주입될지, 각 빈들의 이름은 무엇일지 코드만 보고 쉽게 파악할 수 어려울 수 있다. 자동 등록을 사용하고 있기 때문에 파악하려면 여러 코드를 찾아봐야 함
- 이런 경우 수동 빈으로 등록하거나 또는 자동으로하면 특정 패키지에 같이 묶어두는게 좋다. 핵심은 딱 보고 이해가 되어야 한다.

(별도의 설정 정보로 만들고 수동으로 등록하면 다음과 같다)
```java
@Configuration
public class DiscountPolicyConfig {

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }
    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }
}
```
- 해당 설정 정보만 봐도 한눈에 빈의 이름과 어떤 빈들이 주입될지 파악할 수 있음
- 빈 자동 등록을 사용하고 싶으면 파악하기 좋게 DiscountPolicy의 구현 빈들만 따로 모아서 특정 패키지에 모아두자

+) 스프링과 스프링 부트가 자동으로 등록하는 수 많은 빈들은 예외다. 이런 부분들은 스프링 자체를 잘 이해하고 스프링의 의도대로 잘 사용하는게 중요하다. 스프링 부트의 경우 DataSource 같은 데이터베이스 연결에 사용하는 기술 지원 로직까지 내부에서 자동으로 등록하는데, 이런 부분은 메뉴얼을 참고해서 스프링 부트가 의도한 대로 편리하게 사용하면 된다. 반면에 스프링 부트가 아니라 내가 직접 기술 지원 객체를 스프링 빈으로 등록한다면 수동으로 등록해서 명확하게 들어내는 것이 좋다

(정리)
- 편리한 자동 기능을 기본으로 사용하자
- 직접 등록하는 기술 지원 객체는 수동 등록하자
- 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자