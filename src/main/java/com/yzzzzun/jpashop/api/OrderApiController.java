package com.yzzzzun.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Address;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.domain.OrderItem;
import com.yzzzzun.jpashop.domain.OrderStatus;
import com.yzzzzun.jpashop.repository.OrderRepository;
import com.yzzzzun.jpashop.repository.OrderSearch;

import lombok.Data;

@RestController
public class OrderApiController {

	private final OrderRepository orderRepository;

	public OrderApiController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAll(new OrderSearch(null, null));
		for (Order order : orders) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			order.getOrderItems().forEach(o -> o.getItem().getName());
		}
		return orders;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		return orderRepository.findAll(new OrderSearch(null, null)).stream()
			.map(OrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		return orderRepository.findALlWithItem().stream()
			.map(OrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersPage(@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "100") int limit) {
		return orderRepository.findAllWithMemberDelivery(offset, limit).stream()
			.map(OrderDto::new)
			.collect(Collectors.toList());
	}

	@Data
	static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		public OrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress();

			this.orderItems = order.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(Collectors.toList());
		}
	}

	@Data
	static class OrderItemDto {
		private String itemName;
		private int orderPrice;
		private int count;

		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
}
