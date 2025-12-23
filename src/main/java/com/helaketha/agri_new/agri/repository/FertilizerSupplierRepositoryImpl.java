package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class FertilizerSupplierRepositoryImpl implements FertilizerSupplierRepository {

    private final JdbcTemplate jdbcTemplate;

    public FertilizerSupplierRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<FertilizerSupplier> MAPPER = (rs, rowNum) -> {
        FertilizerSupplier s = new FertilizerSupplier();
        s.setSupplierId(rs.getInt("supplier_id"));
        s.setName(rs.getString("name"));
        s.setPhone(rs.getString("phone"));
        s.setFertilizerType(rs.getString("fertilizer_type"));
        return s;
    };

    @Override
    public int insert(FertilizerSupplier s) {
        String sql = "INSERT INTO fertilizer_suppliers (name, phone, fertilizer_type) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getFertilizerType());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<FertilizerSupplier> findAll() {
        return jdbcTemplate.query(
                "SELECT supplier_id, name, phone, fertilizer_type FROM fertilizer_suppliers",
                MAPPER
        );
    }

    @Override
    public Optional<FertilizerSupplier> findById(int id) {
        return jdbcTemplate.query(
                "SELECT supplier_id, name, phone, fertilizer_type FROM fertilizer_suppliers WHERE supplier_id = ?",
                MAPPER,
                id
        ).stream().findFirst();
    }

    @Override
    public int update(FertilizerSupplier s) {
        return jdbcTemplate.update(
                "UPDATE fertilizer_suppliers SET name = ?, phone = ?, fertilizer_type = ? WHERE supplier_id = ?",
                s.getName(), s.getPhone(), s.getFertilizerType(), s.getSupplierId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(
                "DELETE FROM fertilizer_suppliers WHERE supplier_id = ?",
                id
        );
    }
}
