package com.sportsmanager.dao;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Supabase PostgreSQL Connection Manager
 * Direct connection to Supabase with detailed error messages
 */
public class SupabaseConnection {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_HOST;
    private static String DB_PORT;

    static {
        loadConfiguration();
    }

    /**
     * Load database configuration from config.properties
     */
    private static void loadConfiguration() {
        System.out.println("\n========================================");
        System.out.println("🔧 Loading Supabase Configuration");
        System.out.println("========================================");

        try (InputStream input = SupabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                System.err.println("❌ CRITICAL ERROR: config.properties not found!");
                System.err.println("📁 Expected location: src/main/resources/config.properties");
                System.err.println("💡 Solution: Create config.properties file in src/main/resources/");
                throw new RuntimeException("config.properties not found");
            }

            Properties prop = new Properties();
            prop.load(input);

            DB_HOST = prop.getProperty("supabase.db.host");
            DB_PORT = prop.getProperty("supabase.db.port");
            String dbName = prop.getProperty("supabase.db.name");
            DB_USER = prop.getProperty("supabase.db.user");
            DB_PASSWORD = prop.getProperty("supabase.db.password");

            // Validate configuration
            System.out.println("📋 Configuration values:");
            System.out.println("   Host: " + (DB_HOST != null ? DB_HOST : "❌ NOT SET"));
            System.out.println("   Port: " + (DB_PORT != null ? DB_PORT : "❌ NOT SET"));
            System.out.println("   Database: " + (dbName != null ? dbName : "❌ NOT SET"));
            System.out.println("   User: " + (DB_USER != null ? DB_USER : "❌ NOT SET"));

            if (DB_PASSWORD != null && !DB_PASSWORD.isEmpty()) {
                int passLen = DB_PASSWORD.length();
                String maskedPass = "***" + DB_PASSWORD.substring(Math.max(0, passLen - 4));
                System.out.println("   Password: " + maskedPass + " (length: " + passLen + ")");
            } else {
                System.out.println("   Password: ❌ NOT SET");
            }

            // Check for placeholder values
            boolean hasPlaceholders = false;

            if (DB_HOST == null || DB_HOST.isEmpty()) {
                System.err.println("\n❌ ERROR: Host is not configured!");
                hasPlaceholders = true;
            } else if (DB_HOST.contains("YOUR_PROJECT_ID")) {
                System.err.println("\n❌ ERROR: Host contains placeholder 'YOUR_PROJECT_ID'");
                System.err.println("   Current value: " + DB_HOST);
                hasPlaceholders = true;
            } else if (!DB_HOST.contains("supabase.co") && !DB_HOST.contains("pooler.supabase.com")) {
                System.err.println("\n⚠️  WARNING: Host doesn't look like a Supabase URL");
                System.err.println("   Current value: " + DB_HOST);
                System.err.println("   Expected format: db.xxxxx.supabase.co OR aws-0-region.pooler.supabase.com");
            }

            if (DB_PASSWORD == null || DB_PASSWORD.isEmpty()) {
                System.err.println("\n❌ ERROR: Password is not configured!");
                hasPlaceholders = true;
            } else if (DB_PASSWORD.contains("YOUR_PASSWORD") || DB_PASSWORD.contains("YOUR_DB_PASSWORD")) {
                System.err.println("\n❌ ERROR: Password contains placeholder text");
                hasPlaceholders = true;
            }

            if (DB_USER == null || DB_USER.isEmpty()) {
                System.err.println("\n❌ ERROR: User is not configured!");
                hasPlaceholders = true;
            }

            if (hasPlaceholders) {
                System.err.println("\n📖 HOW TO FIX:");
                System.err.println("1. Go to https://supabase.com/dashboard");
                System.err.println("2. Select your project");
                System.err.println("3. Go to Settings → Database");
                System.err.println("4. Copy the connection details:");
                System.err.println("   - Host (e.g., db.xxxxx.supabase.co)");
                System.err.println("   - Port (5432 or 6543)");
                System.err.println("   - Database (postgres)");
                System.err.println("   - User (postgres)");
                System.err.println("   - Password (the one you set during project creation)");
                System.err.println("5. Update config.properties with these values");
                System.err.println("6. Restart the application");
                throw new RuntimeException("Invalid configuration - please fix config.properties");
            }

            // Build connection URL
            DB_URL = String.format("jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, dbName);

            System.out.println("\n✅ Configuration validated successfully!");
            System.out.println("🔗 JDBC URL: " + DB_URL);
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("\n❌ FATAL ERROR: Failed to load configuration");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Configuration loading failed", e);
        }
    }

    /**
     * Get database connection with detailed error handling
     */
    public static Connection getConnection() throws SQLException {
        System.out.println("🔄 Attempting Supabase connection...");

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Successfully connected to Supabase!");
            return conn;

        } catch (SQLException e) {
            System.err.println("\n❌ DATABASE CONNECTION FAILED");
            System.err.println("========================================");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("========================================");

            // Provide specific troubleshooting based on error
            String errorMsg = e.getMessage().toLowerCase();

            if (errorMsg.contains("connection attempt failed") || errorMsg.contains("connection refused")) {
                System.err.println("\n🔍 DIAGNOSIS: Cannot reach Supabase server");
                System.err.println("\n💡 POSSIBLE CAUSES:");
                System.err.println("1. ❌ Wrong host or port in config.properties");
                System.err.println("   Current: " + DB_HOST + ":" + DB_PORT);
                System.err.println("   Expected format: db.xxxxx.supabase.co:5432");
                System.err.println("   Alternative: aws-0-region.pooler.supabase.com:6543");
                System.err.println("\n2. 🔥 Firewall blocking port " + DB_PORT);
                System.err.println("   Try: Test from another network");
                System.err.println("\n3. ⏸️  Supabase project paused (free tier)");
                System.err.println("   Go to Supabase Dashboard → Check project status");
                System.err.println("\n4. 🌐 Network/VPN issues");
                System.err.println("   Try: Disable VPN and retry");

            } else if (errorMsg.contains("password authentication failed")) {
                System.err.println("\n🔍 DIAGNOSIS: Invalid credentials");
                System.err.println("\n💡 SOLUTIONS:");
                System.err.println("1. ❌ Wrong password in config.properties");
                System.err.println("   Go to: Supabase Dashboard → Settings → Database");
                System.err.println("   Click: 'Reset Database Password'");
                System.err.println("   Copy new password to config.properties");
                System.err.println("\n2. ❌ Wrong username");
                System.err.println("   Default user is: postgres");
                System.err.println("   Your config has: " + DB_USER);

            } else if (errorMsg.contains("database") && errorMsg.contains("does not exist")) {
                System.err.println("\n🔍 DIAGNOSIS: Database not found");
                System.err.println("\n💡 SOLUTION:");
                System.err.println("Database name should be: postgres");
                System.err.println("Check config.properties has: supabase.db.name=postgres");

            } else if (errorMsg.contains("timeout")) {
                System.err.println("\n🔍 DIAGNOSIS: Connection timeout");
                System.err.println("\n💡 SOLUTIONS:");
                System.err.println("1. Slow internet connection");
                System.err.println("2. Supabase server under heavy load");
                System.err.println("3. Try again in a few seconds");

            } else {
                System.err.println("\n🔍 UNKNOWN ERROR TYPE");
                System.err.println("\n💡 GENERAL TROUBLESHOOTING:");
                System.err.println("1. Verify config.properties has correct values");
                System.err.println("2. Test connection in Supabase SQL Editor");
                System.err.println("3. Check Supabase project is active");
                System.err.println("4. Try resetting database password");
            }

            System.err.println("\n📝 QUICK CHECK:");
            System.err.println("Run this in Supabase SQL Editor:");
            System.err.println("   SELECT current_database(), current_user;");
            System.err.println("If that works, the issue is in your config.properties");
            System.err.println("\n========================================\n");

            throw e;
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        System.out.println("\n🧪 Testing Supabase connection...");
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            if (isValid) {
                System.out.println("✅ Connection test PASSED\n");
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("❌ Connection test FAILED\n");
            return false;
        }
    }

    /**
     * Get connection info for display
     */
    public static String getConnectionInfo() {
        return "☁️ Supabase: " + DB_HOST;
    }

    /**
     * Initialize database schema
     */
    public static void initializeDatabase() {
        System.out.println("✓ Using Supabase - Schema managed via SQL Editor");
        System.out.println("💡 Run schema SQL in Supabase Dashboard → SQL Editor");
    }

    /**
     * Close connection safely
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Connection closed");
            } catch (SQLException e) {
                System.err.println("⚠️  Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Execute a test query to verify connection and permissions
     */
    public static void verifyConnection() {
        System.out.println("\n🔍 Verifying Supabase connection and permissions...");

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Test basic connection
            ResultSet rs = stmt.executeQuery("SELECT current_database(), current_user, version()");
            if (rs.next()) {
                System.out.println("✅ Database: " + rs.getString(1));
                System.out.println("✅ User: " + rs.getString(2));
                System.out.println("✅ PostgreSQL: " + rs.getString(3).split(" ")[0] + " " + rs.getString(3).split(" ")[1]);
            }

            // Test table access
            System.out.println("\n🔍 Checking tables...");
            ResultSet tables = stmt.executeQuery(
                    "SELECT table_name FROM information_schema.tables " +
                            "WHERE table_schema = 'public' ORDER BY table_name"
            );

            int tableCount = 0;
            while (tables.next()) {
                tableCount++;
                System.out.println("   ✓ " + tables.getString(1));
            }

            if (tableCount == 0) {
                System.err.println("\n⚠️  WARNING: No tables found!");
                System.err.println("💡 You need to run the schema SQL in Supabase SQL Editor");
            } else {
                System.out.println("\n✅ Found " + tableCount + " tables");
            }

            System.out.println("\n✅ Verification complete!\n");

        } catch (SQLException e) {
            System.err.println("\n❌ Verification failed: " + e.getMessage());
            System.err.println("💡 Make sure you've run the schema SQL in Supabase\n");
        }
    }

    /**
     * Print current configuration (for debugging)
     */
    public static void printConfig() {
        System.out.println("\n📋 Current Configuration:");
        System.out.println("   Host: " + DB_HOST);
        System.out.println("   Port: " + DB_PORT);
        System.out.println("   User: " + DB_USER);
        System.out.println("   JDBC URL: " + DB_URL);
        System.out.println();
    }
}