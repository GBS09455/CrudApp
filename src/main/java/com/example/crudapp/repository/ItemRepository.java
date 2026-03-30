package com.example.crudapp.repository;


import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);
    boolean existsById(Long id);



}
