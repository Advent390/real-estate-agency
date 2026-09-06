package com.metrazh.agency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Дані форми "новий/редагувати об'єкт" з admin/form.html.
 * Аналог dict, який повертала _parse_object_form(form) в app.py.
 * Поля специфіки (rooms, floor, plotArea, purpose, ...) заповнюються
 * тільки для відповідного типу — решта залишаються null і ігноруються.
 */
public class RealEstateFormData {

    @NotNull
    private Integer typeId;

    @NotNull
    private Integer districtId;

    @NotNull
    private Integer statusId;

    @NotBlank
    private String street;

    @NotBlank
    private String houseNumber;

    private String apartmentNumber;

    @NotNull
    @Positive
    private BigDecimal totalArea;

    @NotNull
    private BigDecimal price;

    private String description;

    private String mainPhoto;

    // --- Квартира / Будинок / Офіс ---
    private Integer rooms;          // квартира, будинок
    private Integer floor;          // квартира, офіс
    private Integer totalFloors;    // квартира, будинок, офіс
    private BigDecimal plotArea;    // будинок
    private Integer roomsCount;     // офіс
    private String purpose;         // офіс

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public BigDecimal getPlotArea() {
        return plotArea;
    }

    public void setPlotArea(BigDecimal plotArea) {
        this.plotArea = plotArea;
    }

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
