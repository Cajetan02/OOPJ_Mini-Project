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

            // Drop existing tables to ensure clean slate
            System.out.println("Initializing database...");

            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");

            // Drop tables if they exist (in reverse order due to foreign keys)
            stmt.execute("DROP TABLE IF EXISTS matches");
            stmt.execute("DROP TABLE IF EXISTS teams");
            stmt.execute("DROP TABLE IF EXISTS sports");

            // Create Sports table
            String sportsTable = """
                CREATE TABLE sports (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    scoring_type TEXT NOT NULL
                )
            """;
            stmt.execute(sportsTable);
            System.out.println("✓ Sports table created");

            // Create Teams table with sport_id
            String teamsTable = """
                CREATE TABLE teams (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    coach TEXT NOT NULL,
                    wins INTEGER DEFAULT 0,
                    losses INTEGER DEFAULT 0,
                    draws INTEGER DEFAULT 0,
                    points INTEGER DEFAULT 0,
                    goals_for INTEGER DEFAULT 0,
                    goals_against INTEGER DEFAULT 0,
                    sport_id INTEGER NOT NULL,
                    FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE,
                    UNIQUE(name, sport_id)
                )
            """;
            stmt.execute(teamsTable);
            System.out.println("✓ Teams table created");

            // Create Matches table
            String matchesTable = """
                CREATE TABLE matches (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    team1_name TEXT NOT NULL,
                    team2_name TEXT NOT NULL,
                    match_date DATE NOT NULL,
                    location TEXT NOT NULL,
                    team1_score INTEGER DEFAULT 0,
                    team2_score INTEGER DEFAULT 0,
                    status TEXT DEFAULT 'Scheduled',
                    sport_id INTEGER NOT NULL,
                    FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
                )
            """;
            stmt.execute(matchesTable);
            System.out.println("✓ Matches table created");

            // Insert default sports
            stmt.execute("INSERT INTO sports (name, scoring_type) VALUES ('Football', 'GOALS')");
            stmt.execute("INSERT INTO sports (name, scoring_type) VALUES ('Basketball', 'POINTS')");
            stmt.execute("INSERT INTO sports (name, scoring_type) VALUES ('Cricket', 'RUNS')");
            stmt.execute("INSERT INTO sports (name, scoring_type) VALUES ('Tennis', 'SETS')");
            System.out.println("✓ Default sports added!");

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            e.printStackTrace();
        }
    }
}