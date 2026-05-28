🔍 API Monitoring Service
Full-stack приложение для мониторинга публичных API, сохранения результатов, обработки ошибок с помощью Spring Retry и асинхронной отправки событий в Apache Kafka.









📖 Оглавление
О проекте
Архитектура
Технологии
Быстрый старт
Backend: Настройка и запуск
Frontend: Настройка и запуск
API Документация
Безопасность
Структура проекта
Troubleshooting
🚀 О проекте
Система выполняет периодический опрос публичного API (например, курсов криптовалют), сохраняет результаты в базу данных и отправляет события в Kafka.
Ключевые возможности:
Планировщик: Опрос API каждую минуту через @Scheduled.
Надежность: Повторные попытки запроса при ошибках (3 попытки, exponential backoff) через Spring Retry.
Асинхронность: Отправка успешных ответов в топик api-data, ошибок — в api-errors через Apache Kafka.
Безопасность: Защита REST API через Spring Security (Basic Auth) с разграничением ролей (USER/ADMIN).
Интерфейс: Современный SPA на Angular 17 для просмотра статистики и CRUD-операций.
🏗 Архитектура
mermaid





Code
Preview
🛠 Технологии
Backend
Java 17
Spring Boot 3.2.x
Spring Data JPA (Hibernate)
Spring Security (Basic Auth)
Spring Retry (Exponential Backoff)
Apache Kafka (Spring Kafka)
Database: PostgreSQL (Production) / H2 (Dev)
Build Tool: Maven
Frontend
Angular 17+ (Standalone Components)
RxJS (Reactive Programming)
Axios / HttpClient (HTTP Client)
CSS3 (Custom Styles, No heavy UI libs)
⚡ Быстрый старт
Предварительные требования
JDK 17 или выше (java -version)
Node.js 18+ и npm (node -v)
Apache Kafka & Zookeeper (или KRaft) запущены локально на порту 9092
PostgreSQL (опционально, по умолчанию используется H2)
1. Клонируйте репозиторий
   bash
   12
2. Запуск Backend
   Перейдите в папку backend и запустите приложение:
   bash
   12
   Backend доступен на: http://localhost:8080
3. Запуск Frontend
   В отдельном терминале перейдите в папку frontend:
   bash
   1234
   Frontend доступен на: http://localhost:4200
   ⚙️ Backend: Настройка и запуск
   Конфигурация БД
   По умолчанию используется встроенная база H2. Для переключения на PostgreSQL, измените src/main/resources/application.yml:
   yaml
   123456789
   Конфигурация Kafka
   Убедитесь, что Kafka запущена. Настройки находятся в application.yml:
   yaml
   123456
   Параметры опроса API
   yaml
   1234
   🎨 Frontend: Настройка и запуск
   Установка зависимостей
   bash
   12
   Переменные окружения
   Если бэкенд запускается не на стандартном порту, проверьте файл src/app/core/services/api.service.ts:
   typescript
   1
   Сборка для Production
   bash
   12
   📚 API Документация
   Приложение использует Basic Authentication.
   Учетные данные по умолчанию
   Роль
   Логин
   Пароль
   Доступ
   ADMIN
   admin
   admin123
   Полный доступ (CRUD + Status)
   USER
   user
   user123
   Только чтение статуса (/status)
   (Примечание: Пользователи настроены в application.yml или SecurityConfig)
   Эндпоинты
1. Проверка статуса
   GET /api/status
   Roles: USER, ADMIN
   Response: "Service is running"
2. Получение данных
   GET /api/data
   Roles: ADMIN
   Description: Возвращает последние 10 записей.
3. Создание записи
   POST /api/data
   Roles: ADMIN
   Body:
   json
   1234
4. Обновление записи
   PUT /api/data/{id}
   Roles: ADMIN
5. Удаление записи
   DELETE /api/data/{id}
   Roles: ADMIN
   Пример запроса (cURL)
   bash
   1
   🔒 Безопасность
   Тип авторизации: Basic Auth.
   Механизм: Заголовок Authorization: Base64(username:password).
   CORS: Настроен для разрешения запросов с http://localhost:4200.
   CSRF: Отключен для Stateless API.
   📂 Структура проекта
   text
   123456789101112131415161718192021
   🔧 Troubleshooting
1. Ошибка подключения к Kafka
   Симптом: Connection refused или таймауты в логах.
   Решение: Убедитесь, что Kafka запущена на порту 9092.
   bash
   1234
2. Ошибка CORS во Frontend
   Симптом: Access-Control-Allow-Origin error в консоли браузера.
   Решение: Проверьте CorsConfig.java в бэкенде. Убедитесь, что разрешен origin http://localhost:4200.
3. 401 Unauthorized
   Симптом: Frontend не может получить данные.
   Решение:
   Проверьте правильность логина/пароля (admin / admin123).
   Убедитесь, что у пользователя роль ADMIN для доступа к /api/data.
   Проверьте консоль браузера (Network tab), передается ли заголовок Authorization.
4. База данных не создается
   Решение: Если используете H2, убедитесь, что в application.yml стоит ddl-auto: create-drop или update. Для PostgreSQL создайте базу вручную перед запуском.
   👤 Автор
   Разработано в рамках учебного задания по архитектуре Enterprise-приложений на Spring Boot.
   💡 Совет: Для удобного тестирования API рекомендуется использовать Postman или Swagger UI (если подключен).