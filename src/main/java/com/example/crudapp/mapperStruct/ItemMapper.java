package com.example.crudapp.mapperStruct;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO toDTO(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    Item toEntity(ItemDTO itemDTO);
}
