package com.yzzzzun.jpashop.repository.queryRepository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

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

}
