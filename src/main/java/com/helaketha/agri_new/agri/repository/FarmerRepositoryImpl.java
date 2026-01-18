package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.Farmer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class FarmerRepositoryImpl implements FarmerRepository {

    private final JdbcTemplate jdbcTemplate;

    public FarmerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Farmer> MAPPER = (rs, rowNum) -> {
        Farmer f = new Farmer();
        f.setFarmerId(rs.getInt("farmer_id"));
        f.setFullName(rs.getString("name"));
        f.setPhone(rs.getString("phone"));
        f.setEmail(rs.getString("email"));
        f.setAddress(rs.getString("address"));
        f.setNic(rs.getString("nic"));
        f.setUsername(rs.getString("username"));

        return f;
    };

    @Override
    public int insert(Farmer farmer) {
        String sql = "insert into farmers (name, phone, email, address, nic, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, farmer.getFullName());
            ps.setString(2, farmer.getPhone());
            ps.setString(3, farmer.getEmail());
            ps.setString(4, farmer.getAddress());
            ps.setString(5, farmer.getNic());
            ps.setString(6, farmer.getUsername());

            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public int update(Farmer farmer) {
        String sql = "UPDATE farmers SET name=?, phone=?, email=?, address=?, nic=?, username=?, password=? WHERE farmer_id=?";
        return jdbcTemplate.update(sql,
                farmer.getFullName(),
                farmer.getPhone(),
                farmer.getEmail(),
                farmer.getAddress(),
                farmer.getNic(),
                farmer.getUsername(),

                farmer.getFarmerId());
    }

    @Override
    public List<Farmer> findAll() {
        String sql = "SELECT * FROM farmers";
        return jdbcTemplate.query(sql, MAPPER);
    }

    @Override
    public Optional<Farmer> findById(int id) {
        String sql = "SELECT * FROM farmers WHERE farmer_id = ?";
        return jdbcTemplate.query(sql, MAPPER, id).stream().findFirst();
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM farmers WHERE farmer_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}