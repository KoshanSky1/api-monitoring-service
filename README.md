📸 
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


