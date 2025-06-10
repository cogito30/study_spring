package hello.core.singleton;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {

        // @Bean memberService -> new MemoryMemberRepository()
        // @Bean orderService -> new MemoryMemberRepository()

        // 예상
        // call AppConfig.memberService
        // call AppConfig.memberRepository
        // call AppConfig.memberRepository
        // call AppConfig.orderService
        // call AppConfig.memberReposiroty

        // 실제
        // call AppConfig.memberService
        // call AppConfig.memberRepository
        // call AppConfig.orderService

        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", orderService.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        // 3개 모두 동일
        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("memberService -> memberRepository2 = " + memberRepository2);
        System.out.println("emberRepository = " + memberRepository);

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep() {
        // AppConfig.class를 넘기면 스프링 빈으로 등록됨
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
    }
}