package com.helaketha.agri_new.agri.entity;

import java.time.LocalDate;

public class Service {

    private Integer id;
    private Integer farmerId;
    private String serviceType;
    private LocalDate date;
    private String status;

    public Service() {
    }

    public Service(Integer id, Integer farmerId, String serviceType, LocalDate date, String status) {
        this.id = id;
        this.farmerId = farmerId;
        this.serviceType = serviceType;
        this.date = date;
        this.status = status;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBookingId(Integer id) {
    }
}
