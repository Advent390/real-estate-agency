package com.metrazh.agency.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Обране: чиста M:N таблиця-зв'язок клієнт↔об'єкт. */
@Entity
@Table(name = "favorites")
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_id", nullable = false)
    private RealEstate realEstate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Favorite() {
    }

    public Favorite(Client client, RealEstate realEstate) {
        this.client = client;
        this.realEstate = realEstate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
