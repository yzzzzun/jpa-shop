package com.yzzzzun.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue(value = "M")
public class Movie extends Item {
	private String director;
	private String actor;
}
