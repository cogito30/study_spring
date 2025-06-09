# 04. 스프링 핵심 원리 이해 2 - 객체 지향 원리 적용

## 새로운 할인 정책 개발

(새로운 할인 정책 확장)
```
- 악덕 기획자: 서비스 오픈 직전에 할인 정책을 지금처럼 고정 금액 할인이 아니라 좀 더 합리적인 주문 금액당 할인한는 정률 할인으로 변경하고 싶습니다. 예를 들어서 기존 정책은 VIP가 10,000원을 주문하든 20,000원을 주문하든 항상 1,000원을 할인했는데 이번에 새로 나온 정책은 10%로 지정해두면 고객이 10,000원 주문시 1,000원을 할인해주고, 2,000원 주문시에 2,000원을 할인해주는 겁니다.
- 순진 개발자: 제가 처음부터 고정 금액 할인은 아니라고 했잖아요.
- 악덕 기획자: 애자일 소프트웨어 개발 선언 몰라요? 계획을 따르기보다 변화에 대응하기를
- 순진 개발자: ... (다행히 유연한 설계가 가능하도록 객체지향 설계 원칙을 준수했지)
```

- [애자일 소프트웨어 개발](https://agilemanifesto.org/iso/ko/manifesto.html)

(새로운 정률 할인 추가)
```
OrderServiceImple -> <interface>DiscountPolicy
                                |
                            ---------
                            |       |
                FixDiscountPolicy  RateDiscountPolicy
```

1) src > main > java > hello.core.discount(package) 생성
2) src > main > java > hello.core.discount에 RateDiscountPolicy(class) 생성
3) src > test > java > hello.core.discount(package) 생성
+) cmd + shift + T로 테스트케이스 생성
4) src > test > java > hello.core.discount에 RateDiscountPolicyText(class) 생성
- 테스트 작성시 실패 테스트도 작성해야 함
+) opt + enter 후 import static 적용

## 새로운 할인 정책 적용과 문제점
1) src > main > java > hello.core.order의 OrderServiceImple(Class) 코드 수정. discountPolicy 구체 변경

(문제점)
- 할인 정책을 변경하려면 클라이언트인 OrderServiceImpl 코드를 수정해야 함
- 역할과 구현을 충실하게 분리. 다형성을 활용. 인터페이스와 구현 객체를 분리
- OCP, DIP 같은 객체지향 설계 원칙을 준수한 것 같지만 실제로는 아니다
- 클래스 의존관계를 분석해야함. 추상(인터페이스)뿐만이 아니라 구체(구현) 클래스에도 의존하고 있어 DIP 위반
- 현재 코드는 기능을 확장해서 변경하며 클라이언트 코드에 영향을 주기 때문에 OCP를 위반

(해결방법)
- DIP를 위반하지 않도록 인터페이스에만 의존하도록 의존관계를 변경
1) src > main > java > hello.core.order의 OrderServiceImpl(Class) 코드 수정. 구현체 제거
- Null Pointer Exception 발생을 해결하기 위해 OrderServiceImpl 구현 객체를 대신 생성해서 주입해야함

## 관심사의 분리
- 관심사 분리란 다양한 책임을 분리하여 역할별로 책임을 가지도록 설계하는 것
- 공연에서 배우는 본인의 역할과 배역을 수행하는 것에 집중하고 공연 기획자는 공연을 구성하고 담당 배우를 섭외하여 배우를 지정하는 책임을 담당. 이처럼 애플리케이션도 역할과 구현을 분리하여 책임을 분리해야 함
- 애플리케이션의 전체 동작 방식을 구성하기 위해 구현 객체를 생성하고 연결하는 책임을 가지는 별도의 설정 클래스인 AppConfig를 생성

1) src > main > java > hello.core의 AppConfig(class) 생성
2) src > main > java > hello.core.member의 MemberServiceImpl(class) 코드 수정. 생성자 추가
3) src > main > java > hello.core.order의 OrderServiceImpl(class) 코드 수정. 생성자 추가
- final이 있다면 반드시 생성자에 할당되어야 함
- DIP 원칙 준수하도록 변경

(AppConfig)
- 애플리케이션의 실제 동작에 필요힌 구현 객체를 생성. MemberServiceImpl, MemoryMemberRepository, OrderServiceImpl, FixDiscountPolicy
- AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입해준다. MemberServiceImpl(MemoryMemberRepository), OrderServiceImpl(MemoryMemberRepository, FixDiscountPolicy)

(MemberServiceImpl)
- 설계변경으로 MemberServiceImpl은 MemoryMemberRepository에 의존하지 않고 MemberRepository 인터페이스만 의존
- MemberServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어오는지 알 수 없음
- MemberServiceImpl의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부 AppConfig에서 결정
- MemberServiceImpl은 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중

(클래스 다이어그램)
```
<interface>MemberService
        |(구현체)
MemberServiceImpl  -> <interface>MemberRepository
        |(^생성)                |(구현체)
    AppConfig  ->  MemoryMemberRepository
              (생성)
```
- 객체의 생성과 연결은 AppConfig가 담당
- DIP 완성: MemberServiceImpl은 MemberRepository인 추상에만 의존하면 됨. 구체 클라스를 몰라도 됨
- 관심사의 분리: 객체를 생성하고 연결하는 역할과 실행하는 역할이 명확히 분리

(회원 객체 인스턴스 다이어그램)
```
memberServiceImpl
        |(2. 생성 + 주입)
    appConfig  ->  memoryMemberRepository(x001)
              (1. 생성)
```
- appConfig 객체는 memoryMemberRepository 객체를 생성하고 그 참조값을 memberServiceImpl을 생성하면서 생성자로 전달
- 클라이언트 memberServiceImpl 입장에서 보면 의존관계를 마치 외부에서 주입해주는 것 같다고 해서 DI(의존관계 주입)이라고 함

(OrderServiceImpl)
- 설계변경으로 OrderServiceImpl은 FixDiscountPolicy를 의존하지 않고 DiscountPolicy 인터페이스만 의존
- OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지 알 수 없음
- OrderServiceImpl의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부 AppConfig에서 결정
- OrderServiceImpl은 실행에만 집중
- OrderServiceImpl에는 MemoryMemberRepository, FIxDiscountPolicy 객체의 의존관계가 주입

(AppConfig를 통한 애플리케이션 실행)
1) src > java > hello.core의 MemberApp(class) 코드 수정
2) src > java > hello.core의 OrderApp(class) 코드 수정
3) 애플리케이션 실행(RUN)후 결과 확인
4) src > test > java > hello.core.member의 MemberServiceTest(class) 수정. BeforeEach 사용하여 처리
5) src > test > java > hello.core.order의 OrderServiceTest(class) 수정. BeforeEach 사용하여 처리
6) Test 실행하여 확인

(정리)
- AppConfig를 통해서 관심사를 분리
- AppConfig는 구체 클래스를 선택. 역할에 맞는 담당 구현 클래스 선택. 애플리케이션이 어떻게 동작해야할지 전체 구성을 책임짐
- 각 구체들은 담당 기능을 실행하는 책임만 지면 됨

## AppConfig 리팩터링
- 현재 AppConfig는 중복이 있고 역할에 따른 구현이 잘 보이지 않음
+) cmd + opt + M으로 메서드 생성. 리턴타입으로 구체 클래스가 아닌 인터페이스로 선택

1) src > main > javav > hello.core의 AppConfig(class)에 코드 추가. 역할이 드러나도록 분리
- new MemoryMemberRepository() 부분의 중복 제거. MemoryMemberRepository를 다른 구현체로 변경할 때 한 부분만 변경하면 됨
- AppConfig를 보면 역할과 구현 클래스가 한눈에 들어오도록 변경됨 

## 새로운 구조와 할인 정책 적용
- 정액 할인 정책을 정률 할인 정책으로 변경
- FixDiscountPolicy -> RateDiscountPolicy
- AppConfig의 등장으로 애플리케이션이 크게 사용 영역과 객체를 생성하고 구성(Configuration)하는 영역으로 분리됨
- 사용 영역과 구성 영역으로 분리되어 정책 변경시 구성 영역만 수정하면 됨

1) src > main > java > hello.core의 AppConfig(class) 코드 수정
2) 애플리케이션 실행 확인
3) src > main > java > hello.core의 MemberApp, OrderApp 실행 확인

- 정책 변경시 애플리케이션의 구성 역할을 담당하는 AppConfig만 변경하면 됨. 클라이언트 코드인 OrderServiceImpl을 포함해서 사용 영역의 어떤 코드도 변경할 필요가 없음
- 구성 영역(AppConfig)은 역할을 모두 알고 있어야 함
- DIP와 OCP 원칙을 지킴

## 전체 흐름 정리
1) 새로운 할인 정책 개발
- 다형성 덕분에 새로운 정률 할인 정책 코드를 추가 개발하는 것에 문제가 없음
- 새로 개발한 정률 할인 정책을 적용하려고 하니 클라이언트 코드인 주문 서비스 구현체도 변경해야하는 문제점 발생 -> DIP 위반
2) 관심사 분리
- 애플리케이션을 하나의 공연으로 생각
- 기존에는 클라이언트가 의존하는 서버 구현 객체를 직접 생성하고 실행
- 애플리케이션의 전체 동작 방식을 구성하기 위해, 구현 객체를 생성하고 연결하는 책임을 가지는 AppConfig 등장
- AppConfig의 등장으로 클라이언트 객체는 자신의 역할을 실행하는 것에만 집중, 권한이 줄어듬(책임이 명확해짐)
3) AppConfig 리팩터링
- 구성 정보에서 역할과 구현을 명확하게 분리
- 역할이 잘 들어나고 중복이 제거됨
4) 새로운 구조와 할인 정책 적용
- AppConfig의 등장으로 애플리케이션이 사용 영역과 객체를 생성하고 구성하는 영역으로 분리
- 할인 정책 변경시 AppConfig가 있는 구성 영역만 변경. 사용 영역은 변경 불필요. 클라이언트 코드인 주문 서비스 코드도 변경하지 않음

## 좋은 객체 지향 설계의 5가지 원칙의 적용
1) SRP(단일 책임 원칙)
- 한 클래스는 하나의 책임만 가져야 함
- SRP를 따르면서 관심사를 분리
- 구현 객체를 생성하고 연결하는 책임은 AppConfig가 담당하도록 분리
- 클라이언트 객체는 실행하는 책임만 담당

2) DIP(의존관계 역전 원칙)
- 프로그래머스는 추상화에 의존해야지, 구체화에 의존하면 안됨
- 새로운 할인 정책을 개발하고, 적용하려고 하니 클라이언트 코드도 함께 변경해야 했음. 기존 클라이언트 코드는 구체화 구현 클래스에 의존함
- 클라이언트 코드가 DiscountPolicy 추상화 인터페이스에만 의존하도록 코드를 변경
- 하지만 클라이언트 코드는 인터페이스만으로 아무것도 실행할 수 없음
- AppConfig가 FixDiscountPolicy 객체 인스턴스를 클라이언트 코드 대신 생성해서 클라이언트 코드에 의존관계를 주입. DIP를 지키며 문제 해결

3) OCP(개방-폐쇄 원칙)
- 소프트웨어 요소는 확장에는 열려 있음나 변경에는 닫혀있어야 함
- 다형성을 사용하고 클라잉너트가 DIP를 지킴
- 애플리케이션을 사용 영역과 구성 영역으로 나눔
- AppConfig가 의존관계를 FixDiscountPolicy -> RateDiscountPolicy로 변경해서 클라이언트 코드에 주입하므로 클라이언트 코드는 변경하지 않아도 됨
- 소프트웨어 요소는 새롭게 확장해도 사용 영역의 변경은 닫혀 있음

## IoC, DI 그리고 컨테이너
(IoC: 제어의 역전)
- 구현 객체는 자신의 로직을 실행하는 역할만 담당
- 프로그램의 제어 흐름 권한은 AppConfig가 가지고 있음
- 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 제어의 역전(IoC)라고 함()

(Framework vs Library)
- Framework: 프레임워크가 본인이 작성한 코드를 제어하고 대신 실행하는 것
- Library: 본인이 작성한 코드가 직접 제어의 흐름을 담당하는 것

(DI: 의존관계 주입)
- 의존관계는 정적인 클래스 관계와 실행 시점에 결정되는 객체(인스턴스) 의존 관계를 분리해서 생각해야 함
- 정적인 클래스 의존관계: 클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단. 애플리케이션을 실행하지 않아도 분석할 수 있음
+) 우클릭 > Diagrams > Show Diagram > Java Class Diagram. 우클릭 > show dependencies
- 동적인 클래스 의존관계: 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존 관계
- DI: 애플리케이션 실행시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것. 객체 인스턴스를 생성하고 그 참ㅈ값을 전달해서 연결
- DI(의존관계주입)을 사용하면 클라이언트 코드를 변경하지 않고 클라이언트가 호출하는 대상의 타입 인스턴스를 변경할 수 있음
- DI를 사용하면 정적인 클래스 의존관계를 변경하지 않고 동적인 클래스 의존관계를 변경할 수 있음

(IoC 컨테이너, DI 컨테이너)
- AppConfig처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 IoC 컨테이너 또는 DI 컨테이너라고 함
- 의존관계 주입에 초점을 맞추어 최근에는 주로 DI 컨테이너라 함(=어셈블러, 오브젝트 팩토리)

## 스프링으로 전환하기
- 순수 자바 코드로만 DI를 적용한 것을 Spring을 사용해서 변경
- AppConfig를 Spring으로 변환
1) src > main > java > hello.core의 AppConfig(class) 수정. AppConfig_spring 확인. @Configuration, @Bean 적용
2) src > main > java > hello.core의 MemberApp(class) 코드 수정
- 로그에 Createing chared instance of singleton bean으로 등록된 스프링 빈 확인
3) src > main > java > hello.core의 OrderApp(class) 코드 수정

(스프링 컨테이너)
- ApplicationContext를 스프링 컨테이너라 함
- 기존에는 개발자가 AppConfig를 사용해서 직접 객체를 생성하고 DI를 했지만 스프링 컨테이너를 통해 사용하는 방식으로 변경
- 스프링 컨테이너는 @Configuration이 붙은 AppConfig를 설정(구성) 정보로 사용. @Bean이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록. 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 함
- 스프링 빈은 @Bean이 붙은 메서드명을 스프링 빈의 이름으로 사용
- 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아햐 함. applicationContext.getBean()메서드를 사용해서 찾음
- 스프링 컨테이너에 객체를 스프링 빈으로 등록하고 스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경

+) @Bean(name="...")으로 사용자 지정 가능
