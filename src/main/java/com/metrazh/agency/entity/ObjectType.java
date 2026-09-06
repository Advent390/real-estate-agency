package com.metrazh.agency.entity;

import jakarta.persistence.*;

/** Довідник типів об'єктів: Квартира / Будинок / Офіс. */
@Entity
@Table(name = "object_types")
public class ObjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public ObjectType() {
    }

    public ObjectType(Integer typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
