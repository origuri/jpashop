package jpabook.jpashop.service;

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
