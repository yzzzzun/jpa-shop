package com.yzzzzun.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzzzzun.jpashop.domain.Book;
import com.yzzzzun.jpashop.domain.Item;
import com.yzzzzun.jpashop.repository.ItemRepository;
import com.yzzzzun.jpashop.service.dto.UpdateItemDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	@Transactional
	public void updateItem(Long itemId, UpdateItemDto updateItemDto) {
		Item item = itemRepository.findOne(itemId);

		if (item instanceof Book) {
			Book book = (Book)item;
			book.updateBookInfo(updateItemDto);
			return;
		}
		item.updateInfo(updateItemDto);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
