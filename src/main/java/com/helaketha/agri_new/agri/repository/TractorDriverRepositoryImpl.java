package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.TractorDriver;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class TractorDriverRepositoryImpl implements TractorDriverRepository {

    private final JdbcTemplate jdbcTemplate;

    public TractorDriverRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<TractorDriver> MAPPER = (rs, rowNum) -> {
        TractorDriver d = new TractorDriver();
        d.setTractorDriverId(rs.getInt("tractor_driver_id"));
        d.setName(rs.getString("name"));
        d.setPhone(rs.getString("phone"));
        return d;
    };

    @Override
    public int insert(TractorDriver d) {
        String sql = "INSERT INTO tractor_drivers (name, phone) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getName());
            ps.setString(2, d.getPhone());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<TractorDriver> findAll() {
        return jdbcTemplate.query(
                "SELECT tractor_driver_id, name, phone FROM tractor_drivers",
                MAPPER
        );
    }

    @Override
    public Optional<TractorDriver> findById(int id) {
        return jdbcTemplate.query(
                "SELECT tractor_driver_id, name, phone FROM tractor_drivers WHERE tractor_driver_id = ?",
                MAPPER,
                id
        ).stream().findFirst();
    }

    @Override
    public int update(TractorDriver d) {
        return jdbcTemplate.update(
                "UPDATE tractor_drivers SET name = ?, phone = ? WHERE tractor_driver_id = ?",
                d.getName(), d.getPhone(), d.getTractorDriverId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(
                "DELETE FROM tractor_drivers WHERE tractor_driver_id = ?",
                id
        );
    }
}
