package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.Farmer;
import com.helaketha.agri_new.agri.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@CrossOrigin
public class FarmerController {

    private final FarmerService service;

    public FarmerController(FarmerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Farmer> addFarmer(@RequestBody Farmer farmer) {
        Farmer created = service.create(farmer);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<Farmer> getFarmers() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmerById(@PathVariable int id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Farmer> updateFarmer(@PathVariable int id, @RequestBody Farmer farmer) {
        Farmer updated = service.update(id, farmer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Farmer> patchFarmer(@PathVariable int id, @RequestBody Farmer partial) {
        return service.findById(id).map(existing -> {
            if (partial.getFullName() != null) existing.setFullName(partial.getFullName());
            if (partial.getPhone() != null) existing.setPhone(partial.getPhone());
            if (partial.getEmail() != null) existing.setEmail(partial.getEmail());
            if (partial.getAddress() != null) existing.setAddress(partial.getAddress());
            if (partial.getNic() != null) existing.setNic(partial.getNic());
            service.update(id, existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }
}
