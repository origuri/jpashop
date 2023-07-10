package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    /*
    * 아이템 저장 로직
    * 조회용이 아니기 때문에 트렌젝셔널 어노테이션을 따로 줘야 함.
    * */
    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void itemUpdate(long id, String name, int price, int quantity){
        // 현재 findItem은 createQuery로 찾아왔기 때문에 영속 상태
        Item findItem = itemRepository.findOne(id);

        // 영속 상태인 findItem에 set을 해주면 변경 감지가 일어나서 자동으로 update 쿼리가 실행됨.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(quantity);
    }

    /*
    * 단건 조회
    * */
    public List<Item> findItem(){
        return itemRepository.findAll();
    }

    /*
    * 모든 아이템 조회
    * */
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
