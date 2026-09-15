package com.metrazh.agency.service;

import com.metrazh.agency.entity.Client;
import com.metrazh.agency.entity.RealEstate;
import com.metrazh.agency.entity.Viewing;
import com.metrazh.agency.repository.ClientRepository;
import com.metrazh.agency.repository.RealEstateRepository;
import com.metrazh.agency.repository.ViewingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ViewingService {

    private final ViewingRepository viewingRepository;
    private final ClientRepository clientRepository;
    private final RealEstateRepository realEstateRepository;

    public ViewingService(ViewingRepository viewingRepository,
                           ClientRepository clientRepository,
                           RealEstateRepository realEstateRepository) {
        this.viewingRepository = viewingRepository;
        this.clientRepository = clientRepository;
        this.realEstateRepository = realEstateRepository;
    }

    @Transactional
    public Viewing createViewing(Integer clientId, Integer objectId, LocalDateTime viewingDate, String comment) {
        Client client = clientRepository.getReferenceById(clientId);
        RealEstate realEstate = realEstateRepository.getReferenceById(objectId);

        Viewing viewing = new Viewing();
        viewing.setClient(client);
        viewing.setRealEstate(realEstate);
        viewing.setViewingDate(viewingDate);
        viewing.setComment(comment);
        return viewingRepository.save(viewing);
    }

    public List<Viewing> getClientViewings(Integer clientId) {
        return viewingRepository.findByClient_ClientIdOrderByViewingDateDesc(clientId);
    }

    public List<Viewing> getAllViewings() {
        return viewingRepository.findAllByOrderByViewingDateDesc();
    }

    @Transactional
    public void updateStatus(Integer viewingId, String newStatus) {
        Viewing viewing = viewingRepository.findById(viewingId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Заявку #" + viewingId + " не знайдено"));
        viewing.setRequestStatus(newStatus);
        viewingRepository.save(viewing);
    }
}
