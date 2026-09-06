package com.metrazh.agency.repository;

import com.metrazh.agency.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RealEstateRepository
        extends JpaRepository<RealEstate, Integer>, JpaSpecificationExecutor<RealEstate> {
}
