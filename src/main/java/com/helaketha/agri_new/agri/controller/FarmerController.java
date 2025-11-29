package com.helaketha.agri_new.agri.controller;

import com.helaketha.agri_new.agri.dao.FarmerDao;
import com.helaketha.agri_new.agri.entity.Farmer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FarmerDaoImpl implements FarmerDao {

    private final JdbcTemplate jdbcTemplate;

    public FarmerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Farmer> FARMER_ROW_MAPPER = (rs, rowNum) ->
            new Farmer(
                    rs.getInt("farmer_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("nic")
            );

    @Override
    public int insert(Farmer farmer) {
        String sql = "INSERT INTO farmers (name, phone, address, nic) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                farmer.getName(),
                farmer.getPhone(),
                farmer.getAddress(),
                farmer.getNic());
    }

    @Override
    public List<Farmer> findAll() {
        String sql = "SELECT * FROM farmers";
        return jdbcTemplate.query(sql, FARMER_ROW_MAPPER);
    }

    @Override
    public Optional<Farmer> findById(int id) {
        String sql = "SELECT * FROM farmers WHERE farmer_id = ?";
        return jdbcTemplate.query(sql, FARMER_ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public int update(Farmer farmer) {
        String sql = "UPDATE farmers SET name = ?, phone = ?, address = ?, nic = ? WHERE farmer_id = ?";
        return jdbcTemplate.update(sql,
                farmer.getName(),
                farmer.getPhone(),
                farmer.getAddress(),
                farmer.getNic(),
                farmer.getFarmerId());
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM farmers WHERE farmer_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
