package com.metrazh.agency.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "houses")
public class House {

    @Id
    @Column(name = "object_id")
    private Integer objectId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "object_id")
    private RealEstate realEstate;

    @Column(nullable = false)
    private Integer rooms;

    @Column(name = "total_floors", nullable = false)
    private Integer totalFloors;

    @Column(name = "plot_area", nullable = false, precision = 6, scale = 2)
    private BigDecimal plotArea;

    public Integer getObjectId() {
        return objectId;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
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
}
