package com.sportsmanager.dao;

import com.sportsmanager.model.Sport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class SportDAO {

    public void addSport(Sport sport) throws SQLException {
        String sql = "INSERT INTO sports (name, scoring_type) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sport.getName());
            pstmt.setString(2, sport.getScoringType());
            pstmt.executeUpdate();
        }
    }

    public ObservableList<Sport> getAllSports() throws SQLException {
        ObservableList<Sport> sports = FXCollections.observableArrayList();
        String sql = "SELECT * FROM sports ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sport sport = new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("scoring_type")
                );
                sports.add(sport);
            }
        }
        return sports;
    }

    public Sport getSportById(int id) throws SQLException {
        String sql = "SELECT * FROM sports WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Sport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("scoring_type")
                );
            }
        }
        return null;
    }

    public void updateSport(Sport sport) throws SQLException {
        String sql = "UPDATE sports SET name = ?, scoring_type = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sport.getName());
            pstmt.setString(2, sport.getScoringType());
            pstmt.setInt(3, sport.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteSport(int sportId) throws SQLException {
        String sql = "DELETE FROM sports WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sportId);
            pstmt.executeUpdate();
        }
    }
}