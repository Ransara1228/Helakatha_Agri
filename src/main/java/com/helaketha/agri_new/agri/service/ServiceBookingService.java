package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.entity.ServiceBooking;
import com.helaketha.agri_new.agri.dao.ServiceBookingDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ServiceBooking
public class ServiceBookingService {

    private final ServiceBookingDao dao;

    public ServiceBookingService(ServiceBookingDao dao) {
        this.dao = dao;
    }

    public ServiceBooking save(ServiceBooking sb) {
        return dao.save(sb);
    }

    public List<ServiceBooking> findAll() {
        return dao.findAll();
    }

    public Optional<ServiceBooking> findById(Integer id) {
        return dao.findById(id);
    }

    public List<ServiceBooking> findByStatus(String status) {
        return dao.findByStatus(status);
    }

    public List<ServiceBooking> findByFarmer(Integer farmerId) {
        return dao.findByFarmer(farmerId);
    }

    public List<ServiceBooking> findByDate(LocalDate date) {
        return dao.findByDate(date);
    }

    public List<ServiceBooking> findByDateRange(LocalDate start, LocalDate end) {
        return dao.findByDateRange(start, end);
    }

    public ServiceBooking update(Integer id, ServiceBooking sb) {
        sb.setBookingId(id);
        return dao.update(sb);
    }

    public ServiceBooking updateStatus(Integer id, String status) {
        return dao.updateStatus(id, status);
    }

    public boolean delete(Integer id) {
        return dao.delete(id);
    }
}
