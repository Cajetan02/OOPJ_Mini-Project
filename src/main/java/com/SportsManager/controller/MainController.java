package com.sportsmanager.controller;

import com.sportsmanager.model.Team;
import com.sportsmanager.model.Match;
import com.sportsmanager.dao.TeamDAO;
import com.sportsmanager.dao.MatchDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class MainController {
    // Teams Tab
    @FXML private TableView<Team> teamsTable;
    @FXML private TableColumn<Team, Integer> teamIdCol;
    @FXML private TableColumn<Team, String> teamNameCol;
    @FXML private TableColumn<Team, String> coachCol;
    @FXML private TableColumn<Team, Integer> winsCol;
    @FXML private TableColumn<Team, Integer> lossesCol;
    @FXML private TextField teamNameField;
    @FXML private TextField coachField;

    // Matches Tab
    @FXML private TableView<Match> matchesTable;
    @FXML private TableColumn<Match, Integer> matchIdCol;
    @FXML private TableColumn<Match, String> team1Col;
    @FXML private TableColumn<Match, String> team2Col;
    @FXML private TableColumn<Match, LocalDate> dateCol;
    @FXML private TableColumn<Match, String> locationCol;
    @FXML private TableColumn<Match, Integer> score1Col;
    @FXML private TableColumn<Match, Integer> score2Col;
    @FXML private TableColumn<Match, String> statusCol;
    @FXML private ComboBox<Team> team1Combo;
    @FXML private ComboBox<Team> team2Combo;
    @FXML private DatePicker matchDatePicker;
    @FXML private TextField locationField;
    @FXML private TextField score1Field;
    @FXML private TextField score2Field;

    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();

    @FXML
    public void initialize() {
        setupTeamsTable();
        setupMatchesTable();
        loadTeams();
        loadMatches();
    }

    private void setupTeamsTable() {
        teamIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        teamNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
    }

    private void setupMatchesTable() {
        matchIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1Name"));
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2Name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        score1Col.setCellValueFactory(new PropertyValueFactory<>("team1Score"));
        score2Col.setCellValueFactory(new PropertyValueFactory<>("team2Score"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void handleAddTeam() {
        try {
            String name = teamNameField.getText().trim();
            String coach = coachField.getText().trim();
            
            if (name.isEmpty() || coach.isEmpty()) {
                showAlert("Error", "Please fill all fields!");
                return;
            }

            Team team = new Team(name, coach);
            teamDAO.addTeam(team);
            loadTeams();
            clearTeamFields();
            showAlert("Success", "Team added successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add team: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTeam() {
        Team selected = teamsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a team to delete!");
            return;
        }

        try {
            teamDAO.deleteTeam(selected.getId());
            loadTeams();
            showAlert("Success", "Team deleted successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete team: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddMatch() {
        try {
            Team team1 = team1Combo.getValue();
            Team team2 = team2Combo.getValue();
            LocalDate date = matchDatePicker.getValue();
            String location = locationField.getText().trim();

            if (team1 == null || team2 == null || date == null || location.isEmpty()) {
                showAlert("Error", "Please fill all fields!");
                return;
            }

            if (team1.getId() == team2.getId()) {
                showAlert("Error", "Teams must be different!");
                return;
            }

            Match match = new Match(team1.getName(), team2.getName(), date, location);
            matchDAO.addMatch(match);
            loadMatches();
            clearMatchFields();
            showAlert("Success", "Match scheduled successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add match: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateResult() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a match!");
            return;
        }

        try {
            int score1 = Integer.parseInt(score1Field.getText());
            int score2 = Integer.parseInt(score2Field.getText());

            matchDAO.updateMatchResult(selected.getId(), score1, score2);
            
            // Update team records
            if (score1 > score2) {
                teamDAO.updateTeamRecord(selected.getTeam1Name(), true);
                teamDAO.updateTeamRecord(selected.getTeam2Name(), false);
            } else if (score2 > score1) {
                teamDAO.updateTeamRecord(selected.getTeam2Name(), true);
                teamDAO.updateTeamRecord(selected.getTeam1Name(), false);
            }

            loadMatches();
            loadTeams();
            showAlert("Success", "Match result updated!");
        } catch (Exception e) {
            showAlert("Error", "Failed to update result: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteMatch() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a match to delete!");
            return;
        }

        try {
            matchDAO.deleteMatch(selected.getId());
            loadMatches();
            showAlert("Success", "Match deleted successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete match: " + e.getMessage());
        }
    }

    private void loadTeams() {
        try {
            ObservableList<Team> teams = teamDAO.getAllTeams();
            teamsTable.setItems(teams);
            team1Combo.setItems(teams);
            team2Combo.setItems(teams);
        } catch (Exception e) {
            showAlert("Error", "Failed to load teams: " + e.getMessage());
        }
    }

    private void loadMatches() {
        try {
            matchesTable.setItems(matchDAO.getAllMatches());
        } catch (Exception e) {
            showAlert("Error", "Failed to load matches: " + e.getMessage());
        }
    }

    private void clearTeamFields() {
        teamNameField.clear();
        coachField.clear();
    }

    private void clearMatchFields() {
        team1Combo.setValue(null);
        team2Combo.setValue(null);
        matchDatePicker.setValue(null);
        locationField.clear();
        score1Field.clear();
        score2Field.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}