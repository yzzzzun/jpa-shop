package com.yzzzzun.jpashop.domain;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue(value = "B")
public class Book extends Item {
	private String author;
	private String isbn;

	protected Book() {
	
	}

	public Book(String author, String isbn) {
		this.author = author;
		this.isbn = isbn;
	}

	public Book(String name, int price, int stockQuantity, List<Category> categories, String author,
		String isbn) {
		super(name, price, stockQuantity, categories);
		this.author = author;
		this.isbn = isbn;
	}
}
