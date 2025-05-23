# Chat Application with Java, JavaFX, PostgreSQL, and WebSocket

This is a real-time chat application built with Java, JavaFX for the user interface, PostgreSQL for data storage, and WebSocket for messaging. It allows multiple users to communicate in real-time through a simple chat interface.

## Features

- **WebSocket** for real-time messaging between users.
- **JavaFX** for a responsive and interactive user interface.
- **PostgreSQL** for storing user data and messages.
- Ability to send and receive messages in real-time.
- Support for multiple users interacting in the same chat room.

## Technologies Used

- **Java**: The core programming language used for both the client and server.
- **JavaFX**: For building the graphical user interface.
- **WebSocket**: For establishing real-time, bidirectional communication between the client and server.
- **PostgreSQL**: For storing user credentials and chat messages.
- **Maven**: For managing dependencies and building the project.

- 
## Installation and Setup

### Requirements

- Java 8 or higher
- PostgreSQL
- Maven (for dependency management)

1. Clone the repository:
    ```bash
    git clone https://github.com/dikend1/ChatApp.git
    cd ChatApp

2. Clone the repository:
    ```bash
    CREATE DATABASE chatapp;
3. Then, create the necessary tables for users and messages:
   ```bash
   -- Example schema to create tables for users and messages
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        password VARCHAR(255) NOT NULL
    );
    
    CREATE TABLE messages (
        id SERIAL PRIMARY KEY,
        user_id INT REFERENCES users(id),
        message TEXT NOT NULL,
        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
3. Configure Database Connection
Update the database connection settings in Database.java to match your PostgreSQL configuration:
    ```bash
    public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/chatapp";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    }

    

4. Build and Run the Project
Use Maven to clean, install, and run the project:
    ```bash
    mvn clean install
    mvn javafx:run



