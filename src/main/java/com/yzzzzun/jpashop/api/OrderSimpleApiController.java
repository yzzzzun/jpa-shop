package com.yzzzzun.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.repository.OrderRepository;
import com.yzzzzun.jpashop.repository.OrderSearch;
import com.yzzzzun.jpashop.repository.OrderSimpleQueryDto;

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

	@GetMapping("/api/v2/simple-orders")
	public List<OrderSimpleQueryDto> ordersV2() {
		return orderRepository.findAll(new OrderSearch(null, null)).stream()
			.map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/simple-orders")
	public List<OrderSimpleQueryDto> ordersV3() {
		return orderRepository.findAllWithMemberDelivery().stream()
			.map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderRepository.findOrderDtos();
	}

}
