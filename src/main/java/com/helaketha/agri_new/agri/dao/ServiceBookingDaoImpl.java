package com.helaketha.agri_new.agri.dao;

import com.helaketha.agri_new.agri.entity.ServiceBooking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceBookingDaoImpl implements ServiceBookingDao {

    private final JdbcTemplate jdbcTemplate;

    public ServiceBookingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ServiceBooking> MAPPER = (rs, rowNum) -> {
        ServiceBooking b = new ServiceBooking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setFarmerId(rs.getInt("farmer_id"));
        b.setServiceId(rs.getInt("service_id"));
        b.setServiceType(rs.getString("service_type"));
        Date d = rs.getDate("booking_date");
        if (d != null) { b.setBookingDate(d.toLocalDate()); }
        b.setStatus(rs.getString("status"));
        return b;
    };

    @Override
    public int insert(ServiceBooking booking) {
        String sql = "INSERT INTO service_booking (farmer_id, service_id, service_type, booking_date, status) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, booking.getFarmerId());
            ps.setObject(2, booking.getServiceId());
            ps.setString(3, booking.getServiceType());
            ps.setObject(4, booking.getBookingDate() != null ? Date.valueOf(booking.getBookingDate()) : null);
            ps.setString(5, booking.getStatus());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<ServiceBooking> findAll() {
        return jdbcTemplate.query("SELECT * FROM service_booking", MAPPER);
    }

    @Override
    public Optional<ServiceBooking> findById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_id=?", MAPPER, id).stream().findFirst();
    }

    @Override
    public List<ServiceBooking> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE status=?", MAPPER, status);
    }

    @Override
    public List<ServiceBooking> findByFarmerId(Integer farmerId) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE farmer_id=?", MAPPER, farmerId);
    }

    @Override
    public List<ServiceBooking> findByDate(LocalDate date) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_date=?", MAPPER, Date.valueOf(date));
    }

    @Override
    public List<ServiceBooking> findByDateRange(LocalDate start, LocalDate end) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_date BETWEEN ? AND ?", MAPPER, Date.valueOf(start), Date.valueOf(end));
    }

    @Override
    public int update(ServiceBooking booking) {
        String sql = "UPDATE service_booking SET farmer_id=?, service_id=?, service_type=?, booking_date=?, status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql,
                booking.getFarmerId(),
                booking.getServiceId(),
                booking.getServiceType(),
                booking.getBookingDate() != null ? Date.valueOf(booking.getBookingDate()) : null,
                booking.getStatus(),
                booking.getBookingId());
    }

    @Override
    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE service_booking SET status=? WHERE booking_id=?", status, id);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM service_booking WHERE booking_id=?", id);
    }
}
