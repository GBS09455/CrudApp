package com.example.crudapp.controller;

import com.example.crudapp.dto.ItemDTO;
import com.example.crudapp.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Items", description = "CRUD operations for items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Get all items", description = "Returns all items (cached in Redis under key 'items::all')")
    @ApiResponse(responseCode = "200", description = "List of items retrieved successfully")
    @GetMapping(path = "items")
    public List<ItemDTO> getAllItems() {
        return itemService.findAll();
    }

    @Operation(summary = "Get items paginated", description = "Returns a paginated view of items")
    @ApiResponse(responseCode = "200", description = "Paginated list of items")
    @GetMapping(path = "items/page")
    public Page<ItemDTO> getItemsPaged(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return itemService.findAllPaged(page, size);
    }

    @Operation(summary = "Get item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item found"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping(path = "items/{id}")
    public ItemDTO getItemById(@Parameter(description = "Item ID") @PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @Operation(summary = "Create a new item", description = "Creates an item and evicts the Redis caches")
    @ApiResponse(responseCode = "201", description = "Item created successfully")
    @PostMapping(path = "items")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDTO> create(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.create(itemDTO));
    }

    @Operation(summary = "Update an existing item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping(path = "items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem(@Parameter(description = "Item ID") @PathVariable Long id, @RequestBody ItemDTO updatedItem) {
        return itemService.updateItem(id, updatedItem);
    }

    @Operation(summary = "Delete an item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping(path = "items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@Parameter(description = "Item ID") @PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
