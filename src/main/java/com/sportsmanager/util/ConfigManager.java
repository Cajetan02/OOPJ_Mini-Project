package com.sportsmanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Unable to find config.properties");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    // Convenience methods
    public String getSupabaseUrl() {
        return get("supabase.url");
    }

    public String getSupabaseAnonKey() {
        return get("supabase.anon.key");
    }

    public String getDbHost() {
        return get("supabase.db.host");
    }

    public int getDbPort() {
        return getInt("supabase.db.port", 5432);
    }

    public String getDbName() {
        return get("supabase.db.name", "postgres");
    }

    public String getDbUser() {
        return get("supabase.db.user", "postgres");
    }

    public String getDbPassword() {
        return get("supabase.db.password");
    }

    public int getWinPoints() {
        return getInt("points.win", 3);
    }

    public int getDrawPoints() {
        return getInt("points.draw", 1);
    }

    public boolean isLocalDbEnabled() {
        return getBoolean("local.db.enabled", true);
    }

    public boolean isTournamentsEnabled() {
        return getBoolean("feature.tournaments.enabled", true);
    }

    public boolean isPlayersEnabled() {
        return getBoolean("feature.players.enabled", true);
    }
}