package com.sportsmanager.controller;

import com.sportsmanager.model.Sport;
import com.sportsmanager.model.Team;
import com.sportsmanager.model.Match;
import com.sportsmanager.dao.SportDAO;
import com.sportsmanager.dao.TeamDAO;
import com.sportsmanager.dao.MatchDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDate;

public class MainController {

    // Sports Tab
    @FXML private TableView<Sport> sportsTable;
    @FXML private TableColumn<Sport, Integer> sportIdCol;
    @FXML private TableColumn<Sport, String> sportNameCol;
    @FXML private TableColumn<Sport, String> scoringTypeCol;
    @FXML private TextField sportNameField;
    @FXML private ComboBox<String> scoringTypeCombo;

    // Teams Tab
    @FXML private TableView<Team> teamsTable;
    @FXML private TableColumn<Team, Integer> teamIdCol;
    @FXML private TableColumn<Team, String> teamNameCol;
    @FXML private TableColumn<Team, String> coachCol;
    @FXML private TableColumn<Team, Integer> winsCol;
    @FXML private TableColumn<Team, Integer> drawsCol;
    @FXML private TableColumn<Team, Integer> lossesCol;
    @FXML private TableColumn<Team, Integer> pointsCol;
    @FXML private TableColumn<Team, Integer> goalsForCol;
    @FXML private TableColumn<Team, Integer> goalsAgainstCol;
    @FXML private TableColumn<Team, Integer> goalDiffCol;
    @FXML private TextField teamNameField;
    @FXML private TextField coachField;
    @FXML private ComboBox<Sport> teamSportCombo;

    // Standings Tab
    @FXML private TableView<Team> standingsTable;
    @FXML private TableColumn<Team, Integer> positionCol;
    @FXML private TableColumn<Team, String> standTeamNameCol;
    @FXML private TableColumn<Team, Integer> playedCol;
    @FXML private TableColumn<Team, Integer> standWinsCol;
    @FXML private TableColumn<Team, Integer> standDrawsCol;
    @FXML private TableColumn<Team, Integer> standLossesCol;
    @FXML private TableColumn<Team, Integer> standGoalsForCol;
    @FXML private TableColumn<Team, Integer> standGoalsAgainstCol;
    @FXML private TableColumn<Team, Integer> standGoalDiffCol;
    @FXML private TableColumn<Team, Integer> standPointsCol;
    @FXML private ComboBox<Sport> standingsSportCombo;

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
    @FXML private ComboBox<Sport> matchSportCombo;

    private SportDAO sportDAO = new SportDAO();
    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();

    private Sport selectedSport = null;

    @FXML
    public void initialize() {
        setupSportsTable();
        setupTeamsTable();
        setupStandingsTable();
        setupMatchesTable();

        loadSports();
    }

    // ============================================
    // SPORTS TAB METHODS
    // ============================================

    private void setupSportsTable() {
        sportIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoringTypeCol.setCellValueFactory(new PropertyValueFactory<>("scoringType"));

        // Listen for sport selection
        sportsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSport = newVal;
                updateSportCombos();
            }
        });
    }

    @FXML
    private void handleAddSport() {
        try {
            String name = sportNameField.getText().trim();
            String scoringType = scoringTypeCombo.getValue();

            if (name.isEmpty() || scoringType == null) {
                showAlert("Error", "Please fill all fields!");
                return;
            }

            Sport sport = new Sport(name, scoringType);
            sportDAO.addSport(sport);
            loadSports();
            clearSportFields();
            showAlert("Success", "Sport added successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add sport: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteSport() {
        Sport selected = sportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a sport to delete!");
            return;
        }

        try {
            sportDAO.deleteSport(selected.getId());
            loadSports();
            showAlert("Success", "Sport deleted successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete sport: " + e.getMessage());
        }
    }

    private void loadSports() {
        try {
            ObservableList<Sport> sports = sportDAO.getAllSports();
            sportsTable.setItems(sports);
            updateSportCombos();
        } catch (Exception e) {
            showAlert("Error", "Failed to load sports: " + e.getMessage());
        }
    }

    private void updateSportCombos() {
        try {
            ObservableList<Sport> sports = sportDAO.getAllSports();
            teamSportCombo.setItems(sports);
            matchSportCombo.setItems(sports);
            standingsSportCombo.setItems(sports);

            // Auto-select first sport if none selected
            if (selectedSport == null && !sports.isEmpty()) {
                selectedSport = sports.get(0);
                teamSportCombo.setValue(selectedSport);
                matchSportCombo.setValue(selectedSport);
                standingsSportCombo.setValue(selectedSport);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to update sport filters: " + e.getMessage());
        }
    }

    private void clearSportFields() {
        sportNameField.clear();
        scoringTypeCombo.setValue(null);
    }

    // ============================================
    // TEAMS TAB METHODS
    // ============================================

    private void setupTeamsTable() {
        teamIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        teamNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        drawsCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        goalsForCol.setCellValueFactory(new PropertyValueFactory<>("goalsFor"));
        goalsAgainstCol.setCellValueFactory(new PropertyValueFactory<>("goalsAgainst"));

        // Goal Difference calculated column
        goalDiffCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGoalDifference()).asObject()
        );
    }

    @FXML
    private void handleAddTeam() {
        Sport sport = teamSportCombo.getValue();
        if (sport == null) {
            showAlert("Error", "Please select a sport first!");
            return;
        }

        try {
            String name = teamNameField.getText().trim();
            String coach = coachField.getText().trim();

            if (name.isEmpty() || coach.isEmpty()) {
                showAlert("Error", "Please fill all fields!");
                return;
            }

            Team team = new Team(name, coach, sport.getId());
            teamDAO.addTeam(team);
            loadTeamsForSport(sport.getId());
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
            Sport sport = teamSportCombo.getValue();
            if (sport != null) {
                loadTeamsForSport(sport.getId());
            }
            showAlert("Success", "Team deleted successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete team: " + e.getMessage());
        }
    }

    @FXML
    private void handleSportFilterTeams() {
        Sport sport = teamSportCombo.getValue();
        if (sport != null) {
            selectedSport = sport;
            loadTeamsForSport(sport.getId());
            loadTeamsForMatchCombos(sport.getId());
        }
    }

    @FXML
    private void handleShowStandings() {
        Sport sport = teamSportCombo.getValue();
        if (sport != null) {
            standingsSportCombo.setValue(sport);
            loadStandings(sport.getId());
        }
    }

    private void loadTeamsForSport(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getTeamsBySport(sportId);
            teamsTable.setItems(teams);
        } catch (Exception e) {
            showAlert("Error", "Failed to load teams: " + e.getMessage());
        }
    }

    private void loadTeamsForMatchCombos(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getTeamsBySport(sportId);
            team1Combo.setItems(teams);
            team2Combo.setItems(teams);
        } catch (Exception e) {
            showAlert("Error", "Failed to load teams for matches: " + e.getMessage());
        }
    }

    private void clearTeamFields() {
        teamNameField.clear();
        coachField.clear();
    }

    // ============================================
    // STANDINGS TAB METHODS
    // ============================================

    private void setupStandingsTable() {
        // Position column with special formatting
        positionCol.setCellValueFactory(cellData -> {
            int index = standingsTable.getItems().indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(index).asObject();
        });

        standTeamNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Played = Wins + Draws + Losses
        playedCol.setCellValueFactory(cellData -> {
            Team team = cellData.getValue();
            int played = team.getWins() + team.getDraws() + team.getLosses();
            return new javafx.beans.property.SimpleIntegerProperty(played).asObject();
        });

        standWinsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        standDrawsCol.setCellValueFactory(new PropertyValueFactory<>("draws"));
        standLossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        standGoalsForCol.setCellValueFactory(new PropertyValueFactory<>("goalsFor"));
        standGoalsAgainstCol.setCellValueFactory(new PropertyValueFactory<>("goalsAgainst"));

        standGoalDiffCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGoalDifference()).asObject()
        );

        standPointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
    }

    @FXML
    private void handleLoadStandings() {
        Sport sport = standingsSportCombo.getValue();
        if (sport != null) {
            loadStandings(sport.getId());
        }
    }

    private void loadStandings(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getStandingsBySport(sportId);
            standingsTable.setItems(teams);
        } catch (Exception e) {
            showAlert("Error", "Failed to load standings: " + e.getMessage());
        }
    }

    // ============================================
    // MATCHES TAB METHODS
    // ============================================

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
    private void handleAddMatch() {
        Sport sport = matchSportCombo.getValue();
        if (sport == null) {
            showAlert("Error", "Please select a sport first!");
            return;
        }

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

            Match match = new Match(team1.getName(), team2.getName(), date, location, sport.getId());
            matchDAO.addMatch(match);
            loadMatchesForSport(sport.getId());
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

            // Update team records and points
            if (score1 > score2) {
                // Team 1 wins
                teamDAO.updateMatchResult(selected.getTeam1Name(), true, false, score1, score2);
                teamDAO.updateMatchResult(selected.getTeam2Name(), false, false, score2, score1);
            } else if (score2 > score1) {
                // Team 2 wins
                teamDAO.updateMatchResult(selected.getTeam2Name(), true, false, score2, score1);
                teamDAO.updateMatchResult(selected.getTeam1Name(), false, false, score1, score2);
            } else {
                // Draw
                teamDAO.updateMatchResult(selected.getTeam1Name(), false, true, score1, score2);
                teamDAO.updateMatchResult(selected.getTeam2Name(), false, true, score2, score1);
            }

            Sport sport = matchSportCombo.getValue();
            if (sport != null) {
                loadMatchesForSport(sport.getId());
                loadTeamsForSport(sport.getId());
                loadStandings(sport.getId());
            }

            showAlert("Success", "Match result updated! Standings refreshed.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid scores!");
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
            Sport sport = matchSportCombo.getValue();
            if (sport != null) {
                loadMatchesForSport(sport.getId());
            }
            showAlert("Success", "Match deleted successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete match: " + e.getMessage());
        }
    }

    @FXML
    private void handleSportFilterMatches() {
        Sport sport = matchSportCombo.getValue();
        if (sport != null) {
            selectedSport = sport;
            loadMatchesForSport(sport.getId());
            loadTeamsForMatchCombos(sport.getId());
        }
    }

    private void loadMatchesForSport(int sportId) {
        try {
            ObservableList<Match> matches = matchDAO.getMatchesBySport(sportId);
            matchesTable.setItems(matches);
        } catch (Exception e) {
            showAlert("Error", "Failed to load matches: " + e.getMessage());
        }
    }

    private void clearMatchFields() {
        team1Combo.setValue(null);
        team2Combo.setValue(null);
        matchDatePicker.setValue(null);
        locationField.clear();
        score1Field.clear();
        score2Field.clear();
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}