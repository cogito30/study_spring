package hello.hellospring.service;

import org.junit.jupiter.api.Test;

class MemberServiceTest {

    // MemberService memberService = new MemberService();
    // MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        membeerService = new MemberService(memberRepository);
    }


    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findeMember.getname());
    }

    // 예외 테스트 - 중복 검증
    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        // try {
        //     memberService.join(member2);
        //     fail();
        // } catch (IllegalStateException e) {
        //     Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        // }

        // assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.")
    }

    @Test
    void findMembers() {

    }

    @Test
    void findOne() {

    }
}