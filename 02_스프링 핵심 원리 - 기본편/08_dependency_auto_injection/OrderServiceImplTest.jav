package hello.core.order;

import org.junit.jupiter.api.Test;

import static org.juint.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    // @Test
    // void createOrder() {
    //     OrderServiceImpl orderService = new OrderServiceImpl();
    //     orderService.createOrder(1L, "itemA", 10000);
    // }

    @Test
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}