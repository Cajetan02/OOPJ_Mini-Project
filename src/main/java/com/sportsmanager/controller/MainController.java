package com.sportsmanager.controller;

import com.sportsmanager.dao.*;
import com.sportsmanager.model.*;
import com.sportsmanager.util.SessionManager;
import com.sportsmanager.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.sql.Types;
import java.util.Optional;

public class MainController {

    // Header
    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private Label connectionStatusLabel;
    @FXML private Button logoutButton;

    // Sports Tab
    @FXML private TableView<Sport> sportsTable;
    @FXML private TableColumn<Sport, Integer> sportIdCol;
    @FXML private TableColumn<Sport, String> sportNameCol;
    @FXML private TableColumn<Sport, String> scoringTypeCol;
    @FXML private TextField sportNameField;
    @FXML private ComboBox<String> scoringTypeCombo;
    @FXML private TextField sportSearchField;
    @FXML private Button addSportButton;
    @FXML private Button deleteSportButton;

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
    @FXML private Button addTeamButton;
    @FXML private Button deleteTeamButton;

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
    @FXML private Button scheduleMatchButton;
    @FXML private Button updateResultButton;
    @FXML private Button deleteMatchButton;

    // Tournaments Tab
    @FXML private TableView<Tournament> tournamentsTable;
    @FXML private TableColumn<Tournament, Integer> tournamentIdCol;
    @FXML private TableColumn<Tournament, String> tournamentNameCol;
    @FXML private TableColumn<Tournament, String> tournamentSportCol;
    @FXML private TableColumn<Tournament, String> tournamentTypeCol;
    @FXML private TableColumn<Tournament, LocalDate> tournamentStartDateCol;
    @FXML private TableColumn<Tournament, LocalDate> tournamentEndDateCol;
    @FXML private TableColumn<Tournament, String> tournamentStatusCol;
    @FXML private TableColumn<Tournament, String> tournamentWinnerCol;
    @FXML private TextField tournamentNameField;
    @FXML private ComboBox<Sport> tournamentSportCombo;
    @FXML private ComboBox<Sport> tournamentSportCombo2; // For form (different from filter)
    @FXML private ComboBox<String> tournamentTypeCombo;
    @FXML private DatePicker tournamentStartDatePicker;
    @FXML private DatePicker tournamentEndDatePicker;
    @FXML private ComboBox<String> tournamentStatusCombo;
    @FXML private TextField tournamentPrizeField;
    @FXML private TextArea tournamentDescriptionArea;
    @FXML private ComboBox<Team> tournamentWinnerCombo;
    @FXML private TextField tournamentSearchField;
    @FXML private Button addTournamentButton;
    @FXML private Button deleteTournamentButton;
    @FXML private Button updateTournamentButton;
    @FXML private Button manageTeamsButton;
    @FXML private ComboBox<Sport> globalSportCombo;
    @FXML private TextField shareCodeField;
    @FXML private TextField tournamentIdField;

    private SportDAO sportDAO = new SportDAO();
    private TeamDAO teamDAO = new TeamDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private TournamentDAO tournamentDAO = new TournamentDAO();
    private SessionManager session = SessionManager.getInstance();

    private Sport selectedSport = null;
    private Tournament selectedTournament = null;
    private FilteredList<Sport> filteredSports;
    private FilteredList<Team> filteredTeams;
    private FilteredList<Match> filteredMatches;
    private FilteredList<Tournament> filteredTournaments;

    @FXML
    public void initialize() {
        System.out.println("🎮 Initializing MainController...");
        System.out.println("📍 Session status: " + (session.isLoggedIn() ? "Logged In" : "Not Logged In"));

        if (session.isLoggedIn()) {
            System.out.println("👤 Current user: " + session.getCurrentUser().getFullName());
            System.out.println("🔑 User role: " + session.getCurrentUser().getRole());
        }

        try {
            setupHeader();
            setupSportsTable();
            setupTeamsTable();
            setupStandingsTable();
            setupMatchesTable();

            // Only setup tournaments if table exists
            if (tournamentsTable != null) {
                setupTournamentsTable();
            } else {
                System.out.println("⚠️ Tournaments table not found in FXML");
            }

            setupSearchFilters();
            setupRoleBasedAccess();

            if (scoringTypeCombo != null) {
                scoringTypeCombo.setItems(FXCollections.observableArrayList(
                        "GOALS", "POINTS", "RUNS", "SETS"
                ));
            }

            // Only setup tournament combos if they exist
            if (tournamentTypeCombo != null) {
                tournamentTypeCombo.setItems(FXCollections.observableArrayList(
                        "league", "knockout", "group_knockout"
                ));
            }

            if (tournamentStatusCombo != null) {
                tournamentStatusCombo.setItems(FXCollections.observableArrayList(
                        "upcoming", "ongoing", "completed", "cancelled"
                ));
            }

            loadSports();

            // Only load tournaments if table exists
            if (tournamentsTable != null) {
                loadTournaments();
            }

            System.out.println("✅ MainController initialized successfully");

        } catch (Exception e) {
            System.err.println("❌ CRITICAL ERROR in MainController initialization!");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();

            // Show error dialog on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Initialization Error");
                alert.setHeaderText("Failed to load Sports Manager");
                alert.setContentText("Error: " + e.getMessage() + "\n\nPlease check console for details.");
                alert.showAndWait();
            });
        }
    }

    // ============================================
    // HEADER & SESSION MANAGEMENT
    // ============================================

    private void setupHeader() {
        System.out.println("🔧 Setting up header...");

        try {
            if (session.isLoggedIn()) {
                User currentUser = session.getCurrentUser();

                if (welcomeLabel != null) {
                    welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
                } else {
                    System.out.println("⚠️ welcomeLabel is null");
                }

                if (roleLabel != null) {
                    String role = currentUser.getRole().toUpperCase();
                    String emoji = switch (role) {
                        case "ADMIN" -> "👑";
                        case "MANAGER" -> "📋";
                        case "PLAYER" -> "⚽";
                        default -> "👤";
                    };
                    roleLabel.setText(emoji + " " + role);
                } else {
                    System.out.println("⚠️ roleLabel is null");
                }
            } else {
                System.err.println("⚠️ User not logged in during header setup!");
            }

            if (connectionStatusLabel != null) {
                try {
                    connectionStatusLabel.setText(SupabaseConnection.getConnectionInfo());
                } catch (Exception e) {
                    System.err.println("⚠️ Could not get connection info: " + e.getMessage());
                    connectionStatusLabel.setText("❌ Connection Error");
                }
            } else {
                System.out.println("⚠️ connectionStatusLabel is null");
            }

            System.out.println("✅ Header setup complete");

        } catch (Exception e) {
            System.err.println("❌ Error in setupHeader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Confirm Logout");
        confirm.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            session.logout();

            try {
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                        getClass().getResource("/fxml/login.fxml")
                );
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 450, 600);
                scene.getStylesheets().add(
                        getClass().getResource("/css/style.css").toExternalForm()
                );

                com.sportsmanager.controller.LoginController controller = loader.getController();
                controller.setStage(stage);

                stage.setTitle("🏆 Sports Manager Pro - Login");
                stage.setScene(scene);
                stage.centerOnScreen();

            } catch (Exception e) {
                showError("Error", "Failed to return to login: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefreshConnection() {
        connectionStatusLabel.setText("Testing connection...");

        new Thread(() -> {
            boolean connected = SupabaseConnection.testConnection();

            javafx.application.Platform.runLater(() -> {
                if (connected) {
                    connectionStatusLabel.setText("✅ " + SupabaseConnection.getConnectionInfo());
                    showToastSuccess("Connection successful!");
                } else {
                    connectionStatusLabel.setText("❌ Connection Failed");
                    showToastError("Connection failed!");
                }
            });
        }).start();
    }

    // ============================================
    // ROLE-BASED ACCESS CONTROL
    // ============================================

    private void setupRoleBasedAccess() {
        boolean canModify = session.canModifyData();
        boolean canDelete = session.canDeleteData();

        if (addSportButton != null) addSportButton.setDisable(!canDelete);
        if (deleteSportButton != null) deleteSportButton.setDisable(!canDelete);
        if (addTeamButton != null) addTeamButton.setDisable(!canModify);
        if (deleteTeamButton != null) deleteTeamButton.setDisable(!canModify);
        if (scheduleMatchButton != null) scheduleMatchButton.setDisable(!canModify);
        if (updateResultButton != null) updateResultButton.setDisable(!canModify);
        if (deleteMatchButton != null) deleteMatchButton.setDisable(!canDelete);
        if (addTournamentButton != null) addTournamentButton.setDisable(!canModify);
        if (updateTournamentButton != null) updateTournamentButton.setDisable(!canModify);
        if (deleteTournamentButton != null) deleteTournamentButton.setDisable(!canModify);

        if (!canModify) {
            if (sportNameField != null) sportNameField.setDisable(true);
            if (scoringTypeCombo != null) scoringTypeCombo.setDisable(true);
            if (teamNameField != null) teamNameField.setDisable(true);
            if (coachField != null) coachField.setDisable(true);
            if (tournamentNameField != null) tournamentNameField.setDisable(true);
            if (tournamentTypeCombo != null) tournamentTypeCombo.setDisable(true);
        }

        System.out.println("🔒 Role-based access configured for: " + session.getCurrentUser().getRole());
    }

    // ============================================
    // SEARCH & FILTER SETUP
    // ============================================

    private void setupSearchFilters() {
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

        if (tournamentSearchField != null) {
            tournamentSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (filteredTournaments != null) {
                    filteredTournaments.setPredicate(tournament -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        return tournament.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                tournament.getSportName().toLowerCase().contains(newValue.toLowerCase()) ||
                                tournament.getStatus().toLowerCase().contains(newValue.toLowerCase());
                    });
                }
            });
        }
    }

    @FXML
    private void handleGlobalSportChange() {
        Sport sport = globalSportCombo.getValue();  // ✅ FIX: Use globalSportCombo, not matchSportCombo
        if (sport != null) {
            selectedSport = sport;

            // Update all sport-dependent data
            loadTeamsForSport(sport.getId());
            loadTeamsForMatchCombos(sport.getId());
            loadMatchesForSport(sport.getId());
            loadStandings(sport.getId());

            // Sync all other sport combos
            if (teamSportCombo != null) {
                teamSportCombo.setValue(sport);
            }
            if (matchSportCombo != null) {
                matchSportCombo.setValue(sport);
            }
            if (standingsSportCombo != null) {
                standingsSportCombo.setValue(sport);
            }
            if (tournamentSportCombo != null) {
                tournamentSportCombo.setValue(sport);
            }
            if (tournamentSportCombo2 != null) {
                tournamentSportCombo2.setValue(sport);
            }
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

        sportsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && session.isAdmin()) {
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
        if (!session.isAdmin()) {
            showToastWarning("Only administrators can add sports!");
            return;
        }

        try {
            String name = sportNameField.getText().trim();
            String scoringType = scoringTypeCombo.getValue();

            if (name.isEmpty()) {
                showToastWarning("Sport name cannot be empty!");
                sportNameField.requestFocus();
                return;
            }

            if (name.length() < 3) {
                showToastWarning("Sport name must be at least 3 characters!");
                sportNameField.requestFocus();
                return;
            }

            if (scoringType == null) {
                showToastWarning("Please select a scoring type!");
                scoringTypeCombo.requestFocus();
                return;
            }

            for (Sport sport : sportsTable.getItems()) {
                if (sport.getName().equalsIgnoreCase(name)) {
                    showToastWarning("A sport with this name already exists!");
                    return;
                }
            }

            Sport sport = new Sport(name, scoringType);
            sportDAO.addSport(sport);

            loadSports();
            clearSportFields();
            showToastSuccess("Sport '" + name + "' added successfully!");

        } catch (Exception e) {
            showToastError("Failed to add sport: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteSport() {
        if (!session.isAdmin()) {
            showToastWarning("Only administrators can delete sports!");
            return;
        }

        Sport selected = sportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a sport to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Sport: " + selected.getName());
        confirm.setContentText("This will delete all associated teams and matches!\nAre you sure?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                sportDAO.deleteSport(selected.getId());
                loadSports();
                showToastSuccess("Sport deleted successfully!");
            } catch (Exception e) {
                showToastError("Failed to delete sport: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadSports() {
        try {
            System.out.println("📊 Loading sports from database...");
            ObservableList<Sport> sports = sportDAO.getAllSports();
            filteredSports = new FilteredList<>(sports, p -> true);
            SortedList<Sport> sortedSports = new SortedList<>(filteredSports);
            sortedSports.comparatorProperty().bind(sportsTable.comparatorProperty());
            sportsTable.setItems(sortedSports);
            updateSportCombos();
            System.out.println("✅ Loaded " + sports.size() + " sports");
        } catch (Exception e) {
            showToastError("Failed to load sports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSportCombos() {
        try {
            ObservableList<Sport> sports = sportDAO.getAllSports();

            // ✅ FIX: Add globalSportCombo here!
            if (globalSportCombo != null) {
                globalSportCombo.setItems(sports);
            }

            teamSportCombo.setItems(sports);
            matchSportCombo.setItems(sports);
            standingsSportCombo.setItems(sports);
            if (tournamentSportCombo != null) {
                tournamentSportCombo.setItems(sports);
            }
            if (tournamentSportCombo2 != null) {
                tournamentSportCombo2.setItems(sports);
            }

            if (selectedSport == null && !sports.isEmpty()) {
                selectedSport = sports.get(0);

                // ✅ FIX: Set global combo too!
                if (globalSportCombo != null) {
                    globalSportCombo.setValue(selectedSport);
                }

                teamSportCombo.setValue(selectedSport);
                matchSportCombo.setValue(selectedSport);
                standingsSportCombo.setValue(selectedSport);

                loadTeamsForSport(selectedSport.getId());
                loadTeamsForMatchCombos(selectedSport.getId());
                loadMatchesForSport(selectedSport.getId());
                loadStandings(selectedSport.getId());
            }
        } catch (Exception e) {
            showToastError("Failed to update sport filters: " + e.getMessage());
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

        teamsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && session.canModifyData()) {
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
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to add teams!");
            return;
        }

        Sport sport = teamSportCombo.getValue();
        if (sport == null) {
            showToastWarning("Please select a sport first!");
            teamSportCombo.requestFocus();
            return;
        }

        try {
            String name = teamNameField.getText().trim();
            String coach = coachField.getText().trim();

            if (name.isEmpty() || coach.isEmpty()) {
                showToastWarning("Please fill all fields!");
                return;
            }

            if (name.length() < 2) {
                showToastWarning("Team name must be at least 2 characters!");
                return;
            }

            if (coach.length() < 3) {
                showToastWarning("Coach name must be at least 3 characters!");
                return;
            }

            for (Team team : teamsTable.getItems()) {
                if (team.getName().equalsIgnoreCase(name)) {
                    showToastWarning("A team with this name already exists in this sport!");
                    return;
                }
            }

            Team team = new Team(name, coach, sport.getId());
            teamDAO.addTeam(team);

            loadTeamsForSport(sport.getId());
            loadTeamsForMatchCombos(sport.getId());
            clearTeamFields();
            showToastSuccess("Team '" + name + "' added successfully!");
        } catch (Exception e) {
            showToastError("Failed to add team: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTeam() {
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to delete teams!");
            return;
        }

        Team selected = teamsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a team to delete!");
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

                Sport sport = teamSportCombo.getValue();
                if (sport != null) {
                    loadTeamsForSport(sport.getId());
                    loadTeamsForMatchCombos(sport.getId());
                }
                showToastSuccess("Team deleted successfully!");
            } catch (Exception e) {
                showToastError("Failed to delete team: " + e.getMessage());
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
            System.out.println("📊 Loading teams for sport ID: " + sportId);
            ObservableList<Team> teams = teamDAO.getTeamsBySport(sportId);
            filteredTeams = new FilteredList<>(teams, p -> true);
            SortedList<Team> sortedTeams = new SortedList<>(filteredTeams);
            sortedTeams.comparatorProperty().bind(teamsTable.comparatorProperty());
            teamsTable.setItems(sortedTeams);
            System.out.println("✅ Loaded " + teams.size() + " teams");
        } catch (Exception e) {
            showToastError("Failed to load teams: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTeamsForMatchCombos(int sportId) {
        try {
            ObservableList<Team> teams = teamDAO.getTeamsBySport(sportId);
            team1Combo.setItems(teams);
            team2Combo.setItems(teams);
            if (tournamentWinnerCombo != null) {
                tournamentWinnerCombo.setItems(teams);
            }
        } catch (Exception e) {
            showToastError("Failed to load teams for matches: " + e.getMessage());
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

        standingsTable.setRowFactory(tv -> new TableRow<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setStyle("");
                } else {
                    int index = getIndex() + 1;
                    if (index == 1) {
                        setStyle("-fx-background-color: rgba(255, 215, 0, 0.2);");
                    } else if (index == 2) {
                        setStyle("-fx-background-color: rgba(192, 192, 192, 0.2);");
                    } else if (index == 3) {
                        setStyle("-fx-background-color: rgba(205, 127, 50, 0.2);");
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
            showToastWarning("Please select a sport first!");
        }
    }

    private void loadStandings(int sportId) {
        try {
            System.out.println("📊 Loading standings for sport ID: " + sportId);
            ObservableList<Team> teams = teamDAO.getStandingsBySport(sportId);
            standingsTable.setItems(teams);

            if (statsLabel != null && !teams.isEmpty()) {
                int totalTeams = teams.size();
                int totalMatches = teams.stream().mapToInt(t -> t.getWins() + t.getDraws() + t.getLosses()).sum() / 2;
                int totalGoals = teams.stream().mapToInt(Team::getGoalsFor).sum();

                statsLabel.setText(String.format(
                        "📊 Stats: %d Teams | %d Matches Played | %d Total Goals",
                        totalTeams, totalMatches, totalGoals
                ));
            }
            System.out.println("✅ Loaded standings for " + teams.size() + " teams");
        } catch (Exception e) {
            showToastError("Failed to load standings: " + e.getMessage());
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

        matchesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && session.canModifyData()) {
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
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to schedule matches!");
            return;
        }

        Sport sport = matchSportCombo.getValue();
        if (sport == null) {
            showToastWarning("Please select a sport first!");
            matchSportCombo.requestFocus();
            return;
        }

        try {
            Team team1 = team1Combo.getValue();
            Team team2 = team2Combo.getValue();
            LocalDate date = matchDatePicker.getValue();
            String location = locationField.getText().trim();

            if (team1 == null || team2 == null || date == null || location.isEmpty()) {
                showToastWarning("Please fill all fields!");
                return;
            }

            if (team1.getId() == team2.getId()) {
                showToastWarning("Teams must be different!");
                team2Combo.requestFocus();
                return;
            }

            if (date.isBefore(LocalDate.now())) {
                showToastWarning("Match date cannot be in the past!");
                matchDatePicker.requestFocus();
                return;
            }

            if (location.length() < 3) {
                showToastWarning("Location must be at least 3 characters!");
                locationField.requestFocus();
                return;
            }

            Match match = new Match(team1.getName(), team2.getName(), date, location, sport.getId());
            matchDAO.addMatch(match);

            loadMatchesForSport(sport.getId());
            clearMatchFields();
            showToastSuccess("Match scheduled successfully!");
        } catch (Exception e) {
            showToastError("Failed to add match: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateResult() {
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to update match results!");
            return;
        }

        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a match from the table!");
            return;
        }

        try {
            String score1Text = score1Field.getText().trim();
            String score2Text = score2Field.getText().trim();

            if (score1Text.isEmpty() || score2Text.isEmpty()) {
                showToastWarning("Please enter scores for both teams!");
                score1Field.requestFocus();
                return;
            }

            int score1 = Integer.parseInt(score1Text);
            int score2 = Integer.parseInt(score2Text);

            if (score1 < 0 || score2 < 0) {
                showToastWarning("Scores cannot be negative!");
                return;
            }

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

            Sport sport = matchSportCombo.getValue();
            if (sport != null) {
                loadMatchesForSport(sport.getId());
                loadTeamsForSport(sport.getId());
                loadStandings(sport.getId());
            }

            clearMatchFields();
            showToastSuccess("Match result updated! Standings have been refreshed.");
        } catch (NumberFormatException e) {
            showToastWarning("Please enter valid numeric scores!");
            score1Field.requestFocus();
        } catch (Exception e) {
            showToastError("Failed to update result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteMatch() {
        if (!session.isAdmin()) {
            showToastWarning("Only administrators can delete matches!");
            return;
        }

        Match selected = matchesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a match to delete!");
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

                Sport sport = matchSportCombo.getValue();
                if (sport != null) {
                    loadMatchesForSport(sport.getId());
                }
                showToastSuccess("Match deleted successfully!");
            } catch (Exception e) {
                showToastError("Failed to delete match: " + e.getMessage());
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
            System.out.println("📊 Loading matches for sport ID: " + sportId);
            ObservableList<Match> matches = matchDAO.getMatchesBySport(sportId);
            filteredMatches = new FilteredList<>(matches, p -> true);
            SortedList<Match> sortedMatches = new SortedList<>(filteredMatches);
            sortedMatches.comparatorProperty().bind(matchesTable.comparatorProperty());
            matchesTable.setItems(sortedMatches);
            System.out.println("✅ Loaded " + matches.size() + " matches");
        } catch (Exception e) {
            showToastError("Failed to load matches: " + e.getMessage());
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
    // TOURNAMENTS TAB METHODS (USER-SPECIFIC)
    // ============================================

    private void setupTournamentsTable() {
        if (tournamentsTable == null) return;

        tournamentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tournamentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        tournamentSportCol.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        tournamentTypeCol.setCellValueFactory(new PropertyValueFactory<>("tournamentType"));
        tournamentStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tournamentEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tournamentStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        tournamentWinnerCol.setCellValueFactory(new PropertyValueFactory<>("winnerTeamName"));

        // Style status column
        tournamentStatusCol.setCellFactory(column -> new TableCell<Tournament, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.toUpperCase());
                    String style = switch (status.toLowerCase()) {
                        case "upcoming" -> "-fx-text-fill: #4299e1; -fx-font-weight: bold;";
                        case "ongoing" -> "-fx-text-fill: #48bb78; -fx-font-weight: bold;";
                        case "completed" -> "-fx-text-fill: #ed8936; -fx-font-weight: bold;";
                        case "cancelled" -> "-fx-text-fill: #f56565; -fx-font-weight: bold;";
                        default -> "";
                    };
                    setStyle(style);
                }
            }
        });

        // Selection listener
        tournamentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedTournament = newVal;
        });

        // Double-click to edit
        tournamentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && session.canModifyData()) {
                Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    loadTournamentForEditing(selected);
                }
            }
        });
    }

    @FXML
    private void handleAddTournament() {
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to create tournaments!");
            return;
        }

        try {
            String name = tournamentNameField.getText().trim();
            Sport sport = tournamentSportCombo2 != null ? tournamentSportCombo2.getValue() : null;
            String type = tournamentTypeCombo.getValue();
            LocalDate startDate = tournamentStartDatePicker.getValue();
            LocalDate endDate = tournamentEndDatePicker.getValue();
            String status = tournamentStatusCombo.getValue();
            String prizeText = tournamentPrizeField.getText().trim();
            String description = tournamentDescriptionArea != null ? tournamentDescriptionArea.getText().trim() : "";

            // Validation
            if (name.isEmpty()) {
                showToastWarning("Tournament name cannot be empty!");
                return;
            }

            if (sport == null) {
                showToastWarning("Please select a sport!");
                return;
            }

            if (type == null) {
                showToastWarning("Please select tournament type!");
                return;
            }

            if (startDate == null) {
                showToastWarning("Please select start date!");
                return;
            }

            if (status == null) {
                status = "upcoming";
            }

            double prizeMoney = 0.0;
            if (!prizeText.isEmpty()) {
                try {
                    prizeMoney = Double.parseDouble(prizeText);
                } catch (NumberFormatException e) {
                    showToastWarning("Invalid prize money amount!");
                    return;
                }
            }

            // Create tournament
            Tournament tournament = new Tournament(name, sport.getId(), type, startDate);
            tournament.setEndDate(endDate);
            tournament.setStatus(status);
            tournament.setDescription(description);
            tournament.setPrizeMoney(prizeMoney);
            tournament.setSportName(sport.getName());

            tournamentDAO.addTournament(tournament);
            loadTournaments();
            clearTournamentFields();
            showToastSuccess("Tournament '" + name + "' created successfully!");

        } catch (Exception e) {
            showToastError("Failed to create tournament: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateTournament() {
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to update tournaments!");
            return;
        }

        Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a tournament to update!");
            return;
        }

        try {
            // Check ownership
            if (!session.isAdmin() && !tournamentDAO.isOwner(selected.getId())) {
                showToastWarning("You can only update tournaments you created!");
                return;
            }

            String name = tournamentNameField.getText().trim();
            String type = tournamentTypeCombo.getValue();
            LocalDate startDate = tournamentStartDatePicker.getValue();
            LocalDate endDate = tournamentEndDatePicker.getValue();
            String status = tournamentStatusCombo.getValue();
            String prizeText = tournamentPrizeField.getText().trim();
            String description = tournamentDescriptionArea != null ? tournamentDescriptionArea.getText().trim() : "";
            Team winner = tournamentWinnerCombo.getValue();

            if (name.isEmpty() || type == null || startDate == null || status == null) {
                showToastWarning("Please fill all required fields!");
                return;
            }

            double prizeMoney = 0.0;
            if (!prizeText.isEmpty()) {
                prizeMoney = Double.parseDouble(prizeText);
            }

            selected.setName(name);
            selected.setTournamentType(type);
            selected.setStartDate(startDate);
            selected.setEndDate(endDate);
            selected.setStatus(status);
            selected.setDescription(description);
            selected.setPrizeMoney(prizeMoney);

            if (winner != null) {
                selected.setWinnerTeamId(winner.getId());
                selected.setWinnerTeamName(winner.getName());
            }

            tournamentDAO.updateTournament(selected);
            loadTournaments();
            clearTournamentFields();
            showToastSuccess("Tournament updated successfully!");

        } catch (Exception e) {
            showToastError("Failed to update tournament: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTournament() {
        if (!session.canModifyData()) {
            showToastWarning("You don't have permission to delete tournaments!");
            return;
        }

        Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a tournament to delete!");
            return;
        }

        try {
            // Check ownership
            if (!session.isAdmin() && !tournamentDAO.isOwner(selected.getId())) {
                showToastWarning("You can only delete tournaments you created!");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Tournament: " + selected.getName());
            confirm.setContentText("This will delete all associated data!\nAre you sure?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tournamentDAO.deleteTournament(selected.getId());
                loadTournaments();
                showToastSuccess("Tournament deleted successfully!");
            }
        } catch (Exception e) {
            showToastError("Failed to delete tournament: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTournamentSportFilter() {
        Sport sport = tournamentSportCombo.getValue();
        if (sport != null) {
            try {
                ObservableList<Tournament> tournaments = tournamentDAO.getTournamentsBySport(sport.getId());
                filteredTournaments = new FilteredList<>(tournaments, p -> true);
                tournamentsTable.setItems(filteredTournaments);

                // Load teams for winner selection
                ObservableList<Team> teams = teamDAO.getTeamsBySport(sport.getId());
                if (tournamentWinnerCombo != null) {
                    tournamentWinnerCombo.setItems(teams);
                }
            } catch (Exception e) {
                showToastError("Failed to load tournaments: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleManageTeams() {
        Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a tournament first!");
            return;
        }

        try {
            // Check ownership
            if (!session.isAdmin() && !tournamentDAO.isOwner(selected.getId())) {
                showToastWarning("You can only manage teams for tournaments you created!");
                return;
            }

            // Create team management dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Manage Tournament Teams");
            dialog.setHeaderText("Add/Remove teams for: " + selected.getName());

            // Create layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Available teams list
            ListView<Team> availableTeams = new ListView<>();
            ListView<Team> selectedTeams = new ListView<>();

            // Load teams
            ObservableList<Team> allTeams = teamDAO.getTeamsBySport(selected.getSportId());
            ObservableList<Integer> tournamentTeamIds = tournamentDAO.getTournamentTeamIds(selected.getId());

            ObservableList<Team> available = FXCollections.observableArrayList();
            ObservableList<Team> inTournament = FXCollections.observableArrayList();

            for (Team team : allTeams) {
                if (tournamentTeamIds.contains(team.getId())) {
                    inTournament.add(team);
                } else {
                    available.add(team);
                }
            }

            availableTeams.setItems(available);
            selectedTeams.setItems(inTournament);

            // Buttons
            Button addButton = new Button("Add →");
            Button removeButton = new Button("← Remove");

            addButton.setOnAction(e -> {
                Team team = availableTeams.getSelectionModel().getSelectedItem();
                if (team != null) {
                    try {
                        tournamentDAO.addTeamToTournament(selected.getId(), team.getId());
                        available.remove(team);
                        inTournament.add(team);
                        showToastSuccess("Team added to tournament!");
                    } catch (Exception ex) {
                        showToastError("Failed to add team: " + ex.getMessage());
                    }
                }
            });

            removeButton.setOnAction(e -> {
                Team team = selectedTeams.getSelectionModel().getSelectedItem();
                if (team != null) {
                    try {
                        tournamentDAO.removeTeamFromTournament(selected.getId(), team.getId());
                        inTournament.remove(team);
                        available.add(team);
                        showToastSuccess("Team removed from tournament!");
                    } catch (Exception ex) {
                        showToastError("Failed to remove team: " + ex.getMessage());
                    }
                }
            });

            // Layout
            grid.add(new Label("Available Teams:"), 0, 0);
            grid.add(new Label("Tournament Teams:"), 2, 0);
            grid.add(availableTeams, 0, 1);
            grid.add(new javafx.scene.layout.VBox(10, addButton, removeButton), 1, 1);
            grid.add(selectedTeams, 2, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.showAndWait();

        } catch (Exception e) {
            showToastError("Failed to manage teams: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowTournamentStats() {
        Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a tournament first!");
            return;
        }

        try {
            String stats = tournamentDAO.getTournamentStats(selected.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tournament Statistics");
            alert.setHeaderText(selected.getName());
            alert.setContentText(
                    "Status: " + selected.getStatus().toUpperCase() + "\n" +
                            "Type: " + selected.getTournamentType() + "\n" +
                            "Start Date: " + selected.getStartDate() + "\n" +
                            "End Date: " + (selected.getEndDate() != null ? selected.getEndDate() : "TBD") + "\n" +
                            "Prize Money: $" + String.format("%.2f", selected.getPrizeMoney()) + "\n\n" +
                            stats
            );
            alert.showAndWait();

        } catch (Exception e) {
            showToastError("Failed to load statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTournaments() {
        try {
            System.out.println("📊 Loading tournaments...");
            ObservableList<Tournament> tournaments;

            // Admin sees all tournaments, others see only their own
            if (session.isAdmin()) {
                tournaments = tournamentDAO.getAllTournaments();
                System.out.println("✅ Loaded ALL tournaments (Admin view)");
            } else {
                tournaments = tournamentDAO.getMyTournaments();
                System.out.println("✅ Loaded MY tournaments (User view)");
            }

            if (tournamentsTable != null) {
                filteredTournaments = new FilteredList<>(tournaments, p -> true);
                tournamentsTable.setItems(filteredTournaments);
            }
            System.out.println("✅ Loaded " + tournaments.size() + " tournaments");
        } catch (Exception e) {
            System.err.println("❌ Failed to load tournaments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTournamentForEditing(Tournament tournament) {
        tournamentNameField.setText(tournament.getName());
        tournamentTypeCombo.setValue(tournament.getTournamentType());
        tournamentStartDatePicker.setValue(tournament.getStartDate());
        tournamentEndDatePicker.setValue(tournament.getEndDate());
        tournamentStatusCombo.setValue(tournament.getStatus());
        tournamentPrizeField.setText(String.valueOf(tournament.getPrizeMoney()));

        if (tournamentDescriptionArea != null) {
            tournamentDescriptionArea.setText(tournament.getDescription());
        }

        // Set sport and load teams
        try {
            Sport sport = sportDAO.getSportById(tournament.getSportId());
            if (tournamentSportCombo2 != null) {
                tournamentSportCombo2.setValue(sport);
            }
            handleTournamentSportFilter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTournamentFields() {
        if (tournamentNameField != null) tournamentNameField.clear();
        if (tournamentTypeCombo != null) tournamentTypeCombo.setValue(null);
        if (tournamentStartDatePicker != null) tournamentStartDatePicker.setValue(null);
        if (tournamentEndDatePicker != null) tournamentEndDatePicker.setValue(null);
        if (tournamentStatusCombo != null) tournamentStatusCombo.setValue(null);
        if (tournamentPrizeField != null) tournamentPrizeField.clear();
        if (tournamentDescriptionArea != null) tournamentDescriptionArea.clear();
        if (tournamentWinnerCombo != null) tournamentWinnerCombo.setValue(null);
    }


    @FXML
    private void handleJoinTournament() {
        if (!session.isLoggedIn()) {
            showToastWarning("Please login first!");
            return;
        }

        Tournament selected = tournamentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showToastWarning("Please select a tournament to join!");
            return;
        }

        if (!selected.getStatus().equalsIgnoreCase("upcoming")) {
            showToastWarning("You can only join upcoming tournaments!");
            return;
        }

        try {
            // Check if user already joined
            // You'll need to implement this in TournamentDAO
            showToastInfo("Tournament join feature - coming soon!");

            // Future implementation:
            //TournamentDAO.joinTournament(selected.getId(), session.getCurrentUser().getId());
            //showToastSuccess("Successfully joined tournament: " + selected.getName());

        } catch (Exception e) {
            showToastError("Failed to join tournament: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // ============================================
    // TOAST NOTIFICATION METHODS
    // ============================================

    private void showToastSuccess(String message) {
        try {
            Stage stage = (Stage) sportsTable.getScene().getWindow();
            NotificationUtil.success(stage, message);
        } catch (Exception e) {
            System.err.println("Failed to show toast: " + e.getMessage());
        }
    }

    private void showToastError(String message) {
        try {
            Stage stage = (Stage) sportsTable.getScene().getWindow();
            NotificationUtil.error(stage, message);
        } catch (Exception e) {
            System.err.println("Failed to show toast: " + e.getMessage());
        }
    }

    private void showToastWarning(String message) {
        try {
            Stage stage = (Stage) sportsTable.getScene().getWindow();
            NotificationUtil.warning(stage, message);
        } catch (Exception e) {
            System.err.println("Failed to show toast: " + e.getMessage());
        }
    }

    private void showToastInfo(String message) {
        try {
            Stage stage = (Stage) sportsTable.getScene().getWindow();
            NotificationUtil.info(stage, message);
        } catch (Exception e) {
            System.err.println("Failed to show toast: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}