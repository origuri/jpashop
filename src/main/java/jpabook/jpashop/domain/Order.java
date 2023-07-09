package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // enum타입의 주문 상태 [order, cancel]

    // ==연관관계 메서드==// 양방향일 때만
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 == //
    /*
    *  주문 취소
    * */
    public void cancel(){
        // 이미 배송 완료가 되었으면 취소 불가하게
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("배송완료가 된 제품은 취소가 불가능 합니다");
        }
        // 배송 완료가 아니면 버튼을 취소 가능 상태니까 취소로 변경한다.
        this.setStatus(OrderStatus.CANCEL);

        // 취소를 누르면 내가 시켰던 모든 아이템이 취소가 되야한다.
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    /*
     *  전체 주문 가격 조회
     * */
    public int getTotalPrice(){
        int totalPrice = 0;
        // 사과 3개 바나나 2개를 샀다고 하면 사과 *3 + 바나나 *2 해야 총 값이 나옴.
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
