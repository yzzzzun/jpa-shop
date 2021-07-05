package com.yzzzzun.jpashop.repository;

import com.yzzzzun.jpashop.domain.OrderStatus;

import lombok.Getter;

@Getter
public class OrderSearch {
	private String memberName;
	private OrderStatus orderStatus;

	protected OrderSearch() {
	}

	public OrderSearch(String memberName, OrderStatus orderStatus) {
		this.memberName = memberName;
		this.orderStatus = orderStatus;
	}
}
