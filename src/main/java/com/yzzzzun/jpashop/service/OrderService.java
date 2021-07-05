package com.yzzzzun.jpashop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzzzzun.jpashop.domain.Delivery;
import com.yzzzzun.jpashop.domain.Item;
import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.domain.OrderItem;
import com.yzzzzun.jpashop.repository.ItemRepository;
import com.yzzzzun.jpashop.repository.MemberRepository;
import com.yzzzzun.jpashop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		Member member = memberRepository.find(memberId);
		Item item = itemRepository.findOne(itemId);

		Delivery delivery = new Delivery(null, null, member.getAddress());
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		Order order = Order.createOrder(member, delivery, orderItem);

		orderRepository.save(order);
		return order.getId();
	}

	//취소
	@Transactional
	public void cancel(Long orderId) {
		Order order = orderRepository.findOne(orderId);
		order.cancel();
	}

}

