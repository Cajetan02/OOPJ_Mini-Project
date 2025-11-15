package com.sportsmanager.util;

import com.sportsmanager.model.User;

/**
 * Singleton class to manage user session and permissions
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private String sessionToken;
    private long loginTime;

    private SessionManager() {
        // Private constructor for singleton
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Login user and create session
     */
    public void login(User user, String token) {
        this.currentUser = user;
        this.sessionToken = token;
        this.loginTime = System.currentTimeMillis();
        System.out.println("✅ User logged in: " + user.getUsername() + " [" + user.getRole() + "]");
    }

    /**
     * Logout and clear session
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("👋 User logged out: " + currentUser.getUsername());
        }
        this.currentUser = null;
        this.sessionToken = null;
        this.loginTime = 0;
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null && sessionToken != null;
    }

    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Get login duration in seconds
     */
    public long getSessionDuration() {
        if (loginTime == 0) return 0;
        return (System.currentTimeMillis() - loginTime) / 1000;
    }

    // ============================================
    // ROLE-BASED PERMISSION CHECKS
    // ============================================

    /**
     * Check if user is Admin
     */
    public boolean isAdmin() {
        return isLoggedIn() && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Check if user is Manager
     */
    public boolean isManager() {
        return isLoggedIn() && "manager".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Check if user is Player
     */
    public boolean isPlayer() {
        return isLoggedIn() && "player".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Check if user can modify data (Admin or Manager)
     */
    public boolean canModifyData() {
        return isAdmin() || isManager();
    }

    /**
     * Check if user can delete data (Admin only)
     */
    public boolean canDeleteData() {
        return isAdmin();
    }

    /**
     * Check if user can view all tournaments (Admin only)
     */
    public boolean canViewAllTournaments() {
        return isAdmin();
    }

    /**
     * Check if user can create tournaments (Admin or Manager)
     */
    public boolean canCreateTournaments() {
        return isAdmin() || isManager();
    }

    /**
     * Check if user can manage sports (Admin only)
     */
    public boolean canManageSports() {
        return isAdmin();
    }

    /**
     * Get permission summary
     */
    public String getPermissionSummary() {
        if (!isLoggedIn()) return "Not logged in";

        return String.format("""
                Role: %s
                Can Modify: %s
                Can Delete: %s
                Can Create Tournaments: %s
                Can View All Tournaments: %s
                Can Manage Sports: %s
                """,
                currentUser.getRole().toUpperCase(),
                canModifyData() ? "Yes" : "No",
                canDeleteData() ? "Yes" : "No",
                canCreateTournaments() ? "Yes" : "No",
                canViewAllTournaments() ? "Yes" : "No",
                canManageSports() ? "Yes" : "No"
        );
    }

    /**
     * Validate session timeout (optional - 1 hour default)
     */
    public boolean isSessionValid(long timeoutSeconds) {
        if (!isLoggedIn()) return false;
        return getSessionDuration() < timeoutSeconds;
    }

    /**
     * Refresh session (extend timeout)
     */
    public void refreshSession() {
        if (isLoggedIn()) {
            this.loginTime = System.currentTimeMillis();
            System.out.println("🔄 Session refreshed for: " + currentUser.getUsername());
        }
    }
}