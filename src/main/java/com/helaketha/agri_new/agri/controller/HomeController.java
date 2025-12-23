package com.helaketha.agri_new.agri.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@RequestMapping("/")
public class HomeController {

    private final JdbcTemplate jdbcTemplate;

    public HomeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> home(HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 200);
        body.put("message", "Service is up");
        body.put("path", request.getRequestURI());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health(HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 200);
        body.put("message", "OK");
        body.put("path", request.getRequestURI());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/db-health")
    public ResponseEntity<Map<String, Object>> dbHealth(HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("path", request.getRequestURI());
        try {
            Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            body.put("status", 200);
            body.put("message", "DB reachable");
            body.put("result", one);
            return ResponseEntity.ok(body);
        } catch (Exception ex) {
            body.put("status", 500);
            body.put("message", "DB connection failed: " + ex.getMessage());
            body.put("errorClass", ex.getClass().getName());
            Throwable cause = ex.getCause();
            if (cause != null) {
                body.put("rootCause", cause.getClass().getName() + ": " + cause.getMessage());
                Throwable root = cause.getCause();
                if (root != null) {
                    body.put("deepCause", root.getClass().getName() + ": " + root.getMessage());
                }
            }
            return ResponseEntity.status(500).body(body);
        }
    }
}