package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 책, 앨범, 영화 칼럼이 아이템 테이블에 전부 들어가는 것.
@DiscriminatorColumn(name="Dtype") // db에서 책, 앨범, 영화를 구분할 때 쓰는 구분자
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    /*
    *  stock 증가
    * */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /*
    *
    *  stock 감소 0보다 작아지면 안됨!
    * */
    public void removeStock(int quantity){
       int restStock = stockQuantity - quantity;
       if(restStock < 0){
           throw new NotEnoughStockException("재고량은 0미만이 될 수 없습니다. ");
       }
       this.stockQuantity = restStock;
    }

}
