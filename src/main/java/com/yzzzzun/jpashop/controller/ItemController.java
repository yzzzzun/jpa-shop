package com.yzzzzun.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/{itemId}/edit")
	public String updateItem(@PathVariable("itemId") Long itemId, Model model) {
		Book book = (Book)itemService.findOne(itemId);

		BookForm form = new BookForm();
		form.setId(book.getId());
		form.setName(book.getName());
		form.setPrice(book.getPrice());
		form.setStockQuantity(book.getStockQuantity());
		form.setIsbn(book.getIsbn());
		form.setAuthor(book.getAuthor());

		model.addAttribute("form", form);
		return "items/updateItemForm";
	}

	@PostMapping("/{itemId}/edit")
	public String update(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
		Book book = Book.createBook(form);
		book.setId(form.getId());
		itemService.saveItem(book);
		return "redirect:/items";
	}
}
