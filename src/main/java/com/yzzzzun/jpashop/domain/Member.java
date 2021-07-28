package com.yzzzzun.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.yzzzzun.jpashop.controller.MemberForm;

import lombok.Getter;

@Getter
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String name;

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

	protected Member() {
	}

	public Member(String name, Address address, List<Order> orders) {
		this.name = name;
		this.address = address;
		this.orders = orders;
	}

	public Member(String name) {
		this.name = name;
	}

	public Member(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public static Member createMember(MemberForm memberForm) {
		Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
		return new Member(memberForm.getName(), address);
	}

	public void updateName(String name) {
		this.name = name;
	}
}
