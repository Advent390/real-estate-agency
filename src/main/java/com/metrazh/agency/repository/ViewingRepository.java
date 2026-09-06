package com.metrazh.agency.repository;

import com.metrazh.agency.entity.Viewing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ViewingRepository extends JpaRepository<Viewing, Integer>, JpaSpecificationExecutor<Viewing> {

    List<Viewing> findByClient_ClientIdOrderByViewingDateDesc(Integer clientId);

    List<Viewing> findAllByOrderByViewingDateDesc();

    long countByRequestStatus(String requestStatus);
}
