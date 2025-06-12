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

## 애노체이션 직접 만들기

## 조회한 빈이 모두 필요할 때, List, Map

## 자동, 수동의 올바른 실무 운영 기준
  