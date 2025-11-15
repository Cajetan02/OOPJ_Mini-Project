package com.sportsmanager.dao;

import com.sportsmanager.model.Tournament;
import com.sportsmanager.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * Enhanced Tournament DAO with user-specific access control
 */
public class TournamentDAO {

    /**
     * Add new tournament created by current user
     */
    public void addTournament(Tournament tournament) throws SQLException {
        String sql = "INSERT INTO tournaments (name, sport_id, tournament_type, start_date, " +
                "end_date, status, description, prize_money, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tournament.getName());
            pstmt.setInt(2, tournament.getSportId());
            pstmt.setString(3, tournament.getTournamentType());
            pstmt.setDate(4, Date.valueOf(tournament.getStartDate()));
            pstmt.setDate(5, tournament.getEndDate() != null ? Date.valueOf(tournament.getEndDate()) : null);
            pstmt.setString(6, tournament.getStatus());
            pstmt.setString(7, tournament.getDescription());
            pstmt.setDouble(8, tournament.getPrizeMoney());

            // Set creator as current logged-in user
            String userId = SessionManager.getInstance().getCurrentUser().getId();
            pstmt.setObject(9, java.util.UUID.fromString(userId));

            pstmt.executeUpdate();

            // Get generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                tournament.setId(rs.getInt(1));
            }

            System.out.println("✅ Tournament created: " + tournament.getName() + " by user: " + userId);
        }
    }

    /**
     * Get tournaments created by current user only (Manager/Player view)
     */
    public ObservableList<Tournament> getMyTournaments() throws SQLException {
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList();
        String userId = SessionManager.getInstance().getCurrentUser().getId();

        String sql = "SELECT t.id, t.name, t.sport_id, s.name as sport_name, t.tournament_type, " +
                "t.start_date, t.end_date, t.status, t.description, t.prize_money, " +
                "t.winner_team_id, tm.name as winner_team_name, t.created_by, p.username as creator_name " +
                "FROM tournaments t " +
                "LEFT JOIN sports s ON t.sport_id = s.id " +
                "LEFT JOIN teams tm ON t.winner_team_id = tm.id " +
                "LEFT JOIN profiles p ON t.created_by = p.id " +
                "WHERE t.created_by = ?::uuid " +
                "ORDER BY t.start_date DESC";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Tournament tournament = createTournamentFromResultSet(rs);
                tournaments.add(tournament);
            }

            System.out.println("✅ Loaded " + tournaments.size() + " tournaments for user: " + userId);
        }

        return tournaments;
    }

    /**
     * Get ALL tournaments (Admin view)
     */
    public ObservableList<Tournament> getAllTournaments() throws SQLException {
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList();

        String sql = "SELECT t.id, t.name, t.sport_id, s.name as sport_name, t.tournament_type, " +
                "t.start_date, t.end_date, t.status, t.description, t.prize_money, " +
                "t.winner_team_id, tm.name as winner_team_name, t.created_by, p.username as creator_name " +
                "FROM tournaments t " +
                "LEFT JOIN sports s ON t.sport_id = s.id " +
                "LEFT JOIN teams tm ON t.winner_team_id = tm.id " +
                "LEFT JOIN profiles p ON t.created_by = p.id " +
                "ORDER BY t.start_date DESC";

        try (Connection conn = SupabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tournament tournament = createTournamentFromResultSet(rs);
                tournaments.add(tournament);
            }

            System.out.println("✅ Loaded " + tournaments.size() + " tournaments (Admin view)");
        }

        return tournaments;
    }

    /**
     * Get tournaments by sport (respects user permissions)
     */
    public ObservableList<Tournament> getTournamentsBySport(int sportId) throws SQLException {
        ObservableList<Tournament> tournaments = FXCollections.observableArrayList();
        boolean isAdmin = SessionManager.getInstance().isAdmin();

        String sql = "SELECT t.id, t.name, t.sport_id, s.name as sport_name, t.tournament_type, " +
                "t.start_date, t.end_date, t.status, t.description, t.prize_money, " +
                "t.winner_team_id, tm.name as winner_team_name, t.created_by, p.username as creator_name " +
                "FROM tournaments t " +
                "LEFT JOIN sports s ON t.sport_id = s.id " +
                "LEFT JOIN teams tm ON t.winner_team_id = tm.id " +
                "LEFT JOIN profiles p ON t.created_by = p.id " +
                "WHERE t.sport_id = ? " +
                (isAdmin ? "" : "AND t.created_by = ?::uuid ") +
                "ORDER BY t.start_date DESC";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sportId);
            if (!isAdmin) {
                pstmt.setString(2, SessionManager.getInstance().getCurrentUser().getId());
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Tournament tournament = createTournamentFromResultSet(rs);
                tournaments.add(tournament);
            }

            System.out.println("✅ Loaded " + tournaments.size() + " tournaments for sport ID: " + sportId);
        }

        return tournaments;
    }

    /**
     * Update tournament (only if user owns it or is admin)
     */
    public void updateTournament(Tournament tournament) throws SQLException {
        boolean isAdmin = SessionManager.getInstance().isAdmin();
        String userId = SessionManager.getInstance().getCurrentUser().getId();

        String sql = "UPDATE tournaments SET name = ?, tournament_type = ?, start_date = ?, " +
                "end_date = ?, status = ?, description = ?, prize_money = ?, winner_team_id = ? " +
                "WHERE id = ? " +
                (isAdmin ? "" : "AND created_by = ?::uuid");

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tournament.getName());
            pstmt.setString(2, tournament.getTournamentType());
            pstmt.setDate(3, Date.valueOf(tournament.getStartDate()));
            pstmt.setDate(4, tournament.getEndDate() != null ? Date.valueOf(tournament.getEndDate()) : null);
            pstmt.setString(5, tournament.getStatus());
            pstmt.setString(6, tournament.getDescription());
            pstmt.setDouble(7, tournament.getPrizeMoney());

            if (tournament.getWinnerTeamId() > 0) {
                pstmt.setInt(8, tournament.getWinnerTeamId());
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }

            pstmt.setInt(9, tournament.getId());

            if (!isAdmin) {
                pstmt.setString(10, userId);
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("You don't have permission to update this tournament");
            }

            System.out.println("✅ Tournament updated: " + tournament.getName());
        }
    }

    /**
     * Delete tournament (only if user owns it or is admin)
     */
    public void deleteTournament(int tournamentId) throws SQLException {
        boolean isAdmin = SessionManager.getInstance().isAdmin();
        String userId = SessionManager.getInstance().getCurrentUser().getId();

        String sql = "DELETE FROM tournaments WHERE id = ? " +
                (isAdmin ? "" : "AND created_by = ?::uuid");

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            if (!isAdmin) {
                pstmt.setString(2, userId);
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("You don't have permission to delete this tournament");
            }

            System.out.println("✅ Tournament deleted (rows affected: " + rowsAffected + ")");
        }
    }

    /**
     * Check if user owns tournament
     */
    public boolean isOwner(int tournamentId) throws SQLException {
        String userId = SessionManager.getInstance().getCurrentUser().getId();
        String sql = "SELECT COUNT(*) FROM tournaments WHERE id = ? AND created_by = ?::uuid";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setString(2, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    /**
     * Get tournament statistics
     */
    public String getTournamentStats(int tournamentId) throws SQLException {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM tournament_teams WHERE tournament_id = ?) as team_count, " +
                "(SELECT COUNT(*) FROM matches WHERE tournament_id = ?) as match_count, " +
                "(SELECT COUNT(*) FROM matches WHERE tournament_id = ? AND status = 'completed') as completed_matches";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setInt(2, tournamentId);
            pstmt.setInt(3, tournamentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return String.format("Teams: %d | Matches: %d | Completed: %d",
                        rs.getInt("team_count"),
                        rs.getInt("match_count"),
                        rs.getInt("completed_matches"));
            }
        }

        return "No statistics available";
    }

    /**
     * Add team to tournament
     */
    public void addTeamToTournament(int tournamentId, int teamId) throws SQLException {
        String sql = "INSERT INTO tournament_teams (tournament_id, team_id) VALUES (?, ?) " +
                "ON CONFLICT DO NOTHING";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setInt(2, teamId);
            pstmt.executeUpdate();

            System.out.println("✅ Team added to tournament");
        }
    }

    /**
     * Remove team from tournament
     */
    public void removeTeamFromTournament(int tournamentId, int teamId) throws SQLException {
        String sql = "DELETE FROM tournament_teams WHERE tournament_id = ? AND team_id = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            pstmt.setInt(2, teamId);
            pstmt.executeUpdate();

            System.out.println("✅ Team removed from tournament");
        }
    }

    /**
     * Get teams in tournament
     */
    public ObservableList<Integer> getTournamentTeamIds(int tournamentId) throws SQLException {
        ObservableList<Integer> teamIds = FXCollections.observableArrayList();
        String sql = "SELECT team_id FROM tournament_teams WHERE tournament_id = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tournamentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teamIds.add(rs.getInt("team_id"));
            }
        }

        return teamIds;
    }

    private Tournament createTournamentFromResultSet(ResultSet rs) throws SQLException {
        Tournament tournament = new Tournament(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("sport_id"),
                rs.getString("sport_name"),
                rs.getString("tournament_type"),
                rs.getDate("start_date").toLocalDate(),
                rs.getString("status")
        );

        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            tournament.setEndDate(endDate.toLocalDate());
        }

        tournament.setDescription(rs.getString("description"));
        tournament.setPrizeMoney(rs.getDouble("prize_money"));
        tournament.setWinnerTeamId(rs.getInt("winner_team_id"));

        String winnerName = rs.getString("winner_team_name");
        if (winnerName != null) {
            tournament.setWinnerTeamName(winnerName);
        }

        // Set creator info if available
        try {
            String creatorName = rs.getString("creator_name");
            if (creatorName != null) {
                tournament.setDescription(tournament.getDescription() +
                        "\n[Created by: " + creatorName + "]");
            }
        } catch (SQLException e) {
            // Column might not exist in some queries
        }

        return tournament;
    }
}