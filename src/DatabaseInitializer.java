// let's create the Database Structure and some seed data 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        String url = "jdbc:sqlite:book_management.db";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            if (conn != null) {
                System.out.println("Database created or connected successfully!");

                // Create users table
                String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL);";
                stmt.execute(createUsersTable);
                System.out.println("Users table created successfully!");

                // Insert dummy user
                String insertUser = "INSERT INTO users (username, password) VALUES ('Sus', 'Sus123');";
                stmt.execute(insertUser);


                

                System.out.println("Dummy user inserted successfully!");

                // Create books table
                String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "author TEXT NOT NULL, " +
                        "year INTEGER NOT NULL);";
                stmt.execute(createBooksTable);
                System.out.println("Books table created successfully!");

                // Insert dummy books
                String insertBook1 = "INSERT INTO books (title, author, year) VALUES ('How to Win Friends and Influence People', 'Dale Carnegie', 1936);";
                String insertBook2 = "INSERT INTO books (title, author, year) VALUES ('Atomic Habits', 'James Clear', 2018);";
                String insertBook3 = "INSERT INTO books (title, author, year) VALUES ('To Kill a Mockingbird', 'Harper Lee', 1960);";
                stmt.execute(insertBook1);
                stmt.execute(insertBook2);
                stmt.execute(insertBook3);
                System.out.println("Dummy books inserted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}