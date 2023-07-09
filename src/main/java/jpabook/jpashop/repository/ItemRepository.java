package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /*상품 저장*/
    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
        }else{
            /*아이템 아이디가 있다면 update를 실행함.*/
            em.merge(item);
        }
    }

    /*상품 한개만 찾기*/
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    /*전체 상품 찾기*/
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
