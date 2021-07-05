package com.yzzzzun.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;

	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		orderItem.changeOrder(this);
	}

	public void changeMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}

	public void changeDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.changeOrder(this);
	}

	protected Order() {

	}

	public Order(Member member, Delivery delivery, LocalDateTime orderDate,
		OrderStatus status) {
		this.member = member;
		this.delivery = delivery;
		this.orderDate = orderDate;
		this.status = status;
	}

	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order(member, delivery, LocalDateTime.now(), OrderStatus.ORDER);
		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		return order;
	}

	public void cancel() {
		if (delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalArgumentException("이미 배송이 완료된 상품은 취소가; 불가능합니다.");
		}
		this.status = OrderStatus.CANCEL;
		for (OrderItem orderItem : this.orderItems) {
			orderItem.cancel();
		}
	}

	public int getTotalPrice() {
		return this.orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
	}

}
