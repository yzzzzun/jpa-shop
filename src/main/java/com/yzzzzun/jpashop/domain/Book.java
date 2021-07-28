package com.yzzzzun.jpashop.domain;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.yzzzzun.jpashop.controller.BookForm;
import com.yzzzzun.jpashop.service.dto.UpdateItemDto;

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

	public Book(String name, int price, int stockQuantity, String author, String isbn) {
		super(name, price, stockQuantity);
		this.author = author;
		this.isbn = isbn;
	}

	public static Book createBook(BookForm bookForm) {
		return new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(), bookForm.getAuthor(),
			bookForm.getIsbn());
	}

	public void updateBookInfo(UpdateItemDto updateItemDto) {
		updateInfo(updateItemDto);
		this.author = updateItemDto.getAuthor();
		this.isbn = updateItemDto.getIsbn();
	}
}
