package com.example.crudapp.controller;


import com.example.crudapp.dto.PersonDTO;
import com.example.crudapp.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    //Endpoint for retrieving all persons, it returns a list of PersonDTO objects as a response.
    @GetMapping(path="persons")
    public ResponseEntity<List<PersonDTO>> getAllPersons(){
        return ResponseEntity.ok(personService.getAllPersons());
    }

    //Endpoint for retrieving all persons using Hibernate directly (no Spring Data JPA)
    @GetMapping(path="persons/hibernate")
    public ResponseEntity<List<PersonDTO>> getAllPersonsHibernate(){
        return ResponseEntity.ok(personService.getAllPersonsHibernate());
    }

    //Endpoint for retrieving a person by ID using Hibernate directly
    @GetMapping(path = "person/hibernate/{id}")
    public PersonDTO getPersonByIdHibernate(@PathVariable Long id) {
        return personService.getPersonByIdHibernate(id);
    }

    //Endpoint for creating a new person using Hibernate directly
    @PostMapping(path = "add-person/hibernate")
    public ResponseEntity<PersonDTO> addPersonaHibernate( @RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.addPersonaHibernate(personDTO));
    }

    //Endpoint for updating an existing person using Hibernate directly
    @PutMapping(path = "persons/hibernate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePersonHibernate(@PathVariable Long id, @Valid @RequestBody PersonDTO updatedPerson) {
        return personService.updatePersonHibernate(id, updatedPerson);
    }

    //Endpoint for deleting a person using Hibernate directly
    @DeleteMapping(path = "person/hibernate/{id}")
    public void deletePersonHibernate(@PathVariable Long id) {
        personService.deletePersonHibernate(id);
    }

    //Endpoint for creating a new person, it receives a PersonDTO object in the request body and returns the created person as a response.
    @PostMapping(path="add-person")
    public ResponseEntity<PersonDTO> addPersona( @RequestBody PersonDTO personDTO){
        return ResponseEntity.ok(personService.addPersona(personDTO));
    }

    //Endpoint for retrieving a person by their ID, it receives the ID as a path variable and returns the corresponding person as a response.
    @GetMapping(path="person/{id}")
    public PersonDTO getPersonaById(@PathVariable Long id){
        return personService.getPersonById(id);
    }

    //Endpoint for updating an existing person, it receives the ID of the person to be updated as a path variable and the updated person data as a PersonDTO object in the request body. It returns the updated person as a response.
    @PutMapping( path = "persons/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePerson(@PathVariable Long id, @Valid @RequestBody PersonDTO updatedPerson) {
        return personService.updatePerson(id, updatedPerson);
    }

    //Endpoint for deleting a person, it receives the ID of the person to be deleted as a path variable and returns no content as a response.
    @DeleteMapping(path = "person/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }




}
