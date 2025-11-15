package com.sportsmanager.dao;

import com.sportsmanager.model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Data Access Object for Teams
 * Compatible with both Supabase PostgreSQL and local SQLite
 */
public class TeamDAO {

    public void addTeam(Team team) throws SQLException {
        String sql = "INSERT INTO teams (name, coach, wins, losses, draws, points, goals_for, goals_against, sport_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCoach());
            pstmt.setInt(3, team.getWins());
            pstmt.setInt(4, team.getLosses());
            pstmt.setInt(5, team.getDraws());
            pstmt.setInt(6, team.getPoints());
            pstmt.setInt(7, team.getGoalsFor());
            pstmt.setInt(8, team.getGoalsAgainst());
            pstmt.setInt(9, team.getSportId());
            pstmt.executeUpdate();

            System.out.println("✅ Team added: " + team.getName());
        }
    }

    public ObservableList<Team> getAllTeams() throws SQLException {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        String sql = "SELECT id, name, coach, wins, losses, draws, points, goals_for, goals_against, sport_id " +
                "FROM teams ORDER BY name";

        try (Connection conn = SupabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = createTeamFromResultSet(rs);
                teams.add(team);
            }

            System.out.println("✅ Loaded " + teams.size() + " teams");
        }

        return teams;
    }

    public ObservableList<Team> getTeamsBySport(int sportId) throws SQLException {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        String sql = "SELECT id, name, coach, wins, losses, draws, points, goals_for, goals_against, sport_id " +
                "FROM teams WHERE sport_id = ? ORDER BY name";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sportId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Team team = createTeamFromResultSet(rs);
                teams.add(team);
            }

            System.out.println("✅ Loaded " + teams.size() + " teams for sport ID: " + sportId);
        }

        return teams;
    }

    public ObservableList<Team> getStandingsBySport(int sportId) throws SQLException {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        String sql = "SELECT id, name, coach, wins, losses, draws, points, goals_for, goals_against, sport_id " +
                "FROM teams WHERE sport_id = ? " +
                "ORDER BY points DESC, (goals_for - goals_against) DESC, goals_for DESC";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sportId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Team team = createTeamFromResultSet(rs);
                teams.add(team);
            }

            System.out.println("✅ Loaded standings for " + teams.size() + " teams");
        }

        return teams;
    }

    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE teams SET name = ?, coach = ?, wins = ?, losses = ?, draws = ?, " +
                "points = ?, goals_for = ?, goals_against = ? WHERE id = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCoach());
            pstmt.setInt(3, team.getWins());
            pstmt.setInt(4, team.getLosses());
            pstmt.setInt(5, team.getDraws());
            pstmt.setInt(6, team.getPoints());
            pstmt.setInt(7, team.getGoalsFor());
            pstmt.setInt(8, team.getGoalsAgainst());
            pstmt.setInt(9, team.getId());
            pstmt.executeUpdate();

            System.out.println("✅ Team updated: " + team.getName());
        }
    }

    public void deleteTeam(int teamId) throws SQLException {
        String sql = "DELETE FROM teams WHERE id = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            int rowsAffected = pstmt.executeUpdate();

            System.out.println("✅ Team deleted (rows affected: " + rowsAffected + ")");
        }
    }

    public void updateMatchResult(String teamName, boolean isWin, boolean isDraw,
                                  int goalsFor, int goalsAgainst) throws SQLException {
        String sql = "UPDATE teams SET " +
                "wins = wins + ?, " +
                "draws = draws + ?, " +
                "losses = losses + ?, " +
                "points = points + ?, " +
                "goals_for = goals_for + ?, " +
                "goals_against = goals_against + ? " +
                "WHERE name = ?";

        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int pointsToAdd = 0;
            if (isWin) pointsToAdd = 3;
            else if (isDraw) pointsToAdd = 1;

            pstmt.setInt(1, isWin ? 1 : 0);           // wins
            pstmt.setInt(2, isDraw ? 1 : 0);          // draws
            pstmt.setInt(3, (!isWin && !isDraw) ? 1 : 0); // losses
            pstmt.setInt(4, pointsToAdd);             // points
            pstmt.setInt(5, goalsFor);                // goals_for
            pstmt.setInt(6, goalsAgainst);            // goals_against
            pstmt.setString(7, teamName);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("✅ Match result updated for team: " + teamName + " (rows: " + rowsAffected + ")");
        }
    }

    private Team createTeamFromResultSet(ResultSet rs) throws SQLException {
        return new Team(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("coach"),
                rs.getInt("wins"),
                rs.getInt("losses"),
                rs.getInt("draws"),
                rs.getInt("points"),
                rs.getInt("goals_for"),
                rs.getInt("goals_against"),
                rs.getInt("sport_id")
        );
    }
}