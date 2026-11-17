Real-Time Chat Application

A lightweight real-time chat application built with Java, Spring Boot, Spring WebSocket, STOMP, and Spring Data JPA.

The application enables clients to exchange messages in real time using WebSocket communication. Messages are persisted in a relational database, allowing users to retrieve previously sent messages through a REST API.

The implementation separates WebSocket communication, business logic, and data persistence into distinct layers, keeping the application simple, maintainable, and easy to extend.

Features
Real-time messaging: Exchange messages instantly using WebSocket and STOMP.
Message persistence: Store chat messages in a relational database using Spring Data JPA.
Message broadcasting: Broadcast incoming messages to all clients subscribed to the chat topic.
Message history: Retrieve previously persisted messages through a REST endpoint.
SockJS support: Provide a fallback transport mechanism for environments where native WebSocket connections are unavailable.
Layered architecture: Keep request handling, business logic, and persistence responsibilities separated.
Technology Stack
Technology	Purpose
Java	Application development
Spring Boot	Application framework
Spring WebSocket	Real-time communication
STOMP	Messaging protocol
SockJS	WebSocket fallback support
Spring Data JPA	Data persistence
Hibernate	ORM implementation
Relational Database	Message storage
HTML, JavaScript	Chat client
Architecture

The application follows a simple layered architecture.

text
                 Chat Client
                (HTML / JavaScript)
                       |
                       |
                 WebSocket / STOMP
                       |
                       v
             MessageHandlingController
                       |
                       v
                 MessageService
                       |
                       v
                   MessageRepo
                       |
                       v
                  Database

Component Responsibilities

1. MessageHandlingController

Responsible for handling incoming STOMP messages and exposing REST endpoints.

Receives messages through /app/chat.
Delegates message persistence to MessageService.
Publishes the resulting message to /topic/messages.
Exposes an endpoint to retrieve message history.

2. MessageService

Contains the application's business logic.

Converts incoming messages into persistence entities.
Persists messages using the repository.
Retrieves stored messages.
Converts persistence entities into application-level message objects.

3. MessageRepo

Provides database access through Spring Data JPA.

It extends JpaRepository, leveraging Spring Data's built-in persistence and retrieval operations.

4. WebSocketConfig

Configures the messaging infrastructure.

Enables Spring's STOMP message broker support.
Defines /app as the application destination prefix.
Enables the simple message broker for /topic.
Registers /chat as the WebSocket endpoint.
Enables SockJS fallback support.

5. Chat Client

A lightweight HTML and JavaScript interface using SockJS and STOMP.js.

It allows users to connect, disconnect, send messages, and receive messages in real time.

Message Flow
Sending a Message
A client establishes a WebSocket connection using the /chat endpoint.
The client subscribes to /topic/messages.
When a user sends a message, the client publishes it to /app/chat.
Spring routes the message to MessageHandlingController.
The controller delegates persistence to MessageService.
The service creates a MessageEntity and stores it through MessageRepo.
The saved message is converted into an OutputMessage.
Spring broadcasts the output to all subscribers of /topic/messages.
Retrieving Message History

The application also provides a REST endpoint:

http
GET /messages


This endpoint retrieves persisted messages from the database and returns them as a JSON array.

API Endpoints
WebSocket Endpoint
Endpoint	Description
/chat	WebSocket connection endpoint
/app/chat	Destination for sending chat messages
/topic/messages	Topic for receiving broadcast messages
REST Endpoint

Retrieve all messages

http
GET /messages


Example response:

json
[
  {
    "from": "Abhi",
    "text": "Hello everyone!"
  },
  {
    "from": "John",
    "text": "Hi Abhi!"
  }
]

Getting Started
Prerequisites
Java (version compatible with the project configuration)
Maven or Gradle, depending on the build configuration
A relational database configured for the application
Installation

Clone the repository:

bash
git clone <repository-url>


Navigate to the project directory:

bash
cd chatbox


Configure the database connection in application.properties or application.yml.

Example configuration:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatbox
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update


Adjust the database configuration according to your environment.

Run the Application

For Maven:

bash
./mvnw spring-boot:run


Or:

bash
mvn spring-boot:run


For Gradle:

bash
./gradlew bootRun

Access the Application

Once the application starts, open:

text
http://localhost:8080


Connect using a nickname and start exchanging messages with other connected clients.

Project Structure
text
src/
 └── main/
     ├── java/
     │   └── com/abhi/chatbox/
     │       ├── config/
     │       │   └── WebSocketConfig.java
     │       ├── controller/
     │       │   └── MessageHandlingController.java
     │       ├── repo/
     │       │   └── MessageRepo.java
     │       ├── service/
     │       │   └── MessageService.java
     │       └── utils/
     │           ├── Message.java
     │           ├── MessageEntity.java
     │           └── OutputMessage.java
     │
     └── resources/
         ├── static/
         │   ├── index.html
         │   └── js/
         │       ├── sockjs.min.js
         │       └── stomp.min.js
         └── application.properties


The structure above reflects the packages and resources shown in the implementation.

Design Considerations

A few implementation choices are worth highlighting:

Separation of concerns: Controllers handle communication, services handle business operations, and repositories handle persistence.
Transactional boundaries: Message persistence is executed within a write transaction, while read operations use a read-only transaction by default.
Persistence before broadcasting: Messages are saved before being published, ensuring that successfully broadcast messages have first passed through the persistence operation.
Protocol-based communication: STOMP provides a structured messaging model on top of WebSocket transport.
Minimal client implementation: The frontend focuses on demonstrating real-time communication without introducing an unnecessary frontend framework.
Future Enhancements

The application provides a foundation that can be extended with:

User authentication and authorization.
Private messaging and chat rooms.
Pagination for message history.
Message delivery and read receipts.
Message timestamps stored and retrieved from persistence.
Input validation and exception handling.
Integration and WebSocket testing.
Production-ready message broker integration.
Docker-based deployment.
License

Specify the license under which this project is distributed.
