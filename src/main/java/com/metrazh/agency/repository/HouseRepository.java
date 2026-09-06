package com.metrazh.agency.repository;

import com.metrazh.agency.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Integer> {
}
