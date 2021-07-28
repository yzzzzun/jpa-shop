package com.yzzzzun.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.repository.OrderRepository;
import com.yzzzzun.jpashop.repository.OrderSearch;
import com.yzzzzun.jpashop.repository.OrderSimpleQueryDto;
import com.yzzzzun.jpashop.repository.queryRepository.OrderSimpleQueryRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	public OrderSimpleApiController(OrderRepository orderRepository,
		OrderSimpleQueryRepository orderSimpleQueryRepository) {
		this.orderRepository = orderRepository;
		this.orderSimpleQueryRepository = orderSimpleQueryRepository;
	}

	@GetMapping("/api/v1/simple-orders")
	public Result ordersV1() {
		List<Order> orders = orderRepository.findAll(new OrderSearch(null, null));
		return new Result(orders);
	}

	@GetMapping("/api/v2/simple-orders")
	public Result ordersV2() {
		List<OrderSimpleQueryDto> orderDtos = orderRepository.findAll(new OrderSearch(null, null)).stream()
			.map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
		return new Result(orderDtos);
	}

	@GetMapping("/api/v3/simple-orders")
	public Result ordersV3() {
		List<OrderSimpleQueryDto> dtos = orderRepository.findAllWithMemberDelivery().stream()
			.map(OrderSimpleQueryDto::new)
			.collect(Collectors.toList());
		return new Result(dtos);
	}

	@GetMapping("/api/v4/simple-orders")
	public Result ordersV4() {
		return new Result(orderSimpleQueryRepository.findOrderDtos());
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Result<T> {
		private T data;
	}
}
