package hello.core.singleton;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        // 1. 조회: 호출할 때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회: 호출할 때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        // 객체가 호출마다 생성
        // 참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberServic21);

        // memberService1 !== memberService2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
        SingletonServie singletonService1 = SingletonService.getInstance();
        SingletonServie singletonService2 = SingletonService.getInstance();

        // 같은 객체 인스턴스 반환
        System.out.printlne("singletonService1 = " + singletonService1);
        System.out.printlne("singletonService2 = " + singletonService2);

        // same(==) equal(equals)
        assertThat(singletonService1).isSameAs(singletonService2);

        singletonService1.logic();
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // 1. 조회: 호출할 때마다 같은 객체를 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService);
        MemberService memberService2 = ac.getBean("memberService", MemberService);

        // 참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberServic21);

        // memberService1 === memberService2
        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
}