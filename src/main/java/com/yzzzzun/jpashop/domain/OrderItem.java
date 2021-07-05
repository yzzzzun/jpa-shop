package com.yzzzzun.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Getter
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private int orderPrice;
	private int count;

	protected OrderItem() {
	}

	public OrderItem(Item item, int orderPrice, int count) {
		this.item = item;
		this.orderPrice = orderPrice;
		this.count = count;
	}

	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		item.removeStock(count);
		return new OrderItem(item, orderPrice, count);
	}

	public void changeOrder(Order order) {
		this.order = order;
	}

	//재고 초기화
	public void cancel() {
		item.addStock(count);
	}

	public int getTotalPrice() {
		return orderPrice * count;
	}
}
