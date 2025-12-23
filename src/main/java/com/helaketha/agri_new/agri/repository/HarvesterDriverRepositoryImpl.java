package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.HarvesterDriver;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class HarvesterDriverRepositoryImpl implements HarvesterDriverRepository {

    private final JdbcTemplate jdbcTemplate;

    public HarvesterDriverRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<HarvesterDriver> MAPPER = (rs, rowNum) -> {
        HarvesterDriver d = new HarvesterDriver();
        d.setHarvesterDriverId(rs.getInt("harvester_driver_id"));
        d.setName(rs.getString("name"));
        d.setPhone(rs.getString("phone"));
        return d;
    };

    @Override
    public int insert(HarvesterDriver d) {
        String sql = "INSERT INTO harvester_drivers (name, phone) VALUES (?, ?)";
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
    public List<HarvesterDriver> findAll() {
        return jdbcTemplate.query(
                "SELECT harvester_driver_id, name, phone FROM harvester_drivers",
                MAPPER
        );
    }

    @Override
    public Optional<HarvesterDriver> findById(int id) {
        return jdbcTemplate.query(
                "SELECT harvester_driver_id, name, phone FROM harvester_drivers WHERE harvester_driver_id = ?",
                MAPPER,
                id
        ).stream().findFirst();
    }

    @Override
    public int update(HarvesterDriver d) {
        return jdbcTemplate.update(
                "UPDATE harvester_drivers SET name = ?, phone = ? WHERE harvester_driver_id = ?",
                d.getName(), d.getPhone(), d.getHarvesterDriverId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(
                "DELETE FROM harvester_drivers WHERE harvester_driver_id = ?",
                id
        );
    }
}
