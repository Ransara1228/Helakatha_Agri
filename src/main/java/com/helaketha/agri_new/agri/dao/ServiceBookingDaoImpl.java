package com.helaketha.agri_new.agri.dao;

import com.helaketha.agri_new.agri.entity.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceDaoImpl implements ServiceDao {

    private final JdbcTemplate jdbcTemplate;

    public ServiceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Service> SERVICE_ROW_MAPPER = (rs, rowNum) -> {
        Service s = new Service();
        s.setId(rs.getInt("id"));
        s.setFarmerId(rs.getInt("farmer_id"));
        s.setServiceType(rs.getString("service_type"));
        Date sqlDate = rs.getDate("date");
        if (sqlDate != null) {
            s.setDate(sqlDate.toLocalDate());
        }
        s.setStatus(rs.getString("status"));
        return s;
    };

    @Override
    public int insert(Service service) {
        String sql = "INSERT INTO service (farmer_id, service_type, date, status) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                service.getFarmerId(),
                service.getServiceType(),
                service.getDate(),
                service.getStatus());
    }

    @Override
    public List<Service> findAll() {
        String sql = "SELECT * FROM service";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER);
    }

    @Override
    public Optional<Service> findById(Integer id) {
        String sql = "SELECT * FROM service WHERE id = ?";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public List<Service> findByStatus(String status) {
        String sql = "SELECT * FROM service WHERE status = ?";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER, status);
    }

    @Override
    public List<Service> findByFarmerId(Integer farmerId) {
        String sql = "SELECT * FROM service WHERE farmer_id = ?";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER, farmerId);
    }

    @Override
    public List<Service> findByDate(LocalDate date) {
        String sql = "SELECT * FROM service WHERE date = ?";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER, Date.valueOf(date));
    }

    @Override
    public List<Service> findByDateRange(LocalDate start, LocalDate end) {
        String sql = "SELECT * FROM service WHERE date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, SERVICE_ROW_MAPPER, Date.valueOf(start), Date.valueOf(end));
    }

    @Override
    public int update(Service service) {
        String sql = "UPDATE service SET farmer_id=?, service_type=?, date=?, status=? WHERE id=?";
        return jdbcTemplate.update(sql,
                service.getFarmerId(),
                service.getServiceType(),
                service.getDate(),
                service.getStatus(),
                service.getId());
    }

    @Override
    public int updateStatus(Integer id, String status) {
        String sql = "UPDATE service SET status=? WHERE id=?";
        return jdbcTemplate.update(sql, status, id);
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM service WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }
}
