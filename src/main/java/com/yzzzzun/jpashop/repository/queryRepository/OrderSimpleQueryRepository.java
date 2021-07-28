package com.yzzzzun.jpashop.repository.queryRepository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.yzzzzun.jpashop.repository.OrderSimpleQueryDto;

@Repository
public class OrderSimpleQueryRepository {

	private final EntityManager entityManager;

	public OrderSimpleQueryRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<OrderSimpleQueryDto> findOrderDtos() {
		return entityManager.createQuery(
			"select new com.yzzzzun.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address ) from Order o"
				+ " join o.member m"
				+ " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
	}
}
