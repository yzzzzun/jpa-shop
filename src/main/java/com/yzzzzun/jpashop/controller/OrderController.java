package com.yzzzzun.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yzzzzun.jpashop.domain.Item;
import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.domain.Order;
import com.yzzzzun.jpashop.repository.OrderSearch;
import com.yzzzzun.jpashop.service.ItemService;
import com.yzzzzun.jpashop.service.MemberService;
import com.yzzzzun.jpashop.service.OrderService;

@Controller
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;

	public OrderController(OrderService orderService, MemberService memberService,
		ItemService itemService) {
		this.orderService = orderService;
		this.memberService = memberService;
		this.itemService = itemService;
	}

	@GetMapping("/order")
	public String createForm(Model model) {

		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();

		model.addAttribute("members", members);
		model.addAttribute("items", items);

		return "order/orderForm";
	}

	@PostMapping("/order")
	public String create(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId,
		@RequestParam("count") int count) {
		this.orderService.order(memberId, itemId, count);
		return "redirect:/orders";
	}

	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		List<Order> orders = orderService.getOrders(orderSearch);
		model.addAttribute("orders", orders);
		return "order/orderList";
	}

	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancel(orderId);
		return "redirect:/orders";
	}
}
