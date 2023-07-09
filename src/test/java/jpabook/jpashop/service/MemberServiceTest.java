package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void 회원가입() {
        // given
        Member member1 = new Member();
        member1.setName("오리");

        // when
        Long savedId = memberService.join(member1);

        // then
        assertEquals(member1, memberRepository.findOne(member1.getId()));

    }

    @Test
    void 중복회원예외() {
        // given
        String name = "오리";
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName(name);
        member2.setName(name);

        // when
        memberService.join(member1);

        // then
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));

    }

}