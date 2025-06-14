package hello.core.member;

@Component
public class MemberServiceImple implements MemberService {

    private final MemberRepository MemberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) { memberRepository.save(member); }

    @Override
    public Member findMember(Long memberId) { return memberRepository.findById(memberId); }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

}