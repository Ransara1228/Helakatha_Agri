package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.entity.ServiceBooking;
import com.helaketha.agri_new.agri.service.ServiceBookingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin
public class ServiceBookingController {

    private final ServiceBookingService service;

    public ServiceBookingController(ServiceBookingService service) {
        this.service = service;
    }

    @PostMapping
    public ServiceBooking create(@RequestBody ServiceBooking sb) {
        return service.save(sb);
    }

    @GetMapping
    public List<ServiceBooking> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ServiceBooking> getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<ServiceBooking> getByStatus(@PathVariable String status) {
        return service.findByStatus(status);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<ServiceBooking> getByFarmer(@PathVariable Integer farmerId) {
        return service.findByFarmer(farmerId);
    }

    @GetMapping("/date")
    public List<ServiceBooking> getByDate(@RequestParam String date) {
        return service.findByDate(LocalDate.parse(date));
    }

    @GetMapping("/range")
    public List<ServiceBooking> getRange(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return service.findByDateRange(
                LocalDate.parse(start),
                LocalDate.parse(end)
        );
    }

    @PutMapping("/{id}")
    public ServiceBooking update(
            @PathVariable Integer id,
            @RequestBody ServiceBooking sb
    ) {
        return service.update(id, sb);
    }

    @PatchMapping("/{id}/status")
    public ServiceBooking patchStatus(
            @PathVariable Integer id,
            @RequestParam String status
    ) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return service.delete(id);
    }
}
