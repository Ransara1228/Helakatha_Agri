package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.FarmerRepository;
import com.helaketha.agri_new.agri.entity.Farmer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    private final FarmerRepository dao;

    public FarmerService(FarmerRepository dao) {
        this.dao = dao;
    }

    public Farmer create(Farmer f) {
        int id = dao.insert(f);
        f.setFarmerId(id);
        return f;
    }

    public List<Farmer> findAll() {
        return dao.findAll();
    }

    public Optional<Farmer> findById(int id) {
        return dao.findById(id);
    }

    public Farmer update(int id, Farmer f) {
        f.setFarmerId(id);
        dao.update(f);
        return f;
    }

    public boolean delete(int id) {
        return dao.delete(id) > 0;
    }
}
