package com.helaketha.agri_new.agri.dao;

import com.helaketha.agri_new.agri.entity.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceDao {

    int insert(Service service);

    List<Service> findAll();

    Optional<Service> findById(Integer id);

    List<Service> findByStatus(String status);

    List<Service> findByFarmerId(Integer farmerId);

    List<Service> findByDate(LocalDate date);

    List<Service> findByDateRange(LocalDate start, LocalDate end);

    int update(Service service);

    int updateStatus(Integer id, String status);

    int delete(Integer id);
}
