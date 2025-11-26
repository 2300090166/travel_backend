package com.travelease.backend.controller;

import com.travelease.backend.model.Vehicle;
import com.travelease.backend.repository.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Public endpoint for all users
    @GetMapping("/api/vehicles")
    public List<Vehicle> listPublic() {
        return vehicleRepository.findAll();
    }

    // Admin endpoints
    @GetMapping("/api/admin/vehicles")
    public List<Vehicle> list() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/api/admin/vehicles/{id}")
    public ResponseEntity<Vehicle> get(@PathVariable Long id) {
        Optional<Vehicle> v = vehicleRepository.findById(id);
        return v.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/admin/vehicles")
    public ResponseEntity<Vehicle> create(@RequestBody Vehicle v) {
        Vehicle saved = vehicleRepository.save(v);
        return ResponseEntity.created(URI.create("/api/admin/vehicles/" + saved.getId())).body(saved);
    }

    @PutMapping("/api/admin/vehicles/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody Vehicle v) {
        return vehicleRepository.findById(id).map(existing -> {
            if (v.getName() != null) existing.setName(v.getName());
            if (v.getType() != null) existing.setType(v.getType());
            if (v.getImage() != null) existing.setImage(v.getImage());
            if (v.getPrice() != null) existing.setPrice(v.getPrice());
            if (v.getCapacity() != null) existing.setCapacity(v.getCapacity());
            if (v.getFeatures() != null) existing.setFeatures(v.getFeatures());
            if (v.getDescription() != null) existing.setDescription(v.getDescription());
            if (v.getAvailable() != null) existing.setAvailable(v.getAvailable());
            vehicleRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/admin/vehicles/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) return ResponseEntity.notFound().build();
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
