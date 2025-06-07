package hello.hellospring.repository;

import org.junit.jupiter.api.Test;
import static org.asssertj.core.api.Assertions.*;


class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        // System.out.println("result = " + (result == member));
        // Assertions.assertEquals(memeber, result); // org.junit.jupiter.api
        assertThat(member).isEqualTo(result); // org.assertj.core.api
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findBYName("spring1").get();

        assetThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}