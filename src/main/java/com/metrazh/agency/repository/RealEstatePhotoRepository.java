package com.metrazh.agency.repository;

import com.metrazh.agency.entity.RealEstatePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealEstatePhotoRepository extends JpaRepository<RealEstatePhoto, Integer> {

    List<RealEstatePhoto> findByRealEstate_ObjectIdOrderBySortOrderAscPhotoIdAsc(Integer objectId);
}
