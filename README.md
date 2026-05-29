## 🔍 API Monitoring Service

Full-stack приложение для мониторинга публичных API, сохранения результатов, обработки ошибок с помощью Spring Retry и асинхронной отправки событий в Apache Kafka.

## 🚀 О проекте
Система выполняет периодический опрос публичного API (например, курсов криптовалют), сохраняет результаты в базу данных и отправляет события в Kafka.

**Ключевые возможности:**
- 🔄 **Планировщик**: Опрос API каждую минуту через `@Scheduled`
- 🛡️ **Надежность**: Повторные попытки запроса при ошибках (3 попытки, exponential backoff) через Spring Retry
- ⚡ **Асинхронность**: Отправка успешных ответов в топик `api-data`, ошибок — в `api-errors` через Apache Kafka
- 🔐 **Безопасность**: Защита REST API через Spring Security (Basic Auth) с разграничением ролей (USER/ADMIN)
- 🎨 **Интерфейс**: Современный SPA на Angular 17 для просмотра статистики и CRUD-операций

## 🛠 Технологии

**Backend**
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA (Hibernate)
- Spring Security (Basic Auth)
- Spring Retry (Exponential Backoff)
- Apache Kafka (Spring Kafka)
- Database: PostgreSQL (Production) / H2 (Dev)
- Build Tool: Maven

**Frontend**
- Angular 17+ (Standalone Components)
- RxJS (Reactive Programming)
- Axios / HttpClient (HTTP Client)
- CSS3 (Custom Styles, No heavy UI libs)

## ⚡ Быстрый старт

### 🔧 Предварительные требования
```bash
# Проверка версий
java -version          # JDK 17 или выше
node -v                # Node.js 18+
npm -v                 # npm 9+

JDK 17 или выше (java -version)
Node.js 18+ и npm (node -v)
Apache Kafka & Zookeeper (или KRaft) запущены локально на порту 9092
PostgreSQL (опционально, по умолчанию используется H2)
1. Клонируйте репозиторий
git clone <repository-url>
cd api-monitoring-service
2. Запуск Backend
   Перейдите в папку backend и запустите приложение:
cd backend
# Запуск через Maven (Linux/Mac)
./mvnw spring-boot:run
# Или для Windows
mvnw.cmd spring-boot:run
# Альтернативно: сборка и запуск JAR
./mvnw clean package -DskipTests
java -jar target/api-monitoring-service.jar
   Backend доступен на: http://localhost:8080
4. Запуск Frontend
# Откройте новый терминал и перейдите в папку frontend
cd frontend

# Установите зависимости (при первом запуске)
npm install

# Запустите сервер разработки
npx ng serve

# Или глобально установленный Angular CLI
ng serve

## 📸 Скриншоты
 <img width="1228" height="1082" alt="2026-05-28_10-29-36" src="https://github.com/user-attachments/assets/226cbd67-5b0d-44be-9cc2-9d28786969db" />
 
 <img width="2162" height="1077" alt="2026-05-28_10-37-41" src="https://github.com/user-attachments/assets/a28a1ab4-a75a-4da7-b4de-0072939c1e49" />
 
 <img width="2026" height="1116" alt="2026-05-28_10-38-17" src="https://github.com/user-attachments/assets/0879d224-51b9-485a-a0aa-c890633f5c1c" />


