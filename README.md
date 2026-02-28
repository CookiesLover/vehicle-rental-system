# ВТиПО-33 Сайлау Алмаз
## Веб программалау

# Vehicle Rental System (REST API + Web)
Система аренды автомобилей на Spring Boot: REST API для работы с данными и веб-интерфейс на Thymeleaf для пользователей и администратора.

## Стек технологий
- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- Thymeleaf
- H2 Database (in-memory)
- Swagger/OpenAPI (`springdoc-openapi`)
- Maven

## Основные возможности
- Регистрация и вход пользователей.
- Роли: `ROLE_USER` и `ROLE_ADMIN`.
- Каталог автомобилей с фильтрацией.
- Создание заявок на аренду.
- Просмотр и отмена своих аренд.
- Админ-управление автомобилями, арендой и пользователями.
- Автоматический расчет суммы аренды на основе периода и цены за день.

## Запуск локально
1. Откройте проект в IntelliJ IDEA.
2. Запустите класс `VehicleRentalSystemApplication`.
3. Приложение стартует на `http://localhost:8080`.

Альтернатива через Maven:
```bash
mvn spring-boot:run
```

## Полезные ссылки
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- H2 Console: `http://localhost:8080/h2-console`

## Тестовые пользователи (seed)
- ADMIN: `admin@rent.local` / `admin123`
- USER: `user@rent.local` / `user123`

## API Endpoints
### 1. Auth
| Метод | Эндпоинт | Описание |
|---|---|---|
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Логин |

### 2. Cars
| Метод | Эндпоинт | Описание |
|---|---|---|
| GET | `/api/cars` | Список авто (с фильтрами) |
| GET | `/api/cars/{id}` | Авто по ID |
| POST | `/api/cars` | Создать авто (ADMIN) |
| PUT | `/api/cars/{id}` | Обновить авто (ADMIN) |
| DELETE | `/api/cars/{id}` | Удалить авто (ADMIN) |

Фильтры для `GET /api/cars`:
- `brand`
- `minPrice`
- `maxPrice`
- `vehicleClass`
- `year`
- `status` (`AVAILABLE`, `RENTED`, `MAINTENANCE`)

### 3. Rentals
| Метод | Эндпоинт | Описание |
|---|---|---|
| POST | `/api/rentals` | Создать аренду (USER) |
| GET | `/api/rentals/my` | Мои аренды (USER) |
| PATCH | `/api/rentals/{id}/cancel` | Отменить свою аренду (USER) |
| GET | `/api/rentals` | Все аренды (ADMIN) |
| PATCH | `/api/rentals/{id}/status` | Сменить статус аренды (ADMIN) |
| PATCH | `/api/rentals/{id}/finish` | Завершить аренду (ADMIN) |

### 4. Users (ADMIN)
| Метод | Эндпоинт | Описание |
|---|---|---|
| GET | `/api/users` | Список пользователей |
| PATCH | `/api/users/{id}/role` | Смена роли |
| PATCH | `/api/users/{id}/toggle-enabled` | Включить/выключить пользователя |

## Примеры JSON-запросов
### Регистрация
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "123456",
  "phone": "+7-777-777-77-77"
}
```

### Добавить автомобиль (ADMIN)
```http
POST http://localhost:8080/api/cars
Content-Type: application/json
```

```json
{
  "brand": "Toyota",
  "model": "Camry",
  "year": 2023,
  "vehicleClass": "Sedan",
  "transmission": "Automatic",
  "fuel": "Petrol",
  "seats": 5,
  "pricePerDay": 95.50,
  "imageUrl": "https://example.com/camry.jpg"
}
```

### Создать аренду (USER)
```http
POST http://localhost:8080/api/rentals
Content-Type: application/json
```

```json
{
  "carId": 1,
  "startDate": "2026-03-05",
  "endDate": "2026-03-10"
}
```

### Изменить статус аренды (ADMIN)
```http
PATCH http://localhost:8080/api/rentals/1/status
Content-Type: application/json
```

```json
{
  "status": "APPROVED"
}
```

## Web-страницы
- `/login`
- `/register`
- `/home`
- `/home/{id}`
- `/my-rentals`
- `/admin`
- `/admin/cars`
- `/admin/rentals`
- `/admin/users`
