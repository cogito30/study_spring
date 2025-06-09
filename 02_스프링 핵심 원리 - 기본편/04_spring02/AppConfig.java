package hello.core;

public class AppConfig {

    // public MemberService memberService() {
    //     return new MemberServiceImpl(new MemoryMemberRepository());
    // }

    // public OrderService orderService() {
    //     return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    // }

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}