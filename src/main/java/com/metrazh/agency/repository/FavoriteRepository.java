package com.metrazh.agency.repository;

import com.metrazh.agency.entity.Favorite;
import com.metrazh.agency.entity.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    boolean existsByClient_ClientIdAndRealEstate_ObjectId(Integer clientId, Integer objectId);

    void deleteByClient_ClientIdAndRealEstate_ObjectId(Integer clientId, Integer objectId);

    List<Favorite> findByClient_ClientIdOrderByCreatedAtDesc(Integer clientId);
}
