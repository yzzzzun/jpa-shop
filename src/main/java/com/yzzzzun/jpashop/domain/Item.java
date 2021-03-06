package com.yzzzzun.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import com.yzzzzun.jpashop.exception.NotEnoughStockException;
import com.yzzzzun.jpashop.service.dto.UpdateItemDto;

import lombok.Getter;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();

	protected Item() {
	}

	public Item(String name, int price, int stockQuantity) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}

	public Item(String name, int price, int stockQuantity,
		List<Category> categories) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.categories = categories;
	}

	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public void removeStock(int quantity) {
		int restQuantity = this.stockQuantity - quantity;
		if (restQuantity < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restQuantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void updateInfo(UpdateItemDto updateItemDto) {
		this.price = updateItemDto.getPrice();
		this.name = updateItemDto.getName();
		this.stockQuantity = updateItemDto.getStockQuantity();
	}
}
