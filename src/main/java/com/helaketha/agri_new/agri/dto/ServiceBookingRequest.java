package com.helaketha.agri_new.agri.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.helaketha.agri_new.agri.entity.ServiceBooking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class ServiceBookingRequest {

    @NotNull
    @Positive
    private Integer farmerId;

    @Positive
    private Integer serviceId;

    @NotBlank
    private String serviceType;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @NotBlank
    @Pattern(regexp = "PENDING|CONFIRMED|COMPLETED|CANCELLED")
    private String status;

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceBooking toEntity() {
        return new ServiceBooking(
                null,
                farmerId,
                serviceId,
                serviceType,
                bookingDate,
                status
        );
    }

    public ServiceBooking asEntityWithId(Integer id) {
        ServiceBooking booking = toEntity();
        booking.setBookingId(id);
        return booking;
    }
}

