package com.metrazh.agency.entity;

import jakarta.persistence.*;

/** Додаткові фото об'єкта (1:M з RealEstate). */
@Entity
@Table(name = "real_estate_photos")
public class RealEstatePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_id", nullable = false)
    private RealEstate realEstate;

    @Column(name = "photo_path", nullable = false, length = 255)
    private String photoPath;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
