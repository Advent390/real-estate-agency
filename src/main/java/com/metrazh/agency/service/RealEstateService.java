package com.metrazh.agency.service;

import com.metrazh.agency.dto.DashboardStats;
import com.metrazh.agency.dto.RealEstateFormData;
import com.metrazh.agency.dto.SearchFilters;
import com.metrazh.agency.entity.*;
import com.metrazh.agency.repository.*;
import com.metrazh.agency.specification.RealEstateSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final RealEstatePhotoRepository photoRepository;
    private final ObjectTypeRepository objectTypeRepository;
    private final DistrictRepository districtRepository;
    private final StatusRepository statusRepository;
    private final ClientRepository clientRepository;
    private final ViewingRepository viewingRepository;

    public RealEstateService(RealEstateRepository realEstateRepository,
                              RealEstatePhotoRepository photoRepository,
                              ObjectTypeRepository objectTypeRepository,
                              DistrictRepository districtRepository,
                              StatusRepository statusRepository,
                              ClientRepository clientRepository,
                              ViewingRepository viewingRepository) {
        this.realEstateRepository = realEstateRepository;
        this.photoRepository = photoRepository;
        this.objectTypeRepository = objectTypeRepository;
        this.districtRepository = districtRepository;
        this.statusRepository = statusRepository;
        this.clientRepository = clientRepository;
        this.viewingRepository = viewingRepository;
    }

    // ---- Довідники ----

    public List<ObjectType> getObjectTypes() {
        return objectTypeRepository.findAllByOrderByTypeIdAsc();
    }

    public List<District> getDistricts() {
        return districtRepository.findAllByOrderByNameAsc();
    }

    public List<Status> getStatuses() {
        return statusRepository.findAllByOrderByStatusIdAsc();
    }

    // ---- Пошук / читання ----

    public List<RealEstate> search(SearchFilters filters) {
        return realEstateRepository.findAll(RealEstateSpecifications.fromFilters(filters));
    }

    public RealEstate getById(Integer objectId) {
        return realEstateRepository.findById(objectId)
                .orElseThrow(() -> new EntityNotFoundException("Об'єкт #" + objectId + " не знайдено"));
    }

    public List<RealEstatePhoto> getPhotos(Integer objectId) {
        return photoRepository.findByRealEstate_ObjectIdOrderBySortOrderAscPhotoIdAsc(objectId);
    }

    // ---- CRUD (адмінка) ----

    @Transactional
    public RealEstate create(RealEstateFormData data) {
        RealEstate re = new RealEstate();
        applyCommonFields(re, data);
        applyTypeSpecificFields(re, data);
        return realEstateRepository.save(re);
    }

    @Transactional
    public RealEstate update(Integer objectId, RealEstateFormData data) {
        RealEstate re = getById(objectId);
        applyCommonFields(re, data);

        // Скидаємо стару специфіку — так само, як оригінал видаляв рядки
        // з усіх трьох таблиць перед вставкою нової.
        re.setApartment(null);
        re.setHouse(null);
        re.setOffice(null);

        applyTypeSpecificFields(re, data);
        return realEstateRepository.save(re);
    }

    @Transactional
    public void delete(Integer objectId) {
        realEstateRepository.deleteById(objectId);
    }

    @Transactional
    public void updateStatus(Integer objectId, Integer statusId) {
        RealEstate re = getById(objectId);
        re.setStatus(statusRepository.getReferenceById(statusId));
        realEstateRepository.save(re);
    }

    private void applyCommonFields(RealEstate re, RealEstateFormData data) {
        re.setType(objectTypeRepository.getReferenceById(data.getTypeId()));
        re.setDistrict(districtRepository.getReferenceById(data.getDistrictId()));
        re.setStatus(statusRepository.getReferenceById(data.getStatusId()));
        re.setStreet(data.getStreet());
        re.setHouseNumber(data.getHouseNumber());
        re.setApartmentNumber(
                (data.getApartmentNumber() == null || data.getApartmentNumber().isBlank())
                        ? null : data.getApartmentNumber());
        re.setTotalArea(data.getTotalArea());
        re.setPrice(data.getPrice());
        re.setDescription(data.getDescription());
        re.setMainPhoto(
                (data.getMainPhoto() == null || data.getMainPhoto().isBlank())
                        ? "/img/placeholder.jpg" : data.getMainPhoto());
    }

    private void applyTypeSpecificFields(RealEstate re, RealEstateFormData data) {
        switch (data.getTypeId()) {
            case 1 -> { // Квартира
                Apartment apartment = new Apartment();
                apartment.setRealEstate(re);
                apartment.setRooms(data.getRooms());
                apartment.setFloor(data.getFloor());
                apartment.setTotalFloors(data.getTotalFloors());
                re.setApartment(apartment);
            }
            case 2 -> { // Будинок
                House house = new House();
                house.setRealEstate(re);
                house.setRooms(data.getRooms());
                house.setTotalFloors(data.getTotalFloors());
                house.setPlotArea(data.getPlotArea());
                re.setHouse(house);
            }
            case 3 -> { // Офіс
                Office office = new Office();
                office.setRealEstate(re);
                office.setRoomsCount(data.getRoomsCount());
                office.setFloor(data.getFloor());
                office.setTotalFloors(data.getTotalFloors());
                office.setPurpose(data.getPurpose());
                re.setOffice(office);
            }
            default -> throw new IllegalArgumentException("Невідомий type_id: " + data.getTypeId());
        }
    }

    // ---- Статистика для адмін-дашборду ----

    public DashboardStats getStats() {
        long total = realEstateRepository.count();
        long active = realEstateRepository.count(RealEstateSpecifications.fromFilters(activeFilter()));
        long archived = total - active;
        long clients = clientRepository.count();
        long pending = viewingRepository.countByRequestStatus("нова");
        return new DashboardStats(total, active, archived, clients, pending);
    }

    private SearchFilters activeFilter() {
        SearchFilters f = new SearchFilters();
        f.setIncludeArchived(false);
        return f;
    }

    /** Статуси, що вважаються архівними (для шаблонів). */
    public static boolean isArchivedStatus(Integer statusId) {
        return statusId != null && Arrays.asList(3, 4).contains(statusId);
    }
}
