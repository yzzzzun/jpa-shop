package com.yzzzzun.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue(value = "A")
public class Album extends Item {
	private String artist;
	private String etc;
}
