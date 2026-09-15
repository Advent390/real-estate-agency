package com.metrazh.agency.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Integer districtId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    public District() {
    }

    public District(Integer districtId, String name) {
        this.districtId = districtId;
        this.name = name;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
