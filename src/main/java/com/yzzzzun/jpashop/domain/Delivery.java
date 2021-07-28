package com.yzzzzun.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;

@Getter
@Entity
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private DeliveryStatus status;

	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;

	@Embedded
	private Address address;

	public void changeOrder(Order order) {
		this.order = order;
	}

	protected Delivery() {
	}

	public Delivery(DeliveryStatus status, Order order, Address address) {
		this.status = status;
		this.order = order;
		this.address = address;
	}

	public Delivery(Address address) {
		this.address = address;
	}
}
