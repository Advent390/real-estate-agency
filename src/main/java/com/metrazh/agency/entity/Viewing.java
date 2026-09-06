package com.metrazh.agency.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Заявка клієнта на перегляд об'єкта. */
@Entity
@Table(name = "viewings")
public class Viewing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viewing_id")
    private Integer viewingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_id", nullable = false)
    private RealEstate realEstate;

    @Column(name = "viewing_date", nullable = false)
    private LocalDateTime viewingDate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    /** нова / підтверджена / проведена / скасована */
    @Column(name = "request_status", nullable = false, length = 20)
    private String requestStatus = "нова";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Integer getViewingId() {
        return viewingId;
    }

    public void setViewingId(Integer viewingId) {
        this.viewingId = viewingId;
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

    public LocalDateTime getViewingDate() {
        return viewingDate;
    }

    public void setViewingDate(LocalDateTime viewingDate) {
        this.viewingDate = viewingDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
