package com.metrazh.agency.repository;

import com.metrazh.agency.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
}
