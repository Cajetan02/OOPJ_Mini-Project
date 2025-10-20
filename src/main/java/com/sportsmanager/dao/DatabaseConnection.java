package com.sportsmanager.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:sports_manager.db";
    private static final int SCHEMA_VERSION = 1;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");

            // Check if database needs initialization
            if (!isDatabaseInitialized(conn)) {
                System.out.println("🚀 Initializing database for the first time...");
                createTables(stmt);
                insertDefaultData(stmt);
                setSchemaVersion(conn, SCHEMA_VERSION);
                System.out.println("✅ Database initialized successfully!");
            } else {
                System.out.println("✓ Database already exists, loading...");
                // Check for schema updates
                int currentVersion = getSchemaVersion(conn);
                if (currentVersion < SCHEMA_VERSION) {
                    System.out.println("🔄 Updating database schema...");
                    migrateSchema(conn, currentVersion, SCHEMA_VERSION);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed!");
            e.printStackTrace();
        }
    }

    private static boolean isDatabaseInitialized(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, "sports", null);
        return rs.next();
    }

    private static void createTables(Statement stmt) throws SQLException {
        // Create Sports table
        String sportsTable = """
            CREATE TABLE IF NOT EXISTS sports (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                scoring_type TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.execute(sportsTable);
        System.out.println("  ✓ Sports table created");

        // Create Teams table
        String teamsTable = """
            CREATE TABLE IF NOT EXISTS teams (
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
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE,
                UNIQUE(name, sport_id)
            )
        """;
        stmt.execute(teamsTable);
        System.out.println("  ✓ Teams table created");

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
                sport_id INTEGER NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
            )
        """;
        stmt.execute(matchesTable);
        System.out.println("  ✓ Matches table created");

        // Create schema_version table
        String versionTable = """
            CREATE TABLE IF NOT EXISTS schema_version (
                version INTEGER PRIMARY KEY,
                applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.execute(versionTable);
        System.out.println("  ✓ Schema version table created");

        // Create audit log table for tracking changes
        String auditTable = """
            CREATE TABLE IF NOT EXISTS audit_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                action TEXT NOT NULL,
                table_name TEXT NOT NULL,
                record_id INTEGER,
                details TEXT,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        stmt.execute(auditTable);
        System.out.println("  ✓ Audit log table created");
    }

    private static void insertDefaultData(Statement stmt) throws SQLException {
        // Insert default sports
        String[] defaultSports = {
                "INSERT INTO sports (name, scoring_type) VALUES ('Football', 'GOALS')",
                "INSERT INTO sports (name, scoring_type) VALUES ('Basketball', 'POINTS')",
                "INSERT INTO sports (name, scoring_type) VALUES ('Cricket', 'RUNS')",
                "INSERT INTO sports (name, scoring_type) VALUES ('Tennis', 'SETS')",
                "INSERT INTO sports (name, scoring_type) VALUES ('Hockey', 'GOALS')",
                "INSERT INTO sports (name, scoring_type) VALUES ('Volleyball', 'POINTS')"
        };

        for (String sql : defaultSports) {
            stmt.execute(sql);
        }
        System.out.println("  ✓ Default sports added (6 sports)");
    }

    private static int getSchemaVersion(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(version) as version FROM schema_version")) {
            if (rs.next()) {
                return rs.getInt("version");
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    private static void setSchemaVersion(Connection conn, int version) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO schema_version (version) VALUES (?)")) {
            pstmt.setInt(1, version);
            pstmt.executeUpdate();
        }
    }

    private static void migrateSchema(Connection conn, int fromVersion, int toVersion) throws SQLException {
        // Add migration logic here when schema changes
        System.out.println("  ℹ Schema is up to date (v" + fromVersion + ")");
    }

    // Utility method to backup database
    public static void backupDatabase(String backupPath) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("BACKUP TO '" + backupPath + "'");
            System.out.println("✅ Database backed up to: " + backupPath);
        } catch (SQLException e) {
            System.err.println("❌ Backup failed: " + e.getMessage());
        }
    }

    // Utility method to log actions
    public static void logAction(String action, String tableName, int recordId, String details) {
        String sql = "INSERT INTO audit_log (action, table_name, record_id, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, tableName);
            pstmt.setInt(3, recordId);
            pstmt.setString(4, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log action: " + e.getMessage());
        }
    }
}