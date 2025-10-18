package com.sportsmanager.dao;

import com.sportsmanager.model.Match;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class MatchDAO {

    public void addMatch(Match match) throws SQLException {
        String sql = "INSERT INTO matches (team1_name, team2_name, match_date, location, team1_score, team2_score, status, sport_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, match.getTeam1Name());
            pstmt.setString(2, match.getTeam2Name());
            pstmt.setDate(3, Date.valueOf(match.getMatchDate()));
            pstmt.setString(4, match.getLocation());
            pstmt.setInt(5, match.getTeam1Score());
            pstmt.setInt(6, match.getTeam2Score());
            pstmt.setString(7, match.getStatus());
            pstmt.setInt(8, match.getSportId());
            pstmt.executeUpdate();
        }
    }

    public ObservableList<Match> getAllMatches() throws SQLException {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        String sql = "SELECT * FROM matches ORDER BY match_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Match match = createMatchFromResultSet(rs);
                matches.add(match);
            }
        }
        return matches;
    }

    public ObservableList<Match> getMatchesBySport(int sportId) throws SQLException {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        String sql = "SELECT * FROM matches WHERE sport_id = ? ORDER BY match_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sportId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Match match = createMatchFromResultSet(rs);
                matches.add(match);
            }
        }
        return matches;
    }

    public void updateMatchResult(int matchId, int team1Score, int team2Score) throws SQLException {
        String sql = "UPDATE matches SET team1_score = ?, team2_score = ?, status = 'Completed' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, team1Score);
            pstmt.setInt(2, team2Score);
            pstmt.setInt(3, matchId);
            pstmt.executeUpdate();
        }
    }

    public void deleteMatch(int matchId) throws SQLException {
        String sql = "DELETE FROM matches WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, matchId);
            pstmt.executeUpdate();
        }
    }

    private Match createMatchFromResultSet(ResultSet rs) throws SQLException {
        return new Match(
                rs.getInt("id"),
                rs.getString("team1_name"),
                rs.getString("team2_name"),
                rs.getDate("match_date").toLocalDate(),
                rs.getString("location"),
                rs.getInt("team1_score"),
                rs.getInt("team2_score"),
                rs.getString("status"),
                rs.getInt("sport_id")
        );
    }
}