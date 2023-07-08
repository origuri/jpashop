package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 회원 저장하는 로직
    public void save(Member member){
        em.persist(member);
    }
    // pk로 회원 찾기
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }
    // 전체 회원 목록 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    // 이름으로 검색하기
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
