package com.metrazh.agency.repository;

import com.metrazh.agency.entity.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer> {

    List<ObjectType> findAllByOrderByTypeIdAsc();
}
