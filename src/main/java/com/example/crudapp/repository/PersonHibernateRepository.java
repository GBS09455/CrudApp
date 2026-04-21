package com.example.crudapp.repository;

import com.example.crudapp.entity.Person;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
public class PersonHibernateRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;


    public List<Person> findAll() {
        log.info("findAll() called - INFO level");
        log.debug("findAll() called - DEBUG level");
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            log.debug("EntityManager created, executing query...");
            return em.createQuery("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.items", Person.class).getResultList();
        } catch (PersistenceException e) {
            throw new RuntimeException("Error retrieving persons from the database", e);
        } finally {
            closeQuietly(em);
        }
    }


    public Optional<Person> findById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (id <= 0)    throw new IllegalArgumentException("Id must be a positive number");

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            List<Person> result = em
                    .createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.items WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (PersistenceException e) {
            throw new RuntimeException("Error finding person with id: " + id, e);
        } finally {
            closeQuietly(em);
        }
    }


    public Person save(Person person) {
        if (person == null) throw new IllegalArgumentException("Person must not be null");

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = entityManagerFactory.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Person saved;
            if (person.getId() == null) {
                em.persist(person);
                saved = person;
            } else {
                saved = em.merge(person);
            }

            tx.commit();
            return saved;
        } catch (PersistenceException e) {
            rollbackQuietly(tx);
            throw new RuntimeException("Error saving person: " + person.getName(), e);
        } finally {
            closeQuietly(em);
        }
    }


    public void deleteById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (id <= 0)    throw new IllegalArgumentException("Id must be a positive number");

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = entityManagerFactory.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Person person = em
                    .createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.items WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult();
            em.remove(person);
            tx.commit();
        } catch (NoResultException e) {
            rollbackQuietly(tx);
            throw new RuntimeException("Person with id " + id + " was not found", e);
        } catch (PersistenceException e) {
            rollbackQuietly(tx);
            throw new RuntimeException("Error deleting person with id: " + id, e);
        } finally {
            closeQuietly(em);
        }
    }


    public boolean existsById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (id <= 0)    throw new IllegalArgumentException("Id must be a positive number");

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            Long count = em
                    .createQuery("SELECT COUNT(p) FROM Person p WHERE p.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } catch (PersistenceException e) {
            throw new RuntimeException("Error checking existence of person with id: " + id, e);
        } finally {
            closeQuietly(em);
        }
    }


    public boolean existsByName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name must not be blank");

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            Long count = em
                    .createQuery("SELECT COUNT(p) FROM Person p WHERE p.name = :name", Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return count > 0;
        } catch (PersistenceException e) {
            throw new RuntimeException("Error checking existence of person with name: " + name, e);
        } finally {
            closeQuietly(em);
        }
    }


    private void closeQuietly(EntityManager em) {
        if (em != null && em.isOpen()) {
            try { em.close(); } catch (Exception ignored) {}
        }
    }

    private void rollbackQuietly(EntityTransaction tx) {
        if (tx != null && tx.isActive()) {
            try { tx.rollback(); } catch (Exception ignored) {}
        }
    }
}
