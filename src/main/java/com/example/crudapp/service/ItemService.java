package com.example.crudapp.service;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.entity.Item;
import com.example.crudapp.exception.ItemAlreadyExistsException;
import com.example.crudapp.mapper.ItemMapper;
import com.example.crudapp.repository.ItemRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * @Cacheable: la primul apel executa metoda si salveaza rezultatul in Redis
     * sub cheia "items::all". La urmatoarele apeluri returneaza direct din cache,
     * fara sa mai acceseze baza de date.
     */
    @Cacheable(value = "items", key = "'all'")
    public List<ItemDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * @Cacheable cu cheie compusa din pagina si dimensiunea paginii.
     * Ex: "items-paged::0-10", "items-paged::1-10"
     * Fiecare combinatie page+size are propria intrare in cache.
     */
    @Cacheable(value = "items-paged", key = "#page + '-' + #size")
    public Page<ItemDTO> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findAll(pageable);
        List<ItemDTO> dtos = itemPage.getContent()
                .stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, itemPage.getTotalElements());
    }

    /**
     * @Cacheable cu cheia egala cu id-ul itemului.
     * Ex: "items::5" -> ItemDTO cu id=5
     * La al doilea apel cu acelasi id, datele vin din Redis.
     */
    @Cacheable(value = "items", key = "#id")
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        return ItemMapper.toDTO(item);
    }

    /**
     * @Caching: grupare de mai multe adnotari pe aceeasi metoda.
     * - @CacheEvict pe "items" key='all'      -> sterge lista completa din cache
     * - @CacheEvict pe "items-paged" allEntries=true -> sterge toate paginile din cache
     * Dupa create, datele vechi din cache sunt invalide, deci le stergem.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "items", key = "'all'"),
            @CacheEvict(value = "items-paged", allEntries = true)
    })
    public ItemDTO create(ItemDTO dto) {
        if (itemRepository.existsByName(dto.getName())) {
            throw new ItemAlreadyExistsException("item.already.exists", dto.getName());
        }
        Item entity = toEntity(dto);
        Item savedEntity = itemRepository.save(entity);
        return ItemMapper.toDTO(savedEntity);
    }

    /**
     * @CachePut: executa intotdeauna metoda (update real in DB) si
     * actualizeaza intrarea din cache pentru acest id cu noul rezultat.
     * - @CacheEvict pe "items" key='all' -> lista completa e invalida dupa update
     * - @CacheEvict pe "items-paged"     -> paginile sunt invalide dupa update
     */
    @Transactional
    @Caching(
            put  = { @CachePut(value = "items", key = "#id") },
            evict = {
                    @CacheEvict(value = "items", key = "'all'"),
                    @CacheEvict(value = "items-paged", allEntries = true)
            }
    )
    public ItemDTO updateItem(Long id, ItemDTO updatedItem) {
        Item existingItem = itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("item.not.found"));
        existingItem.setName(updatedItem.getName());
        existingItem.setDescription(updatedItem.getDescription());
        Item savedItem = itemRepository.save(existingItem);
        return ItemMapper.toDTO(savedItem);
    }

    /**
     * @Caching cu mai multe @CacheEvict:
     * - sterge intrarea specifica din cache (items::<id>)
     * - sterge lista completa (items::all)
     * - sterge toate paginile (items-paged::*)
     * Astfel nu raman date sterse in cache.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "items", key = "#id"),
            @CacheEvict(value = "items", key = "'all'"),
            @CacheEvict(value = "items-paged", allEntries = true)
    })
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item with id " + id + " not found");
        }
        itemRepository.deleteById(id);
    }
}
