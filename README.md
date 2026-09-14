# Метраж — Агенція нерухомості (Java/Spring Boot версія)

## Налаштування бази даних

Створіть порожню базу (таблиці Hibernate створить сам):

```sql
CREATE DATABASE real_estate_agency;
```

Перевірте/змініть підключення в
`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/real_estate_agency
spring.datasource.username=postgres
spring.datasource.password=1234
```

## Запуск

```bash
mvn spring-boot:run
```

або зберіть jar і запустіть окремо:

```bash
mvn clean package
java -jar target/real-estate-agency.jar
```

Застосунок підніметься на **http://localhost:5000** (порт налаштований
такий самий, як в оригінальному `app.run(port=5000)`).

При першому старті `DataInitializer` автоматично наповнить базу
довідниками, тестовими об'єктами, адміном і клієнтами (аналог
`init_db.py`). При наступних запусках наповнення пропускається,
якщо довідники вже не порожні.

## Тестові облікові дані

| Роль    | Логін / Email          | Пароль      |
|---------|------------------------|-------------|
| Адмін   | `admin`                 | `admin123`  |
| Клієнт  | `ivan@example.com`      | `client123` |
| Клієнт  | `maria@example.com`     | `client123` |
| Клієнт  | `jack@example.com`      | `client123` |

- Адмін-панель: http://localhost:5000/admin/login
- Каталог: http://localhost:5000/

## Структура проєкту

```
src/main/java/com/metrazh/agency/
├── RealEstateAgencyApplication.java   — точка входу
├── config/          — WebConfig (interceptor'и), DataInitializer
├── interceptor/     — ClientAuthInterceptor, AdminAuthInterceptor
├── advice/          — GlobalModelAdvice (глобальні дані для шаблонів)
├── entity/          — JPA-сутності
├── repository/      — Spring Data JPA репозиторії
├── specification/   — динамічні фільтри (Criteria API)
├── dto/             — DTO форм і фільтрів
├── service/         — бізнес-логіка
├── controller/      — HTTP-маршрути
└── util/            — FlashService, PriceFormatter, SessionKeys

src/main/resources/
├── application.properties
├── templates/       — Thymeleaf-шаблони (структура як в оригіналі)
│   ├── fragments/common.html   — header/footer/flash (аналог base.html)
│   ├── catalog.html, object.html, login.html, cabinet.html
│   └── admin/       — login, dashboard, form, viewings, clients, reports
└── static/
    ├── css/style.css
    └── img/office1.png
```
