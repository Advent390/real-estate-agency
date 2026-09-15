package com.metrazh.agency.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "offices")
public class Office {

    @Id
    @Column(name = "object_id")
    private Integer objectId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "object_id")
    private RealEstate realEstate;

    @Column(name = "rooms_count", nullable = false)
    private Integer roomsCount;

    @Column(nullable = false)
    private Integer floor;

    @Column(name = "total_floors", nullable = false)
    private Integer totalFloors;

    /** офіс / магазин / склад */
    @Column(nullable = false, length = 50)
    private String purpose;

    public Integer getObjectId() {
        return objectId;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
