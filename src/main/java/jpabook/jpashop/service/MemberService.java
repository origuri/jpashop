package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)// 조회만 하는 경우에 트렌젝션을 따로 걸어주고 readOnly를 해주면 성능 최적화가 된다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    // 회원 가입 - 같은 이름을 가진 사람은 가입 x
    @Transactional
    public Long join(Member member) {
        validateDuplidateMember(member);// 중복회원
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplidateMember(Member member) {
        // 문제가 있으면 여기서 예외 터짐.
        // 이렇게 하는 것보다 length가 0보다 크다 라고 하는게 좀 더 최적화에 도움이 됨.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            // 잘못된 상태라고 알려주는 예외
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회 : 조회만 하는 경우에 트렌젝션을 따로 걸어주고 readOnly를 해주면 성능 최적화가 된다.
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    // 회원 전체 조회 : 조회만 하는 경우에 트렌젝션을 따로 걸어주고 readOnly를 해주면 성능 최적화가 된다.
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }



}
