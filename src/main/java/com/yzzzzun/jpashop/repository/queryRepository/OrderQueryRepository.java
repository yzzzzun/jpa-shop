package com.yzzzzun.jpashop.repository.queryRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.yzzzzun.jpashop.repository.OrderFlatDto;
import com.yzzzzun.jpashop.repository.OrderItemQueryDto;
import com.yzzzzun.jpashop.repository.OrderQueryDto;

@Repository
public class OrderQueryRepository {

	private final EntityManager entityManager;

	public OrderQueryRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> orderQueryDtos = findOrders();

		orderQueryDtos.forEach(orderDto -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(orderDto.getOrderId()); // N+1 문제
			orderDto.setOrderItems(orderItems);
		});

		return orderQueryDtos;
	}

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return entityManager.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi"
				+ " join oi.item i"
				+ " where oi.order.id = :orderId", OrderItemQueryDto.class)
			.setParameter("orderId", orderId)
			.getResultList();
	}

	private List<OrderQueryDto> findOrders() {
		return entityManager.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o"
				+ " join o.member m"
				+ " join o.delivery d", OrderQueryDto.class)
			.getResultList();
	}

	public List<OrderQueryDto> findAllByDto_Optimization() {
		List<OrderQueryDto> orders = findOrders();
		List<Long> orderIds = toOrderIds(orders);

		Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
		orders.forEach(orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));

		return orders;
	}

	private List<Long> toOrderIds(List<OrderQueryDto> orders) {
		return orders.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());
	}

	private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
		List<OrderItemQueryDto> orderItems = entityManager.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi"
				+ " join oi.item i"
				+ " where oi.order.id in :orderIds", OrderItemQueryDto.class)
			.setParameter("orderIds", orderIds).getResultList();

		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
			.collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
		return orderItemMap;
	}

	public List<OrderFlatDto> findAllByDto_flat() {
		return entityManager.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) from Order o"
				+ " join o.member m"
				+ " join o.delivery d"
				+ " join o.orderItems oi "
				+ " join oi.item i", OrderFlatDto.class)
			.getResultList();
	}
}
