package com.metrazh.agency.service;

import com.metrazh.agency.entity.Favorite;
import com.metrazh.agency.repository.ClientRepository;
import com.metrazh.agency.repository.FavoriteRepository;
import com.metrazh.agency.repository.RealEstateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Аналог секції "ОБРАНЕ" з db.py. */
@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ClientRepository clientRepository;
    private final RealEstateRepository realEstateRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                            ClientRepository clientRepository,
                            RealEstateRepository realEstateRepository) {
        this.favoriteRepository = favoriteRepository;
        this.clientRepository = clientRepository;
        this.realEstateRepository = realEstateRepository;
    }

    public boolean isInFavorites(Integer clientId, Integer objectId) {
        return favoriteRepository.existsByClient_ClientIdAndRealEstate_ObjectId(clientId, objectId);
    }

    @Transactional
    public void addToFavorites(Integer clientId, Integer objectId) {
        if (isInFavorites(clientId, objectId)) {
            return; // ON CONFLICT DO NOTHING — як в оригіналі
        }
        Favorite favorite = new Favorite(
                clientRepository.getReferenceById(clientId),
                realEstateRepository.getReferenceById(objectId));
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFromFavorites(Integer clientId, Integer objectId) {
        favoriteRepository.deleteByClient_ClientIdAndRealEstate_ObjectId(clientId, objectId);
    }

    public List<Favorite> getClientFavorites(Integer clientId) {
        return favoriteRepository.findByClient_ClientIdOrderByCreatedAtDesc(clientId);
    }
}
