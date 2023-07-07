package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false)
    void save() {
        //given 상황을 주는 것. 오리라는 사람이 가입하려고 한다.
        Member member = new Member();
        member.setUsername("오리");

        //when  // 오리라는 사람이 가입을 했다.
        Long saveId = memberRepository.save(member);

        //then // 진짜로 가입이 되었는지 확인한다.
        Member findMember = memberRepository.find(saveId);
        Assertions.assertThat(member.getId()).isEqualTo(findMember.getId());
        Assertions.assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        // 트렌젝션 안에서 같은 id(pk)값을 가지고 잇으면 영속성 컨텍스트로 인해 둘은 같은 객체이다.
        Assertions.assertThat(member).isEqualTo(findMember);
    }

    @Test
    void find() {
    }
}