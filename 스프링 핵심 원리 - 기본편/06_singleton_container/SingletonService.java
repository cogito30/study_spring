package hello.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    // 생성자 중복 사용 방지
    private SingletonService() {
    }

    public void login() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}