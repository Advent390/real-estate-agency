package com.metrazh.agency.repository;

import com.metrazh.agency.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    List<District> findAllByOrderByNameAsc();
}
