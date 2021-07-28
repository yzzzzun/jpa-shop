package com.yzzzzun.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yzzzzun.jpashop.domain.Address;
import com.yzzzzun.jpashop.domain.Book;
import com.yzzzzun.jpashop.domain.Delivery;
import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.domain.OrderItem;

@Component
public class InitDb {

	private final InitService initService;

	public InitDb(InitService initService) {
		this.initService = initService;
	}

	@PostConstruct
	public void init() {
		initService.dbInitFirst();
		initService.dbInitSecond();
	}

	@Component
	@Transactional
	static class InitService {

		private final EntityManager em;

		public InitService(EntityManager em) {
			this.em = em;
		}

		public void dbInitFirst() {
			Member member = createMember("userA", "서울", "1", "1111");
			em.persist(member);

			Book jpaBookFirst = createBook("JPA1 BOOK", 10000, 100);
			em.persist(jpaBookFirst);

			Book jpaBookSecond = createBook("JPA2 BOOK", 20000, 200);
			em.persist(jpaBookSecond);

			OrderItem firstBookOrderItem = OrderItem.createOrderItem(jpaBookFirst, 10000, 1);
			OrderItem secondBookOrderItem = OrderItem.createOrderItem(jpaBookSecond, 20000, 2);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, firstBookOrderItem, secondBookOrderItem);
			em.persist(order);
		}

		public void dbInitSecond() {
			Member member = createMember("userB", "수원", "2", "2222");
			em.persist(member);

			Book springBookFirst = createBook("SPRING1 BOOK", 20000, 200);
			em.persist(springBookFirst);

			Book springBookSecond = createBook("SPRING2 BOOK", 40000, 300);
			em.persist(springBookSecond);

			OrderItem firstBookOrderItem = OrderItem.createOrderItem(springBookFirst, 20000, 3);
			OrderItem secondBookOrderItem = OrderItem.createOrderItem(springBookSecond, 40000, 4);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, firstBookOrderItem, secondBookOrderItem);
			em.persist(order);
		}

		private Delivery createDelivery(Member member) {
			return new Delivery(member.getAddress());
		}

		private Book createBook(String name, int price, int stockQuantity) {
			return new Book(name, price, stockQuantity);
		}

		private Member createMember(String name, String city, String street, String zipcode) {
			return new Member(name, new Address(city, street, zipcode));
		}
	}

}
