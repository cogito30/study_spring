package hello.core.order;

@Component
@RequiredArgsConstructor
public class OrderServiceImple implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // public OrderServeiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    //     this.memberRepository = memberRepository;
    //     this.discountPolicy = discountPolicy;
    // }

    @Autowired 
    private DiscountPolicy rateDiscountPolicy;

    @Autowired
    public OrderServeiceImpl(MemberRepository memberRepository, DiscountPolicy reateDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = rateDiscountPolicy;
    }

    @Override
    public OrderCreate(...) {
        ...
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}