package com.metrazh.agency.entity;

import jakarta.persistence.*;

/** Специфіка для квартир. Спільний PK з real_estate (object_id). */
@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @Column(name = "object_id")
    private Integer objectId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "object_id")
    private RealEstate realEstate;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer floor;

    @Column(name = "total_floors", nullable = false)
    private Integer totalFloors;

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
}
