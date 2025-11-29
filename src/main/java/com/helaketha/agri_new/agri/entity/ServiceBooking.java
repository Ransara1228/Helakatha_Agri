package com.helaketha.agri_new.agri.entity;

import java.time.LocalDate;

public class ServiceBooking {


    private Integer bookingId;
    private Integer farmerId;
    private Integer serviceId;      // references a service catalog (optional)
    private String serviceType;     // e.g. "TRACTOR", "HARVEST"
    private LocalDate bookingDate;
    private String status;          // PENDING, CONFIRMED, COMPLETED, CANCELLED

    public ServiceBooking() { }

    public ServiceBooking(Integer bookingId, Integer farmerId, Integer serviceId, String serviceType,
                          LocalDate bookingDate, String status) {
        this.bookingId = bookingId;
        this.farmerId = farmerId;
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getFarmerId() { return farmerId; }
    public void setFarmerId(Integer farmerId) { this.farmerId = farmerId; }

    public Integer getServiceId() { return serviceId; }
    public void setServiceId(Integer serviceId) { this.serviceId = serviceId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
