
# PackEvents - Event Ticket Booking System 🎟️

PackEvents is a **scalable, microservices-based ticket booking platform** developed as a project for a Software Analysis and Design course. It demonstrates a modern, event-driven architecture using **Spring Boot, Docker, and Kafka**. The system features secure authentication, asynchronous event processing, and real-time observability with monitoring tools.

---

## 🏗️ Architecture

PackEvents follows a microservices architecture where services communicate asynchronously via **Apache Kafka**:

- **User Service:** Handles authentication, user registration, login, and JWT token generation.  
- **Booking Service:** Core service managing events, seats, and the entire booking lifecycle.  
- **Payment Service:** Consumes booking events from Kafka to securely process payments.  
- **Notification Service:** Consumes booking and payment events to send user notifications.  

**Key Technologies:** Spring Boot, Apache Kafka, Docker, Prometheus, Grafana, OpenAPI

---

## 📁 Project Structure

This is a **multi-module Maven project**. The relevant directories are:

```
packevents-project/
├── booking-service/        # Manages events, seats, and bookings
├── monitoring/             # Prometheus & monitoring config
├── notification-service/   # Sends notifications based on Kafka events
├── payment-service/        # Processes payments from booking events
├── user-service/           # User authentication & registration
├── docker-compose.yml      # Defines app stack for Docker
└── pom.xml                 # Parent Maven build file
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21 (JDK)  
- Apache Maven  
- Docker & Docker Compose  

### Run Instructions

1. **Build all microservices JARs:**

```
mvn clean install
```

2. **Start the application stack:**

```
docker-compose up -d --build
```
This launches all services, Kafka, Prometheus, and Grafana.

3. **Stop the stack:**
```
docker-compose down
```

---

## 🌐 Accessing Services and Dashboards

| Service / Tool            | URL                                         | Credentials       |
|----------------------------|---------------------------------------------|------------------|
| **Swagger UI (User)**      | http://localhost:8080/swagger-ui.html       | -                |
| **Swagger UI (Booking)**   | http://localhost:8081/swagger-ui.html       | -                |
| **Kafdrop (Kafka UI)**     | http://localhost:9000                       | -                |
| **Prometheus**             | http://localhost:9090                       | -                |
| **Grafana**                | http://localhost:3000                       | `admin` / `admin`|

### Grafana Setup
1. Open Grafana: `http://localhost:3000` and log in.  
2. **Add Data Source:** Configure **Prometheus** with URL `http://prometheus:9090`.  
3. **Import Dashboard:** Navigate to *Dashboards → Import* and use ID `4701`.  
   Link it to your Prometheus data source for JVM metrics visualization.  

---

## 🛠️ Development Workflow

### Making Code Changes
If modifying code (e.g., dependencies or classes):

1. **Rebuild JARs:**
```
mvn clean install
```
2. **Rebuild Docker images and restart:**
```
docker-compose up -d --build
```
   Always include the `--build` flag to ensure newly built code is included in containers.

