package com.sportsmanager.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.UUID;

/**
 * Manage user access and roles within tournaments
 */
public class TournamentUserDAO {

    /**
     * Add user to tournament with specified role
     */
    public void addUserToTournament(int tournamentId, String userId, String role) throws SQLException {
        String sql = "INSERT INTO tournament_members (tournament_id, user_id, role) VALUES (?, ?::uuid, ?) " +
                "ON CONFLICT (tournament_id, user_id) DO UPDATE SET role = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, userId);
            pstmt.setString(3, role);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
        }
    }

    /**
     * Remove user from tournament
     */
    public void removeUserFromTournament(int tournamentId, String userId) throws SQLException {
        String sql = "DELETE FROM tournament_members WHERE tournament_id = ? AND user_id = ?::uuid";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get user role in tournament
     */
    public String getUserRoleInTournament(int tournamentId, String userId) throws SQLException {
        String sql = "SELECT role FROM tournament_members WHERE tournament_id = ? AND user_id = ?::uuid";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }
        }

        return null;
    }

    /**
     * Check if user has access to tournament
     */
    public boolean hasAccessToTournament(int tournamentId, String userId) throws SQLException {
        return getUserRoleInTournament(tournamentId, userId) != null;
    }

    /**
     * Update user role in tournament
     */
    public void updateUserRole(int tournamentId, String userId, String newRole) throws SQLException {
        String sql = "UPDATE tournament_members SET role = ? WHERE tournament_id = ? AND user_id = ?::uuid";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newRole);
            pstmt.setInt(2, tournamentId);
            pstmt.setString(3, userId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get all members of tournament
     */
    public ObservableList<TournamentMember> getTournamentMembers(int tournamentId) throws SQLException {
        ObservableList<TournamentMember> members = FXCollections.observableArrayList();
        String sql = "SELECT tm.user_id, p.username, p.full_name, tm.role, tm.joined_at " +
                "FROM tournament_members tm " +
                "JOIN profiles p ON tm.user_id = p.id " +
                "WHERE tm.tournament_id = ? " +
                "ORDER BY tm.joined_at DESC";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TournamentMember member = new TournamentMember(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
                members.add(member);
            }
        }

        return members;
    }

    /**
     * Generate unique share code for tournament
     */
    public String generateShareCode(int tournamentId) throws SQLException {
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sql = "UPDATE tournaments SET share_code = ? WHERE id = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            pstmt.setInt(2, tournamentId);
            pstmt.executeUpdate();
        }

        return code;
    }

    /**
     * Get tournament ID from share code
     */
    public Integer getTournamentByShareCode(String shareCode) throws SQLException {
        String sql = "SELECT id FROM tournaments WHERE share_code = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shareCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        return null;
    }

    /**
     * Check if user is admin of tournament
     */
    public boolean isAdmin(int tournamentId, String userId) throws SQLException {
        String role = getUserRoleInTournament(tournamentId, userId);
        return "admin".equals(role);
    }

    /**
     * Check if user is manager of tournament
     */
    public boolean isManager(int tournamentId, String userId) throws SQLException {
        String role = getUserRoleInTournament(tournamentId, userId);
        return "admin".equals(role) || "manager".equals(role);
    }

    /**
     * Tournament member inner class
     */
    public static class TournamentMember {
        public String userId;
        public String username;
        public String fullName;
        public String role;

        public TournamentMember(String userId, String username, String fullName, String role) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
        }

        @Override
        public String toString() {
            return fullName + " (" + role + ")";
        }
    }
}