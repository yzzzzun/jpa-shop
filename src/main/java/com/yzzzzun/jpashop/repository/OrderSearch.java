package com.yzzzzun.jpashop.repository;

import com.yzzzzun.jpashop.domain.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
