package com.example.crudapp.mapperStruct;

import com.example.crudapp.dto.PersonDTO;
import com.example.crudapp.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface PersonMapper {

    PersonDTO toDTO(Person person);

    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonDTO personDTO);
}
