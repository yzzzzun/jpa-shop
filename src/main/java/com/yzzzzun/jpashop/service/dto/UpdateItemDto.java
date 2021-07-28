package com.yzzzzun.jpashop.service.dto;

import com.yzzzzun.jpashop.controller.BookForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
	private int price;
	private String name;
	private int stockQuantity;
	private String author;
	private String isbn;

	public UpdateItemDto(int price, String name, int stockQuantity, String author, String isbn) {
		this.price = price;
		this.name = name;
		this.stockQuantity = stockQuantity;
		this.author = author;
		this.isbn = isbn;
	}

	public static UpdateItemDto of(BookForm form) {
		return new UpdateItemDto(form.getPrice(), form.getName(), form.getStockQuantity(),
			form.getAuthor(), form.getIsbn());
	}
}
