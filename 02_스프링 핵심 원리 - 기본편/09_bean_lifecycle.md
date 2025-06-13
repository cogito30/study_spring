# 09 빈 생명주기 콜백

## 빈 생명주기 콜백 시작
- 데이터베이스 커넥션 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리해 두고, 애플리케이션 종류 시점에 연결을 모두 종료하는 작업을 진행하려면, 객체의 초기화와 종료 작업이 필요하다
- 스프링을 통해 이러한 초기화 작업과 종료 작업을 어떻게 진행하는지 확인해보자

- 간단하게 외부 네트워크에 미리 연결한 객체를 하나 생성한다고 가정. 실제로 네트워크 연결이 아닌 단순히 문자만 출력하도록 함. NetworkClient는 애플리케이션 시작 지점에 connect()를 호출해서 연결을 맺어두어야하고, 애플리케이션이 종료되면 disConnect()를 호출해서 연결을 끊어야 함

1) src > test > javav > hello.core.lifecycle(package) 생성
2) src > test > javav > hello.core.lifecycle에 NetworkClient(class) 생성
3) src > test > java > hello.core.lifecycle에 BeanLifeCycle(class) 생성
- 생성자 부분을 보면 url 정보 없이 connect가 호출되는 것을 확인할 수 있음
- 객체를 생성하는 단계에는 url이 없고 객체를 생성한 다음에 외부에서 수정자 주입을 통해서 setUrl()이 호출되어야 url이 존재하게 됨

- 스프링 빈은 다음과 같은 LifeCycle을 가짐
- **객체 생성 -> 의존관계 주입**

- 스프링 빈은 객체를 생성하고 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료됨. 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출해야 함
- 개발자가 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까
- 스프링은 의존관계 중비이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공. 또한 스프링은 스프링 컨테이너가 종료되기 직전에 솜려 콜백을 줌. 따라서 안전하게 종료 작업을 진행할 수 있음

(스프링 빈의 이벤트 라이프 사이클)
- 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
- 초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
- 소멸전 콜백: 빈이 소멸되기 직전에 호출

- 스프링은 다양한 방식으로 생명주기 콜백을 지원

(객체의 생성과 초기화를 분리하자)
- 생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다. 반면에 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는 등 무거운 동작을 수행
- 따라서 생성자 안에서 무거운 초기화 작업을 함께하는 것보다는 객체를 생성하는 부분과 초기화하는 부분을 명확하게 나누는 것이 유지보수 관점에서 좋음. 물론 초기화 작업이 내무 값들만 약간 변경하는 정도로 단순한 경우에는 생성자에서 한번에 다 처리하는게 나을 수 있음

(스프링의 빈 생명주기 콜백 지원 방법 3가지)
1) 인터페이스(InitializingBean, DisposableBean)
2) 설정 정보에 초기화 메서드, 종료 메서드 지정
3) @PostConstruct, @PreDestory 애노테이션 지원

## 인터페이스 InitializingBean, DisposableBean
1) src > test > javav > hello.core.lifecycle에 NetworkClient(class) 코드 추가. InitializingBean, DistroyBean 상속
- InitializingBean은 afterPropertiesSet() 메서드로 초기화를 지원
- DisposableBean은 destroy() 메서드로 소멸을 지원
- 출력 결과를 보면 초기화 메서드가 주입 완료 후에 적절하게 호출된 것을 확인
- 스프링 컨테이너의 종료가 호출되자 소멸 메서드가 호출된 것도 확인

(초기화, 소멸 인터페이스 단점)
- 이 인터페이스는 스프링 전용 인터페이스. 해당 코드가 스프링 전용 인터페이스에 의존함
- 초기화, 소명 메서드의 이름을 변경할 수 없음
- 본인이 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없음

+) 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법. 지금은 더 나은 방법이 있어서 잘 사용하지 않음

## 빈 등록 초기화, 소멸 메서드
- 설정 정보에 @Bean(initMethod = "init", destroyMethod = "close")처럼 초기화, 소멸 메서드를 지정할 수 있음
1) src > test > javav > hello.core.lifecycle에 NetworkClient(class) 코드 수정
2) src > test > javav > hello.core.lifecycle에 BeanLifeCycleTEst(class) 코드 수정. @Bean에 파라미터 추가

(설정 정보 사용 특징)
- 메서드 이름을 자유롭게 줄 수 있음
- 스프링 빈이 스프링 코드에 의존하지 않음
- 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있음

(종료 메서드 추론)
- @Bean의 destroyMethod 속성에는 특별한 기능이 있음
- 라이브러리는 대부분 close, shutdown이라는 이름의 종료 메서드를 사용
- @Bean의 destroyMethod는 기본값이 (inferred)로 등록 도미
- 이 추론 기능은 close, shutdown이라는 이름의 메서드를 자동으로 호출해 줌. 이름 그대로 종료 메서드를 추론해서 호출
- 따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작
- 추론 기능을 사용하기 싫은면 destroyMethod="" 처럼 빈 공백을 지정하면 됨

## 애노테이션 @PostConstruct, @PreDestroy
- 현재 주로 사용하는 방법
1) src > test > javav > hello.core.lifecycle에 NetworkClient(class) 코드 수정
2) src > test > javav > hello.core.lifecycle에 BeanLifeCycleTEst(class) 코드 수정. @Bean에 파라미터 제거

(@PostConstruct, @PreDestroy 애노테이션 특징)