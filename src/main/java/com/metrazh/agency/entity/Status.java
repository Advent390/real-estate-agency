package com.metrazh.agency.entity;

import jakarta.persistence.*;

/**
 * Довідник статусів об'єкта: У продажу / Вільний від оренди /
 * Продано (В архіві) / Орендовано (В архіві).
 * Ідентифікатори 1,2 = активні; 3,4 = архівні (див. RealEstateService).
 */
@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public Status() {
    }

    public Status(Integer statusId, String name) {
        this.statusId = statusId;
        this.name = name;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
