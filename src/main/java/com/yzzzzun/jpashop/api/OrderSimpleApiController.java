package com.yzzzzun.jpashop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.repository.OrderRepository;
import com.yzzzzun.jpashop.repository.OrderSearch;

@RestController
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	public OrderSimpleApiController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		return orderRepository.findAll(new OrderSearch(null, null));
	}
}
