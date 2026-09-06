package com.metrazh.agency.repository;

import com.metrazh.agency.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    List<Status> findAllByOrderByStatusIdAsc();
}
