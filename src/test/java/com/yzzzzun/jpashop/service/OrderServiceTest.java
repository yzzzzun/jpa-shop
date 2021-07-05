package com.yzzzzun.jpashop.service;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.yzzzzun.jpashop.domain.Address;
import com.yzzzzun.jpashop.domain.Book;
import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.domain.OrderStatus;
import com.yzzzzun.jpashop.exception.NotEnoughStockException;
import com.yzzzzun.jpashop.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Test
	void 상품주문() {
		//given
		Member member = createMember();

		Book book = createBook("jpaBook", 30000, 10);
		//when
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		//then
		Order findOrder = orderRepository.findOne(orderId);

		assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
		assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
		assertThat(findOrder.getTotalPrice()).isEqualTo(60000);
		assertThat(book.getStockQuantity()).isEqualTo(8);
	}

	@Test
	void 주문취소() {
		//given
		Member member = createMember();
		Book book = createBook("jpaBook", 30000, 10);

		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		//when
		orderService.cancel(orderId);

		Order order = orderRepository.findOne(orderId);

		//then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
		assertThat(book.getStockQuantity()).isEqualTo(10);
	}

	@DisplayName("상품을 재고 이상으로 주문할경우 오류")
	@Test
	void 상품주문_재고수량초과() {
		Member member = createMember();
		Book book = createBook("jpaBook", 30000, 10);

		int orderCount = 20;
		Assertions.assertThatThrownBy(() -> {
			orderService.order(member.getId(), book.getId(), orderCount);
		}).isInstanceOf(NotEnoughStockException.class);
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book(name, price, stockQuantity, null, null, null);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member("member1", new Address("서울", "경기", "123"), null);
		em.persist(member);
		return member;
	}
}