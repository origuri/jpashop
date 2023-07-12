package jpabook.jpashop.repository.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        // 전체 주문을 가저옴.
        List<OrderQueryDto> result = findOrders(); // n + 1문제 발생

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


    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        // 전체 리스트 뽑은 걸 map으로 orderId 리스트로 매핑해줌. (4, 11)이 들어있음.
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        // orderIds를 파라미터로 in 절로 한번에 orderItems를 가져옴. (4개의 상품.)
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)  " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds",
                OrderItemQueryDto.class
        ).setParameter("orderIds", orderIds).getResultList();

        // 그걸 orderId를 키값으로 map에다가 넣어놓음
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        // order를 for 문으로 풀어서 orderItems에 map의 value(orderId가 키값)을 꺼내서 넣음.
        result.forEach(orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));

        return result;
    }
}
