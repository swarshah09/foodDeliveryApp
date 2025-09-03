## ShopSphere - Microservices Demo

A complete microservices application showcasing Spring Cloud with Eureka discovery, API Gateway, Kafka messaging, MySQL persistence, and a modern React frontend.

### Tech Stack
- Backend: Java 17, Spring Boot 3.3.x, Spring Cloud 2023.x
- Services: `eureka-server`, `api-gateway`, `product-service`, `order-service`, `notification-service`
- Database: MySQL 8
- Messaging: Apache Kafka (Zookeeper for local dev)
- Frontend: React (Vite) + Tailwind CSS
- Containerization: Docker, Docker Compose

### Architecture
- Service registry via Eureka
- Dynamic routing via Spring Cloud Gateway (Discovery locator)
- Product Service: CRUD with JPA + Flyway migrations (MySQL)
- Order Service: JPA + Kafka producer publishes order events
- Notification Service: Kafka consumer logs order notifications
- React web app calls Gateway at `/product-service/...` and `/order-service/...`

---

## Run Locally (without Docker)
Prereqs: Java 17, Maven, Node 18+

1) Start infra using Docker for DB and Kafka only (optional but recommended):
```bash
docker compose up -d mysql zookeeper kafka
```

2) Run services (each in a separate terminal):
```bash
mvn -pl eureka-server -am spring-boot:run
mvn -pl api-gateway -am spring-boot:run
mvn -pl product-service -am spring-boot:run
mvn -pl order-service -am spring-boot:run
mvn -pl notification-service -am spring-boot:run
```

3) Run the React web app:
```bash
cd react-webapp
npm install
npm run dev
```
Dev UI: `http://localhost:5173`

Gateway: `http://localhost:8080`

Eureka: `http://localhost:8761`

---

## Docker Compose (full stack)
Build jars and images, then run the entire stack:
```bash
mvn -DskipTests package
docker compose build --no-cache
docker compose up -d
```
Access:
- UI: `http://localhost:5173`
- Eureka: `http://localhost:8761`
- Gateway: `http://localhost:8080`

The web app proxies `/api/...` to the gateway. Example calls via Gateway:
- `GET /product-service/api/products`
- `POST /order-service/api/orders`

---

## Configuration
- Local profile defaults point to `localhost` for MySQL (`root:root`) and Kafka (`localhost:9092`)
- Docker profile (`SPRING_PROFILES_ACTIVE=docker`) points to service names: `mysql`, `kafka`, `eureka-server`
- DB migrations: Flyway under each service `src/main/resources/db/migration`

Kafka topic used: `orders`

---

## Deployment Guidance
### Compose (dev/staging)
- Ensure jars are built, then `docker compose build && docker compose up -d`

### Kubernetes (outline)
1. Build and push images to a registry
2. Provision MySQL and Kafka (operators or managed services)
3. Deploy Eureka, Gateway, Product, Order, Notification as Deployments + Services
4. Expose Gateway via Ingress/LoadBalancer
5. Configure env vars and profile `docker` or a `k8s` profile
6. Set Gateway to use Eureka for service discovery

---

## Notes
- For production, add authentication/authorization at Gateway, observability (Prometheus + Grafana), and resilience (Resilience4j)
- Consider schema per service DBs and proper migrations management
