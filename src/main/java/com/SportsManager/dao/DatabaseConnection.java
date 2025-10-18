package com.sportsmanager.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:sports_manager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Teams table
            String teamsTable = """
                CREATE TABLE IF NOT EXISTS teams (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    coach TEXT NOT NULL,
                    wins INTEGER DEFAULT 0,
                    losses INTEGER DEFAULT 0
                )
            """;
            stmt.execute(teamsTable);

            // Create Matches table
            String matchesTable = """
                CREATE TABLE IF NOT EXISTS matches (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    team1_name TEXT NOT NULL,
                    team2_name TEXT NOT NULL,
                    match_date DATE NOT NULL,
                    location TEXT NOT NULL,
                    team1_score INTEGER DEFAULT 0,
                    team2_score INTEGER DEFAULT 0,
                    status TEXT DEFAULT 'Scheduled',
                    FOREIGN KEY (team1_name) REFERENCES teams(name),
                    FOREIGN KEY (team2_name) REFERENCES teams(name)
                )
            """;
            stmt.execute(matchesTable);

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}