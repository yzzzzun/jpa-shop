package com.yzzzzun.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yzzzzun.jpashop.domain.Book;
import com.yzzzzun.jpashop.domain.Item;
import com.yzzzzun.jpashop.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("form") BookForm form) {
		Book book = Book.createBook(form);
		itemService.saveItem(book);
		return "redirect:/";
	}

	@GetMapping
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}
}
