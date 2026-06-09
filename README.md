# SkillTrade

Веб-приложение для бартерного обмена навыками между пользователями.
Пользователь публикует объявление: «что умею» ↔ «что хочу научиться», другие
откликаются оффером, после завершённого обмена стороны могут оставить отзыв.

---

## Стек

| Слой             | Технология                                                |
|------------------|-----------------------------------------------------------|
| Бэкенд           | Spring Boot 3.3, Spring MVC + REST                        |
| Безопасность     | Spring Security 6 (form login + HTTP Basic, BCrypt, CSRF) |
| ORM / БД         | Spring Data JPA + Hibernate, PostgreSQL 16                |
| Кэш              | Redis 7 (`@Cacheable` / `@CacheEvict`)                    |
| Шаблонизатор     | FreeMarker                                                |
| HTTP-клиент      | OkHttp (для HuggingFace и LibreTranslate)                 |
| Документация API | SpringDoc OpenAPI 2 / Swagger UI                          |
| Фронт            | HTML5, CSS3, vanilla JS, Fetch API (AJAX)                 |
| Сборка / запуск  | Maven, Docker, docker-compose                             |

---

## Что умеет

- Регистрация и логин (BCrypt, CSRF на формах, роли `USER` / `ADMIN`)
- CRUD объявлений + AJAX-фильтрация (категория / город / мин. рейтинг автора)
- Офферы: отправка через AJAX, принятие / отклонение / завершение (`DONE`)
- Отзывы (1-5) после завершённого обмена
- **Анализ тональности** отзывов через HuggingFace Inference API
  (модель `cardiffnlp/twitter-xlm-roberta-base-sentiment`, мультиязычная) →
  цветной бейдж POSITIVE / NEUTRAL / NEGATIVE (к сожалению этот сервис не работает с русским языком и я не нашел рабочей версии под свою задачу локально поднимать ИИ для этого я не стал)
- **Перевод описания** через локальный LibreTranslate в Docker (AJAX-кнопка) (с коробки LibreTranslate не поддерживал русский язык, и я нашел [репозиторий](https://github.com/LibreTranslate/LibreTranslate-Models/blob/main/en_ru.argosmodel) с данными для первода с английского на русский)
- Профиль: bio, город, средний рейтинг (кэш в Redis), список навыков с AJAX-управлением
- REST API под `/api/v1/**` со Swagger UI
- Админ-эндпоинты `/api/v1/admin/**` (категории, навыки, поиск юзеров через
  CriteriaBuilder, блокировка) — управление из Postman / Swagger

## Запуск

```bash
docker compose up -d --build
```

Поднимет четыре контейнера:

| Сервис              | Порт на хосте | Назначение                      |
| ------------------- | ------------- | ------------------------------- |
| `st-app`            | 8080          | Spring Boot                     |
| `st-postgres`       | 5433          | PostgreSQL (5432 внутри сети)   |
| `st-redis`          | —             | Redis (только в docker-network) |
| `st-libretranslate` | 5000          | переводчик                      |

Открыть:
- http://localhost:8080/ — главная
- http://localhost:8080/swagger-ui.html — REST API
