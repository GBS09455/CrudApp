package com.example.crudapp.repository;

import com.example.crudapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Override
    List<Person> findAll();

    @Override
    Optional<Person> findById(Long id);

    boolean existsByName(String name);
}


