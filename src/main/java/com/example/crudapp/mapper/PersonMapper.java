package com.example.crudapp.mapper;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.dto.PersonDTO;
import com.example.crudapp.entity.Item;
import com.example.crudapp.entity.Person;

import java.util.List;

public class PersonMapper {

    public static PersonDTO toDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setAge(person.getAge());

        if(person.getItems() != null) {
            List<ItemDTO> itemsDTOs = person.getItems().stream().map(ItemMapper::toDTO).toList();
            dto.setItems(itemsDTOs);
        }
        return dto;
    }

    public static Person toEntity(PersonDTO dto) {
        Person person = new Person();
        person.setName(dto.getName());
        person.setAge(dto.getAge());
        if(dto.getItems() != null) {
            List<Item> items = dto.getItems().stream().map(ItemMapper::toEntity).toList();
            person.setItems(items);
        }
        return person;
    }

}
