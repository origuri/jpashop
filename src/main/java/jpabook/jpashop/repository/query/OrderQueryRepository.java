package jpabook.jpashop.repository.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        // 전체 주문을 가저옴.
        List<OrderQueryDto> result = findOrders();

        // stream으로 풀어서 orderId로 다시 item을 찾아옴.
        result.stream().forEach(o-> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            // result에 다시 세팅해줌.
            o.setOrderItems(orderItems);
        });
        return result;
    }
    // 전체 주문을 가져오는 메소드
    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        "from Order o join o.member m " +
                        "join o.delivery d",
                OrderQueryDto.class
        ).getResultList();
    }
    // order_id로 item을 가져오는 메소드
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)  " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id = :orderId",
                OrderItemQueryDto.class
        ).setParameter("orderId", orderId).getResultList();
    }



}
