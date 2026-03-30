package com.example.crudapp.controler;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;
import com.example.crudapp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ItemControler {
    @Autowired
    private  ItemService itemService;

    public ItemControler(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(path="items")
    public List<ItemDTO> getAllItems() {
        return itemService.findAll();
    }

    @GetMapping(path="items/{id}")
        public ItemDTO getItemById(@PathVariable Long id) {
            return itemService.getItemById(id);
        }

    @PostMapping(path="items")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDTO> create(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.create(itemDTO));
    }

    @PutMapping( path = "items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem(@PathVariable Long id, @RequestBody ItemDTO updatedItem) {
        return itemService.updateItem(id, updatedItem);
    }

    @DeleteMapping(path = "items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}