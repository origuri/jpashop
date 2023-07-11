package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name ="order_item_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //== 생성 메소드==//
    // 가격을 아이템 엔티티 꺼를 사용안하는 이유는 할인이나 쿠폰 사용할 수 있기 때문
    public static OrderItem createOrderItem(Item item , int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 내가 산 아이템은 재고수량이 줄어들어야 함.
        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직==//
    public void cancel(){
        // 취소 했으니까 수량을 원복 해줘야 한다.
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        // 주문 가격과 주문 수량을 곱해야 하기 때문
        return getOrderPrice() * getCount();
    }
}
