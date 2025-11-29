package com.helaketha.agri_new.agri.dao;

import com.helaketha.agri_new.agri.entity.Farmer;

import java.util.List;
import java.util.Optional;

public interface FarmerDao {
    int insert(Farmer farmer);               // returns generated id
    List<Farmer> findAll();
    Optional<Farmer> findById(int id);
    int update(Farmer farmer);               // returns rows affected
    int delete(int id);                      // returns rows affected
}
