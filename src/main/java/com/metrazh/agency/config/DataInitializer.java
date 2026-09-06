package com.metrazh.agency.config;

import com.metrazh.agency.entity.*;
import com.metrazh.agency.repository.*;
import com.metrazh.agency.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Наповнення бази при першому старті застосунку.
 * Аналог init_db.py (schema.sql виконує Hibernate через ddl-auto=update,
 * а тут відтворено вміст seed.sql + створення адміна й тестових клієнтів
 * з валідними хешами паролів, як робив init_db.py).
 * Ідемпотентно: якщо довідники вже наповнені — нічого не робить.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ObjectTypeRepository objectTypeRepository;
    private final DistrictRepository districtRepository;
    private final StatusRepository statusRepository;
    private final AdminUserRepository adminUserRepository;
    private final ClientRepository clientRepository;
    private final RealEstateRepository realEstateRepository;
    private final ViewingRepository viewingRepository;
    private final FavoriteRepository favoriteRepository;
    private final AuthService authService;

    public DataInitializer(ObjectTypeRepository objectTypeRepository,
                            DistrictRepository districtRepository,
                            StatusRepository statusRepository,
                            AdminUserRepository adminUserRepository,
                            ClientRepository clientRepository,
                            RealEstateRepository realEstateRepository,
                            ViewingRepository viewingRepository,
                            FavoriteRepository favoriteRepository,
                            AuthService authService) {
        this.objectTypeRepository = objectTypeRepository;
        this.districtRepository = districtRepository;
        this.statusRepository = statusRepository;
        this.adminUserRepository = adminUserRepository;
        this.clientRepository = clientRepository;
        this.realEstateRepository = realEstateRepository;
        this.viewingRepository = viewingRepository;
        this.favoriteRepository = favoriteRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (objectTypeRepository.count() > 0) {
            return; // вже ініціалізовано
        }

        System.out.println("Наповнення бази початковими даними...");

        // --- Довідники ---
        ObjectType typeApartment = objectTypeRepository.save(new ObjectType(null, "Квартира"));
        ObjectType typeHouse = objectTypeRepository.save(new ObjectType(null, "Будинок"));
        ObjectType typeOffice = objectTypeRepository.save(new ObjectType(null, "Офіс"));

        District dCentral = districtRepository.save(new District(null, "Центральний"));
        District dZavodsky = districtRepository.save(new District(null, "Заводський"));
        District dInhulsky = districtRepository.save(new District(null, "Інгульський"));
        District dKorabelny = districtRepository.save(new District(null, "Корабельний"));

        Status stForSale = statusRepository.save(new Status(null, "У продажу"));
        Status stForRent = statusRepository.save(new Status(null, "Вільний від оренди"));
        Status stSold = statusRepository.save(new Status(null, "Продано / В архіві"));
        Status stRented = statusRepository.save(new Status(null, "Орендовано / В архіві"));

        // --- Адмін і тестові клієнти (аналог create_users() з init_db.py) ---
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPasswordHash(authService.hashPassword("admin123"));
        admin.setFullName("Адміністратор системи");
        adminUserRepository.save(admin);

        Client ivan = new Client();
        ivan.setEmail("ivan@example.com");
        ivan.setPasswordHash(authService.hashPassword("client123"));
        ivan.setFullName("Іван Петренко");
        ivan.setPhone("+380501234567");
        clientRepository.save(ivan);

        Client maria = new Client();
        maria.setEmail("maria@example.com");
        maria.setPasswordHash(authService.hashPassword("client123"));
        maria.setFullName("Марія Коваленко");
        maria.setPhone("+380671112233");
        clientRepository.save(maria);

        // --- Об'єкти нерухомості (аналог seed.sql) ---

        RealEstate re1 = baseObject(typeApartment, dCentral, stForSale,
                "Соборна", "15", "42", "65.5", "1850000",
                "Простора 2-кімнатна квартира в центрі міста. Свіжий ремонт, меблі залишаються.",
                "/img/apt1.jpg");
        Apartment a1 = apartment(re1, 2, 5, 9);
        re1.setApartment(a1);
        realEstateRepository.save(re1);

        RealEstate re2 = baseObject(typeApartment, dInhulsky, stForSale,
                "Космонавтів", "83", "17", "42.0", "980000",
                "1-кімнатна квартира з гарним краєвидом. Поруч школа, магазини, парк.",
                "/img/apt2.jpg");
        re2.setApartment(apartment(re2, 1, 7, 9));
        realEstateRepository.save(re2);

        RealEstate re3 = baseObject(typeApartment, dCentral, stForSale,
                "Шевченка", "64", "8", "85.3", "2400000",
                "Велика 3-кімнатна квартира в новобудові. Закрита територія, паркінг.",
                "/img/apt3.jpg");
        re3.setApartment(apartment(re3, 3, 4, 16));
        realEstateRepository.save(re3);

        RealEstate re4 = baseObject(typeApartment, dZavodsky, stForRent,
                "Адміральська", "22", "5", "38.0", "12000",
                "Здається в оренду. Меблі та техніка є. Ціна за місяць.",
                "/img/apt4.jpg");
        re4.setApartment(apartment(re4, 1, 3, 5));
        realEstateRepository.save(re4);

        RealEstate re5 = baseObject(typeHouse, dKorabelny, stForSale,
                "Лазурна", "7", null, "145.0", "3200000",
                "Двоповерховий будинок з гаражем. Ділянка засаджена. Всі комунікації.",
                "/img/house1.jpg");
        House h5 = new House();
        h5.setRealEstate(re5);
        h5.setRooms(4);
        h5.setTotalFloors(2);
        h5.setPlotArea(new BigDecimal("8.5"));
        re5.setHouse(h5);
        realEstateRepository.save(re5);

        RealEstate re6 = baseObject(typeHouse, dInhulsky, stForSale,
                "Лісова", "32", null, "90.0", "1750000",
                "Затишний одноповерховий будинок. Ремонт потрібен косметичний.",
                "/img/house2.jpg");
        House h6 = new House();
        h6.setRealEstate(re6);
        h6.setRooms(3);
        h6.setTotalFloors(1);
        h6.setPlotArea(new BigDecimal("6.0"));
        re6.setHouse(h6);
        realEstateRepository.save(re6);

        RealEstate re7 = baseObject(typeOffice, dCentral, stForSale,
                "Соборна", "40", "102", "78.0", "1900000",
                "Офісне приміщення на першому поверсі бізнес-центру. Окремий вхід.",
                "/img/office1.png");
        Office o7 = new Office();
        o7.setRealEstate(re7);
        o7.setRoomsCount(4);
        o7.setFloor(1);
        o7.setTotalFloors(5);
        o7.setPurpose("офіс");
        re7.setOffice(o7);
        realEstateRepository.save(re7);

        RealEstate re8 = baseObject(typeOffice, dZavodsky, stForRent,
                "Заводська", "11", "1", "55.0", "25000",
                "Приміщення під магазин або салон. Окремий вхід з вулиці. Оренда за місяць.",
                "/img/office2.jpg");
        Office o8 = new Office();
        o8.setRealEstate(re8);
        o8.setRoomsCount(2);
        o8.setFloor(1);
        o8.setTotalFloors(9);
        o8.setPurpose("магазин");
        re8.setOffice(o8);
        realEstateRepository.save(re8);

        RealEstate re9 = baseObject(typeApartment, dCentral, stSold,
                "Грецька", "12", "23", "50.0", "1300000",
                "Продана у вересні 2025 року.",
                "/img/apt5.jpg");
        re9.setApartment(apartment(re9, 2, 6, 9));
        realEstateRepository.save(re9);

        // --- Заявки на перегляд (приклади) ---
        viewingRepository.save(viewing(ivan, re1, LocalDateTime.of(2026, 5, 15, 14, 0),
                "Хочу подивитись у вечірній час", "нова"));
        viewingRepository.save(viewing(ivan, re5, LocalDateTime.of(2026, 5, 12, 11, 0),
                "Цікавить стан ділянки", "підтверджена"));
        viewingRepository.save(viewing(maria, re3, LocalDateTime.of(2026, 5, 10, 16, 30),
                null, "проведена"));

        // --- Обране (приклади) ---
        favoriteRepository.save(new Favorite(ivan, re1));
        favoriteRepository.save(new Favorite(ivan, re3));
        favoriteRepository.save(new Favorite(ivan, re5));
        favoriteRepository.save(new Favorite(maria, re2));
        favoriteRepository.save(new Favorite(maria, re7));

        System.out.println("=".repeat(50));
        System.out.println("  Базу успішно ініціалізовано!");
        System.out.println("=".repeat(50));
        System.out.println("  Адмін:    логін=admin,             пароль=admin123");
        System.out.println("  Клієнт 1: email=ivan@example.com,  пароль=client123");
        System.out.println("  Клієнт 2: email=maria@example.com, пароль=client123");
        System.out.println("=".repeat(50));
    }

    private RealEstate baseObject(ObjectType type, District district, Status status,
                                   String street, String houseNumber, String apartmentNumber,
                                   String totalArea, String price, String description, String mainPhoto) {
        RealEstate re = new RealEstate();
        re.setType(type);
        re.setDistrict(district);
        re.setStatus(status);
        re.setStreet(street);
        re.setHouseNumber(houseNumber);
        re.setApartmentNumber(apartmentNumber);
        re.setTotalArea(new BigDecimal(totalArea));
        re.setPrice(new BigDecimal(price));
        re.setDescription(description);
        re.setMainPhoto(mainPhoto);
        return re;
    }

    private Apartment apartment(RealEstate re, int rooms, int floor, int totalFloors) {
        Apartment a = new Apartment();
        a.setRealEstate(re);
        a.setRooms(rooms);
        a.setFloor(floor);
        a.setTotalFloors(totalFloors);
        return a;
    }

    private Viewing viewing(Client client, RealEstate re, LocalDateTime date, String comment, String status) {
        Viewing v = new Viewing();
        v.setClient(client);
        v.setRealEstate(re);
        v.setViewingDate(date);
        v.setComment(comment);
        v.setRequestStatus(status);
        return v;
    }
}
