package com.example.crudapp.mapper;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;

public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        return dto;
    }

    public static Item toEntity(ItemDTO dto) {
        Item item = new Item();
        // Do NOT set id — let the DB generate it via @GeneratedValue
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        return item;
    }
}
