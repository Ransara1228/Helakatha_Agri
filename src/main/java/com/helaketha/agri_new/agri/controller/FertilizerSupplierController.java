package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;
import com.helaketha.agri_new.agri.service.FertilizerSupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-suppliers")
@CrossOrigin
@Validated
public class FertilizerSupplierController {

    private final FertilizerSupplierService service;

    public FertilizerSupplierController(FertilizerSupplierService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FertilizerSupplier> create(@Valid @RequestBody FertilizerSupplier supplier) {
        FertilizerSupplier created = service.create(supplier);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<FertilizerSupplier> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FertilizerSupplier> getById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FertilizerSupplier> update(@PathVariable int id, @Valid @RequestBody FertilizerSupplier supplier) {
        FertilizerSupplier updated = service.update(id, supplier);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}