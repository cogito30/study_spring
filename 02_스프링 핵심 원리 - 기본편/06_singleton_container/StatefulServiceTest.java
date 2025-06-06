package hello.core.singleton;

public class StatefulService {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: A사용자 10000운 주문
        // statefulService1.order("userA", 10000);
        int userAPrice = statefulService1.order("userA", 10000);
        // ThreadB: B사용자 20000운 주문
        int userBPrice = statefulService2.order("userA", 20000);

        // ThreadA: 사용자A 주문 금액 조회
        // int price = statefulService1.getPrice();
        // System.out.println("price = " + price);
        System.out.println("price = " + userAPrice);

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}