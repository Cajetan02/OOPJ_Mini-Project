package com.sportsmanager.dao;

import com.sportsmanager.model.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class TeamDAO {
    
    public void addTeam(Team team) throws SQLException {
        String sql = "INSERT INTO teams (name, coach, wins, losses) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCoach());
            pstmt.setInt(3, team.getWins());
            pstmt.setInt(4, team.getLosses());
            pstmt.executeUpdate();
        }
    }

    public ObservableList<Team> getAllTeams() throws SQLException {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        String sql = "SELECT * FROM teams";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Team team = new Team(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("coach"),
                    rs.getInt("wins"),
                    rs.getInt("losses")
                );
                teams.add(team);
            }
        }
        return teams;
    }

    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE teams SET name = ?, coach = ?, wins = ?, losses = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getCoach());
            pstmt.setInt(3, team.getWins());
            pstmt.setInt(4, team.getLosses());
            pstmt.setInt(5, team.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteTeam(int teamId) throws SQLException {
        String sql = "DELETE FROM teams WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);
            pstmt.executeUpdate();
        }
    }

    public void updateTeamRecord(String teamName, boolean isWin) throws SQLException {
        String sql = isWin ? 
            "UPDATE teams SET wins = wins + 1 WHERE name = ?" :
            "UPDATE teams SET losses = losses + 1 WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teamName);
            pstmt.executeUpdate();
        }
    }
}