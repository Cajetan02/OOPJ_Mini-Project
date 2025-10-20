package com.sportsmanager.controller;

import com.sportsmanager.model.Sport;
import com.sportsmanager.model.Team;
import com.sportsmanager.model.Match;
import com.sportsmanager.dao.SportDAO;
import com.sportsmanager.dao.TeamDAO;
import com.sportsmanager.dao.MatchDAO;
import com.sportsmanager.dao.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.time.LocalDate;
import java.util.Optional;

public class MainController {

    // Sports Tab
    @FXML private TableView<Sport> sportsTable;
    @FXML private TableColumn<Sport, Integer> sportIdCol;
    @FXML private TableColumn<Sport, String> sportNameCol;
    @FXML private TableColumn<Sport, String> scoringTypeCol;
    @FXML private TextField sportNameField;
    @FXML private ComboBox<String> scoringTypeCombo;
    @FXML private TextField sportSearchField;

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
    @FXML private TextField teamSearchField;

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
    @FXML private Label statsLabel;

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
    @FXML private TextField matchSearchField;

    private SportDAO sportDAO = new SportDAO();
    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();

    private Sport selectedSport = null;
    private FilteredList<Sport> filteredSports;
    private FilteredList<Team> filteredTeams;
    private FilteredList<Match> filteredMatches;

    @FXML
    public void initialize() {
        setupSportsTable();
        setupTeamsTable();
        setupStandingsTable();
        setupMatchesTable();

        setupSearchFilters();

        // Populate scoring type combo
        scoringTypeCombo.setItems(FXCollections.observableArrayList(
                "GOALS", "POINTS", "RUNS", "SETS"
        ));

        loadSports();
    }

    // ============================================
    // SEARCH & FILTER SETUP
    // ============================================

    private void setupSearchFilters() {
        // Sport search
        if (sportSearchField != null) {
            sportSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (filteredSports != null) {
                    filteredSports.setPredicate(sport -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        return sport.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                sport.getScoringType().toLowerCase().contains(newValue.toLowerCase());
                    });
                }
            });
        }

        // Team search
        if (teamSearchField != null) {
            teamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (filteredTeams != null) {
                    filteredTeams.setPredicate(team -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        return team.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                team.getCoach().toLowerCase().contains(newValue.toLowerCase());
                    });
                }
            });
        }

        // Match search
        if (matchSearchField != null) {
            matchSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (filteredMatches != null) {
                    filteredMatches.setPredicate(match -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        return match.getTeam1Name().toLowerCase().contains(newValue.toLowerCase()) ||
                                match.getTeam2Name().toLowerCase().contains(newValue.toLowerCase()) ||
                                match.getLocation().toLowerCase().contains(newValue.toLowerCase());
                    });
                }
            });
        }
    }

    // ============================================
    // SPORTS TAB METHODS
    // ============================================

    private void setupSportsTable() {
        sportIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoringTypeCol.setCellValueFactory(new PropertyValueFactory<>("scoringType"));

        sportsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSport = newVal;
                updateSportCombos();
            }
        });

        // Enable row selection with double-click to edit
        sportsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Sport selected = sportsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    sportNameField.setText(selected.getName());
                    scoringTypeCombo.setValue(selected.getScoringType());
                }
            }
        });
    }

    @FXML
    private void handleAddSport() {
        try {
            String name = sportNameField.getText().trim();
            String scoringType = scoringTypeCombo.getValue();

            // Validation
            if (name.isEmpty()) {
                showWarning("Validation Error", "Sport name cannot be empty!");
                sportNameField.requestFocus();
                return;
            }

            if (name.length() < 3) {
                showWarning("Validation Error", "Sport name must be at least 3 characters!");
                sportNameField.requestFocus();
                return;
            }

            if (scoringType == null) {
                showWarning("Validation Error", "Please select a scoring type!");
                scoringTypeCombo.requestFocus();
                return;
            }

            // Check for duplicates
            for (Sport sport : sportsTable.getItems()) {
                if (sport.getName().equalsIgnoreCase(name)) {
                    showWarning("Duplicate Entry", "A sport with this name already exists!");
                    return;
                }
            }

            Sport sport = new Sport(name, scoringType);
            sportDAO.addSport(sport);

            DatabaseConnection.logAction("ADD", "sports", 0, "Added sport: " + name);

            loadSports();
            clearSportFields();
            showSuccess("Success", "Sport '" + name + "' added successfully!");
        } catch (Exception e) {
            showError("Error", "Failed to add sport: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSport() {
        Sport selected = sportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selection Required", "Please select a sport to delete!");
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Sport: " + selected.getName());
        confirm.setContentText("This will delete all associated teams and matches!\nAre you sure?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                sportDAO.deleteSport(selected.getId());
                DatabaseConnection.logAction("DELETE", "sports", selected.getId(), "Deleted sport: " + selected.getName());
                loadSports();
                showSuccess("Success", "Sport deleted successfully!");
            } catch (Exception e) {
                showError("Error", "Failed to delete sport: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadSports() {
        try {
            ObservableList<Sport> sports = sportDAO.getAllSports();
            filteredSports = new FilteredList<>(sports, p -> true);
            SortedList<Sport> sortedSports = new SortedList<>(filteredSports);
            sortedSports.comparatorProperty().bind(sportsTable.comparatorProperty());
            sportsTable.setItems(sortedSports);
            updateSportCombos();
        } catch (Exception e) {
            showError("Error", "Failed to load sports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSportCombos() {
        try {
            ObservableList<Sport> sports = sportDAO.getAllSports();
            teamSportCombo.setItems(sports);
            matchSportCombo.setItems(sports);
            standingsSportCombo.setItems(sports);

            if (selectedSport == null && !sports.isEmpty()) {
                selectedSport = sports.get(0);
                teamSportCombo.setValue(selectedSport);
                matchSportCombo.setValue(selectedSport);
                standingsSportCombo.setValue(selectedSport);

                loadTeamsForSport(selectedSport.getId());
                loadTeamsForMatchCombos(selectedSport.getId());
                loadMatchesForSport(selectedSport.getId());
                loadStandings(selectedSport.getId());
            }
        } catch (Exception e) {
            showError("Error", "Failed to update sport filters: " + e.getMessage());
            e.printStackTrace();
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

        goalDiffCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGoalDifference()).asObject()
        );

        // Double-click to edit
        teamsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Team selected = teamsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    teamNameField.setText(selected.getName());
                    coachField.setText(selected.getCoach());
                }
            }
        });
    }

    @FXML
    private void handleAddTeam() {
        Sport sport = teamSportCombo.getValue();
        if (sport == null) {
            showWarning("Selection Required", "Please select a sport first!");
            teamSportCombo.requestFocus();
            return;
        }

        try {
            String name = teamNameField.getText().trim();
            String coach = coachField.getText().trim();

            // Validation
            if (name.isEmpty() || coach.isEmpty()) {
                showWarning("Validation Error", "Please fill all fields!");
                return;
            }

            if (name.length() < 2) {
                showWarning("Validation Error", "Team name must be at least 2 characters!");
                return;
            }

            if (coach.length() < 3) {
                showWarning("Validation Error", "Coach name must be at least 3 characters!");
                return;
            }

            // Check duplicates
            for (Team team : teamsTable.getItems()) {
                if (team.getName().equalsIgnoreCase(name)) {
                    showWarning("Duplicate Entry", "A team with this name already exists in this sport!");
                    return;
                }
            }

            Team team = new Team(name, coach, sport.getId());
            teamDAO.addTeam(team);
            DatabaseConnection.logAction("ADD", "teams", 0, "Added team: " + name);

            loadTeamsForSport(sport.getId());
            loadTeamsForMatchCombos(sport.getId());
            clearTeamFields();
            showSuccess("Success", "Team '" + name + "' added successfully!");
        } catch (Exception e) {
            showError("Error", "Failed to add team: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTeam() {
        Team selected = teamsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selection Required", "Please select a team to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Team: " + selected.getName());
        confirm.setContentText("Are you sure you want to delete this team?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                teamDAO.deleteTeam(selected.getId());
                DatabaseConnection.logAction("DELETE", "teams", selected.getId(), "Deleted team: " + selected.getName());

                Sport sport = teamSportCombo.getValue();
                if (sport != null) {
                    loadTeamsForSport(sport.getId());
                    loadTeamsForMatchCombos(sport.getId());
                }
                showSuccess("Success", "Team deleted successfully!");
            } catch (Exception e) {
                showError("Error", "Failed to delete team: " + e.getMessage());
                e.printStackTrace();
            }
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
            filteredTeams = new FilteredList<>(teams, p -> true);
            SortedList<Team> sortedTeams = new SortedList<>(filteredTeams);
            sortedTeams.comparatorProperty().bind(teamsTable.comparatorProperty());
            teamsTable.setItems(sortedTeams);
        } catch (Exception e) {
            showError("Error", "Failed to load teams: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTeamsForMatchCombos(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getTeamsBySport(sportId);
            team1Combo.setItems(teams);
            team2Combo.setItems(teams);
        } catch (Exception e) {
            showError("Error", "Failed to load teams for matches: " + e.getMessage());
            e.printStackTrace();
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
        positionCol.setCellValueFactory(cellData -> {
            int index = standingsTable.getItems().indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(index).asObject();
        });

        standTeamNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

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

        // Highlight top 3 positions
        standingsTable.setRowFactory(tv -> new TableRow<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setStyle("");
                } else {
                    int index = getIndex() + 1;
                    if (index == 1) {
                        setStyle("-fx-background-color: rgba(255, 215, 0, 0.2);"); // Gold
                    } else if (index == 2) {
                        setStyle("-fx-background-color: rgba(192, 192, 192, 0.2);"); // Silver
                    } else if (index == 3) {
                        setStyle("-fx-background-color: rgba(205, 127, 50, 0.2);"); // Bronze
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    @FXML
    private void handleLoadStandings() {
        Sport sport = standingsSportCombo.getValue();
        if (sport != null) {
            loadStandings(sport.getId());
        } else {
            showWarning("Selection Required", "Please select a sport first!");
        }
    }

    private void loadStandings(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getStandingsBySport(sportId);
            standingsTable.setItems(teams);

            // Update statistics label
            if (statsLabel != null && !teams.isEmpty()) {
                int totalTeams = teams.size();
                int totalMatches = teams.stream().mapToInt(t -> t.getWins() + t.getDraws() + t.getLosses()).sum() / 2;
                int totalGoals = teams.stream().mapToInt(Team::getGoalsFor).sum();

                statsLabel.setText(String.format(
                        "📊 Stats: %d Teams | %d Matches Played | %d Total Goals",
                        totalTeams, totalMatches, totalGoals
                ));
            }
        } catch (Exception e) {
            showError("Error", "Failed to load standings: " + e.getMessage());
            e.printStackTrace();
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

        // Double-click to load scores for editing
        matchesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Match selected = matchesTable.getSelectionModel().getSelectedItem();
                if (selected != null && selected.getStatus().equals("Completed")) {
                    score1Field.setText(String.valueOf(selected.getTeam1Score()));
                    score2Field.setText(String.valueOf(selected.getTeam2Score()));
                }
            }
        });
    }

    @FXML
    private void handleAddMatch() {
        Sport sport = matchSportCombo.getValue();
        if (sport == null) {
            showWarning("Selection Required", "Please select a sport first!");
            matchSportCombo.requestFocus();
            return;
        }

        try {
            Team team1 = team1Combo.getValue();
            Team team2 = team2Combo.getValue();
            LocalDate date = matchDatePicker.getValue();
            String location = locationField.getText().trim();

            // Validation
            if (team1 == null || team2 == null || date == null || location.isEmpty()) {
                showWarning("Validation Error", "Please fill all fields!");
                return;
            }

            if (team1.getId() == team2.getId()) {
                showWarning("Invalid Selection", "Teams must be different!");
                team2Combo.requestFocus();
                return;
            }

            if (date.isBefore(LocalDate.now())) {
                showWarning("Invalid Date", "Match date cannot be in the past!");
                matchDatePicker.requestFocus();
                return;
            }

            if (location.length() < 3) {
                showWarning("Validation Error", "Location must be at least 3 characters!");
                locationField.requestFocus();
                return;
            }

            Match match = new Match(team1.getName(), team2.getName(), date, location, sport.getId());
            matchDAO.addMatch(match);
            DatabaseConnection.logAction("ADD", "matches", 0, "Scheduled: " + team1.getName() + " vs " + team2.getName());

            loadMatchesForSport(sport.getId());
            clearMatchFields();
            showSuccess("Success", "Match scheduled successfully!");
        } catch (Exception e) {
            showError("Error", "Failed to add match: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateResult() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selection Required", "Please select a match from the table!");
            return;
        }

        try {
            String score1Text = score1Field.getText().trim();
            String score2Text = score2Field.getText().trim();

            if (score1Text.isEmpty() || score2Text.isEmpty()) {
                showWarning("Validation Error", "Please enter scores for both teams!");
                score1Field.requestFocus();
                return;
            }

            int score1 = Integer.parseInt(score1Text);
            int score2 = Integer.parseInt(score2Text);

            if (score1 < 0 || score2 < 0) {
                showWarning("Invalid Score", "Scores cannot be negative!");
                return;
            }

            // Confirmation dialog
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Result");
            confirm.setHeaderText("Update Match Result");
            confirm.setContentText(String.format(
                    "%s %d - %d %s\n\nThis will update team standings. Continue?",
                    selected.getTeam1Name(), score1, score2, selected.getTeam2Name()
            ));

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }

            matchDAO.updateMatchResult(selected.getId(), score1, score2);

            // Update team records and points
            if (score1 > score2) {
                teamDAO.updateMatchResult(selected.getTeam1Name(), true, false, score1, score2);
                teamDAO.updateMatchResult(selected.getTeam2Name(), false, false, score2, score1);
            } else if (score2 > score1) {
                teamDAO.updateMatchResult(selected.getTeam2Name(), true, false, score2, score1);
                teamDAO.updateMatchResult(selected.getTeam1Name(), false, false, score1, score2);
            } else {
                teamDAO.updateMatchResult(selected.getTeam1Name(), false, true, score1, score2);
                teamDAO.updateMatchResult(selected.getTeam2Name(), false, true, score2, score1);
            }

            DatabaseConnection.logAction("UPDATE", "matches", selected.getId(),
                    String.format("Result: %s %d-%d %s", selected.getTeam1Name(), score1, score2, selected.getTeam2Name()));

            Sport sport = matchSportCombo.getValue();
            if (sport != null) {
                loadMatchesForSport(sport.getId());
                loadTeamsForSport(sport.getId());
                loadStandings(sport.getId());
            }

            clearMatchFields();
            showSuccess("Success", "Match result updated!\n\nStandings have been refreshed.");
        } catch (NumberFormatException e) {
            showWarning("Invalid Input", "Please enter valid numeric scores!");
            score1Field.requestFocus();
        } catch (Exception e) {
            showError("Error", "Failed to update result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteMatch() {
        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Selection Required", "Please select a match to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Match");
        confirm.setContentText(String.format(
                "%s vs %s\nDate: %s\nLocation: %s\n\nAre you sure?",
                selected.getTeam1Name(), selected.getTeam2Name(),
                selected.getMatchDate(), selected.getLocation()
        ));

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                matchDAO.deleteMatch(selected.getId());
                DatabaseConnection.logAction("DELETE", "matches", selected.getId(),
                        "Deleted: " + selected.getTeam1Name() + " vs " + selected.getTeam2Name());

                Sport sport = matchSportCombo.getValue();
                if (sport != null) {
                    loadMatchesForSport(sport.getId());
                }
                showSuccess("Success", "Match deleted successfully!");
            } catch (Exception e) {
                showError("Error", "Failed to delete match: " + e.getMessage());
                e.printStackTrace();
            }
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
            filteredMatches = new FilteredList<>(matches, p -> true);
            SortedList<Match> sortedMatches = new SortedList<>(filteredMatches);
            sortedMatches.comparatorProperty().bind(matchesTable.comparatorProperty());
            matchesTable.setItems(sortedMatches);
        } catch (Exception e) {
            showError("Error", "Failed to load matches: " + e.getMessage());
            e.printStackTrace();
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

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}