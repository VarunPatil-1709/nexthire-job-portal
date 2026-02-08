# Job Portal Backend – Microservices Architecture

🎥 **Project Demo (Full Flow Walkthrough):**  
👉 [Demo](https://drive.google.com/drive/folders/1RRqg0VzPoCwTNPQHaHuSApRUauhdaafC)

A production-style **microservices-based Job Portal backend** built using **Spring Boot**, designed to demonstrate **real-world backend engineering practices**, including:

- JWT & Refresh Token–based authentication
- Event-driven communication using Kafka
- Outbox pattern and idempotent consumers
- Real-time messaging using WebSockets
- Secure file storage with AWS S3
- Clear service boundaries and clean separation of concerns


---

## 🧱 Technology Stack

- Java 17
- Spring Boot
- Spring Security (JWT + Refresh Token)
- Apache Kafka
- MySQL
- AWS S3 (Resume storage)
- WebSocket (Real-time chat)
- OpenFeign (inter-service communication)
- Maven
- React.js + Tailwind CSS (Frontend)

---

## 🧩 Microservices Overview

| Service                  | Responsibility                                     |
| ------------------------ | -------------------------------------------------- |
| **auth-service**         | Authentication, JWT & refresh token management     |
| **user-service**         | User profile management (candidate & recruiter)    |
| **job-service**          | Job posting, job applications & application status |
| **chat-service**         | Real-time chat using WebSocket                     |
| **notification-service** | Async in-app notifications using Kafka             |

---

## 🔁 System Architecture & Flow

### 1. Authentication & User Creation Flow

- The system begins with the **Auth Service**, which handles user registration and login.
- Authentication is implemented using **JWT and Refresh Tokens**.
- On successful registration, the Auth Service **publishes a Kafka event** containing the `authUserId`.
- The **User Service consumes this event** and creates the initial user profile.
- Users then complete their personal or recruiter-specific details via User Service.
- Recruiter company information is exposed through a **Feign Client** for other services.

> Kafka ensures **loose coupling** between Auth and User services and improves scalability.

---

### 2. Job Posting & Visibility Flow

- Recruiters create job postings using the **Job Service**.
- While creating a job:
  - Job Service fetches **company details** from User Service using Feign.
- Job visibility rules:
  - Recruiters can view **only their own job postings**.
  - Candidates (students) can view **all active job postings**.

---

### 3. Job Application & Resume Upload

- Candidates can apply **only once per job**.
- During application:
  - Resume is uploaded and stored securely in **AWS S3**.
  - Only the recruiter who owns the job can download resumes.
- Application status lifecycle:
  - `APPLIED`
  - `SHORTLISTED`
  - `REJECTED`

---

### 4. Notification System (Event-Driven)

- Important actions (shortlist, reject, chat creation) publish **Kafka events**.
- The **Notification Service consumes these events** and generates in-app notifications.
- This keeps notification handling **fully asynchronous and decoupled**.

---

### 5. Chat Session & Real-Time Communication

- After shortlisting a candidate:
  - Recruiter can create a **Chat Session**.
  - A unique **Chat ID** is generated.
  - Notification is sent to the candidate.
- Chat sessions are categorized as:
  - **Active**
  - **Upcoming**
- Both recruiter and candidate join using the Chat ID.
- Real-time communication is implemented using:
  - **WebSocket**
  - JWT-secured handshake
- Chat logic is isolated in the **Chat Service**.

---

### 6. Reliability & Data Consistency

- The system uses the **Kafka Outbox Pattern** to ensure:
  - Reliable event publishing
  - No message loss
- **Idempotent consumers** are implemented to safely handle retries.
- This makes the system resilient to failures and network issues.

---

## 🌐 Frontend

The frontend is built using **React.js and Tailwind CSS**.

### Features

- Authentication (JWT-based)
- Job browsing & applications
- Recruiter dashboard
- Real-time chat
- Notifications

### Running Frontend Locally

```bash
cd frontend
npm install
npm run dev   # or npm start
```

## ▶️ Running Locally (High Level)

1. Clone the repository
2. Configure MySQL, Kafka, and AWS S3 credentials
3. Run services individually using Maven
4. Use Postman to test APIs
5. Run frontend separately

> Detailed setup instructions can be added later.

---

## 🐳 Kafka & Event-Driven Communication

Apache Kafka is used as the **backbone of asynchronous communication** between microservices in the system.  
It enables **loose coupling, scalability, and reliability** across services.

---

### 🔄 Kafka Usage in the System

Kafka is used for the following event flows:

- **Auth Service → User Service**
  - Publishes `USER_CREATED` events after successful registration.
- **Job Service → Notification Service**
  - Publishes events for job application actions such as:
    - Job applied
    - Job shortlisted
    - Job rejected
- **Chat Service → Notification Service**
  - Publishes events when a chat session is created.

---

### 🧩 Reliability Patterns

To ensure reliability and consistency:

- **Outbox Pattern**
  - Events are first stored in the database and then published to Kafka.
  - Prevents event loss during service failures.
- **Idempotent Consumers**
  - Consumers safely handle duplicate events.
  - Ensures exactly-once logical processing.

---

### 🐳 Running Kafka Locally (Docker)

Kafka can be started locally using Docker Compose.

docker-compose up -d


## 🚀 Future Enhancements

- API Gateway
- Centralized logging & monitoring
- Distributed tracing
- Redis-based rate limiting
- Kubernetes deployment

---

## 👨‍💻 Author

**Varun Patil**
Backend Developer | Java | Spring Boot | Microservices

