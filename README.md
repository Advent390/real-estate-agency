# Метраж — Агенція нерухомості (Java/Spring Boot версія)

Це перенесення курсової роботи з **Flask + PostgreSQL (Python)** на
**Spring Boot + Thymeleaf + JPA/Hibernate (Java)**.

## Відповідність оригіналу

| Python (оригінал)          | Java (ця версія)                                      |
|-----------------------------|--------------------------------------------------------|
| `app.py`                    | пакет `controller/*`                                    |
| `auth.py`                   | `service/AuthService`, `interceptor/*`, `util/SessionKeys` |
| `db.py`                     | `repository/*`, `specification/RealEstateSpecifications`, `service/*` |
| `schema.sql`                | JPA-сутності в `entity/*` (Hibernate сам створює таблиці, `ddl-auto=update`) |
| `seed.sql` + `init_db.py`   | `config/DataInitializer`                                |
| `werkzeug.security`         | `spring-security-crypto` (BCrypt)                       |
| Jinja2 `templates/*.html`   | Thymeleaf `templates/*.html`                            |
| Flask `flash()`             | `util/FlashService` (сесійний аналог)                    |
| `@client_required` / `@admin_required` | `ClientAuthInterceptor` / `AdminAuthInterceptor` |
| `@app.template_filter('price')` | `util/PriceFormatter` (bean `@priceFormatter`)     |

## Вимоги

- **JDK 17+**
- **Maven 3.9+** (або використовуйте IDE — IntelliJ IDEA сам підтягне залежності)
- **PostgreSQL 14+**

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

## Важливе застереження

Цей код був написаний і ретельно перевірений вручну (баланс дужок,
відповідність сигнатур, логіка перенесена 1:1 з Python-версії), але
**не був скомпільований і протестований** у середовищі, де я його
створював — там немає доступу до інтернету, а отже й до Maven Central
для завантаження залежностей Spring Boot.

Тому перед тим, як показувати проєкт викладачу/захищати курсову:

1. Виконайте `mvn clean compile` і виправте помилки компіляції,
   якщо Maven на них вкаже (це можуть бути дрібні неточності на
   кшталт назви методу чи імпорту — за обсягу ~50 файлів це
   нормальна практика для будь-якого нового проєкту).
2. Запустіть застосунок і пройдіться по всіх сторінках: каталог,
   фільтри, картка об'єкта, реєстрація/логін, кабінет (обране,
   заявка на перегляд), адмінка (дашборд, форма створення/редагування
   об'єкта, заявки, клієнти, звіти).
3. Якщо десь Hibernate поскаржиться на схему при `ddl-auto=update` —
   найпростіше видалити базу і створити наново (`DROP DATABASE` /
   `CREATE DATABASE`), Hibernate згенерує таблиці з нуля.

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
