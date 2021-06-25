package com.yzzzzun.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue(value = "B")
public class Book extends Item {
	private String author;
	private String isbn;
}
