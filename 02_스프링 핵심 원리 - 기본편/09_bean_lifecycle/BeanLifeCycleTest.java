package hello.core.lifecycle;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        // close 사용을 위해. 상위 인터페이스인 ConfigurableApplicationContext 사용
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        // @Bean(initMethod = "init", destroyMethod = "close")
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hell0-spring.dev");
            return networkClient;
        }
    }
}