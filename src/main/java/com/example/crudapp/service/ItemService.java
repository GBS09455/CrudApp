package com.example.crudapp.service;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;
import com.example.crudapp.exception.ItemAlreadyExistsException;
import com.example.crudapp.mapper.ItemMapper;
import com.example.crudapp.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.crudapp.mapper.ItemMapper.toEntity;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        return ItemMapper.toDTO(item);
    }
    @Transactional
    public ItemDTO create(ItemDTO dto) {
        if (itemRepository.existsByName(dto.getName())) {
            throw new ItemAlreadyExistsException("Item with name:" +dto.getName() + " already exists.");
        }
        Item entity = toEntity(dto);
        Item savedEntity = itemRepository.save(entity);
        return ItemMapper.toDTO(savedEntity);
    }
    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO updatedItem) {
        Item existingItem = itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Item with id " + id + " not found"));
        existingItem.setName(updatedItem.getName());
        existingItem.setDescription(updatedItem.getDescription());
        Item savedItem = itemRepository.save(existingItem);
        return ItemMapper.toDTO(savedItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item with id " + id + " not found");
        }
        itemRepository.deleteById(id);
    }
}
