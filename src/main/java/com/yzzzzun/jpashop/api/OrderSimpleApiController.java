package com.yzzzzun.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Address;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.domain.OrderStatus;
import com.yzzzzun.jpashop.repository.OrderRepository;
import com.yzzzzun.jpashop.repository.OrderSearch;

import lombok.Data;

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
	public List<SimpleOrderDto> ordersV2() {
		return orderRepository.findAll(new OrderSearch(null, null)).stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress();
		}
	}
}
