package com.yzzzzun.jpashop.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yzzzzun.jpashop.domain.Order;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	private final EntityManager em;

	public void save(Order order) {
		this.em.persist(order);
	}

	public Order findOne(Long orderId) {
		return em.find(Order.class, orderId);
	}

	public List<Order> findAll(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);
		Join<Object, Object> m = o.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}

		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> typedQuery = em.createQuery(cq).setMaxResults(1000);
		return typedQuery.getResultList();
	}

	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery("select o from Order o"
			+ " join fetch o.member m"
			+ " join fetch o.delivery d", Order.class).getResultList();
	}
}
