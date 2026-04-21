package com.example.crudapp.service;

import com.example.crudapp.dto.PersonDTO;

import com.example.crudapp.entity.Item;
import com.example.crudapp.entity.Person;

import com.example.crudapp.exception.PersonAlreadyExceptionHandler;
import com.example.crudapp.exception.PersonNotFoundException;
//import com.example.crudapp.mapper.PersonMapper;
import com.example.crudapp.mapperStruct.PersonMapper;
import com.example.crudapp.repository.PersonHibernateRepository;
import com.example.crudapp.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonHibernateRepository personHibernateRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonHibernateRepository personHibernateRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personHibernateRepository = personHibernateRepository;
        this.personMapper = personMapper;
    }

    @Transactional
    public List<PersonDTO> getAllPersons() {
        try {
            return personRepository.findAll().stream()
                    .map(personMapper::toDTO)
                    .toList();
        } catch (Exception ex) {
            log.error("Error fetching all persons: {}", ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public List<PersonDTO> getAllPersonsHibernate() {
        try {
            log.debug("Fetching all persons using Hibernate repository");
            return personHibernateRepository.findAll().stream()
                    .map(personMapper::toDTO)
                    .toList();
        } catch (Exception ex) {
            log.error("Error fetching all persons (Hibernate): {}", ex.getMessage(), ex);
            return List.of();
        }
    }

    @Transactional
    public PersonDTO getPersonById(Long id) {
        try {
            Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("person.not.found", id));
            return personMapper.toDTO(person);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found with id {}: {}", id, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error fetching person with id {}: {}", id, ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public PersonDTO getPersonByIdHibernate(Long id) {
        try {
            Person person = personHibernateRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("person.not.found", id));
            return personMapper.toDTO(person);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found with id {}: {}", id, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error fetching person with id {} (Hibernate): {}", id, ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public PersonDTO addPersona(PersonDTO personDTO) {
        try {
            if (personRepository.existsByName(personDTO.getName())) {
                throw new PersonAlreadyExceptionHandler("person.already.exists", personDTO.getName());
            }
            Person entity = personMapper.toEntity(personDTO);
            if (entity.getItems() != null) {
                for (Item item : entity.getItems()) {
                    item.setPerson(entity);
                }
            }
            Person savedEntity = personRepository.save(entity);
            return personMapper.toDTO(savedEntity);
        } catch (PersonAlreadyExceptionHandler ex) {
            log.error("Duplicate person: {}", ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error adding person {}: {}", personDTO.getName(), ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public PersonDTO addPersonaHibernate(PersonDTO personDTO) {
        try {
            if (personHibernateRepository.existsByName(personDTO.getName())) {
                throw new PersonAlreadyExceptionHandler("person.already.exists", personDTO.getName());
            }
            Person entity = personMapper.toEntity(personDTO);
            if (entity.getItems() != null) {
                for (Item item : entity.getItems()) {
                    item.setPerson(entity);
                }
            }
            Person savedEntity = personHibernateRepository.save(entity);
            return personMapper.toDTO(savedEntity);
        } catch (PersonAlreadyExceptionHandler ex) {
            log.error("Duplicate person (Hibernate): {}", ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error adding person {} (Hibernate): {}", personDTO.getName(), ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public PersonDTO updatePerson(Long id, PersonDTO updatedItem) {
        try {
            Person existingPerson = personRepository.findById(id).orElseThrow(() ->
                    new PersonNotFoundException("person.not.found", id));
            existingPerson.setName(updatedItem.getName());
            Person savedItem = personRepository.save(existingPerson);
            return personMapper.toDTO(savedItem);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found for update with id {}: {}", id, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error updating person with id {}: {}", id, ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public PersonDTO updatePersonHibernate(Long id, PersonDTO updatedItem) {
        try {
            Person existingPerson = personHibernateRepository.findById(id).orElseThrow(() ->
                    new PersonNotFoundException("person.not.found", id));
            existingPerson.setName(updatedItem.getName());
            Person savedItem = personHibernateRepository.save(existingPerson);
            return personMapper.toDTO(savedItem);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found for update with id {} (Hibernate): {}", id, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.error("Error updating person with id {} (Hibernate): {}", id, ex.getMessage(), ex);
            return null;
        }
    }

    @Transactional
    public void deletePerson(Long id) {
        try {
            if (!personRepository.existsById(id)) {
                throw new PersonNotFoundException("person.not.found", id);
            }
            personRepository.deleteById(id);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found for delete with id {}: {}", id, ex.getMessage());
        } catch (Exception ex) {
            log.error("Error deleting person with id {}: {}", id, ex.getMessage(), ex);
        }
    }

    @Transactional
    public void deletePersonHibernate(Long id) {
        try {
            if (!personHibernateRepository.existsById(id)) {
                throw new PersonNotFoundException("person.not.found", id);
            }
            personHibernateRepository.deleteById(id);
        } catch (PersonNotFoundException ex) {
            log.error("Person not found for delete with id {} (Hibernate): {}", id, ex.getMessage());
        } catch (Exception ex) {
            log.error("Error deleting person with id {} (Hibernate): {}", id, ex.getMessage(), ex);
        }
    }

}
