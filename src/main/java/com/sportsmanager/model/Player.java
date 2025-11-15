package com.sportsmanager.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Player model - represents a player in a team
 */
public class Player {
    private final IntegerProperty id;
    private final StringProperty userId; // Link to User
    private final IntegerProperty teamId;
    private final StringProperty teamName;
    private final IntegerProperty jerseyNumber;
    private final StringProperty position;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty nationality;
    private final IntegerProperty heightCm;
    private final IntegerProperty weightKg;
    private final IntegerProperty goalsScored;
    private final IntegerProperty assists;
    private final IntegerProperty yellowCards;
    private final IntegerProperty redCards;
    private final IntegerProperty matchesPlayed;
    private final BooleanProperty isActive;
    private final ObjectProperty<LocalDate> joinedDate;

    public Player(int id, String userId, int teamId, String teamName,
                  int jerseyNumber, String position) {
        this.id = new SimpleIntegerProperty(id);
        this.userId = new SimpleStringProperty(userId);
        this.teamId = new SimpleIntegerProperty(teamId);
        this.teamName = new SimpleStringProperty(teamName);
        this.jerseyNumber = new SimpleIntegerProperty(jerseyNumber);
        this.position = new SimpleStringProperty(position);
        this.dateOfBirth = new SimpleObjectProperty<>(null);
        this.nationality = new SimpleStringProperty("");
        this.heightCm = new SimpleIntegerProperty(0);
        this.weightKg = new SimpleIntegerProperty(0);
        this.goalsScored = new SimpleIntegerProperty(0);
        this.assists = new SimpleIntegerProperty(0);
        this.yellowCards = new SimpleIntegerProperty(0);
        this.redCards = new SimpleIntegerProperty(0);
        this.matchesPlayed = new SimpleIntegerProperty(0);
        this.isActive = new SimpleBooleanProperty(true);
        this.joinedDate = new SimpleObjectProperty<>(LocalDate.now());
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // User ID
    public String getUserId() { return userId.get(); }
    public void setUserId(String value) { userId.set(value); }
    public StringProperty userIdProperty() { return userId; }

    // Team ID
    public int getTeamId() { return teamId.get(); }
    public void setTeamId(int value) { teamId.set(value); }
    public IntegerProperty teamIdProperty() { return teamId; }

    // Team Name
    public String getTeamName() { return teamName.get(); }
    public void setTeamName(String value) { teamName.set(value); }
    public StringProperty teamNameProperty() { return teamName; }

    // Jersey Number
    public int getJerseyNumber() { return jerseyNumber.get(); }
    public void setJerseyNumber(int value) { jerseyNumber.set(value); }
    public IntegerProperty jerseyNumberProperty() { return jerseyNumber; }

    // Position
    public String getPosition() { return position.get(); }
    public void setPosition(String value) { position.set(value); }
    public StringProperty positionProperty() { return position; }

    // Date of Birth
    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public void setDateOfBirth(LocalDate value) { dateOfBirth.set(value); }
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }

    // Nationality
    public String getNationality() { return nationality.get(); }
    public void setNationality(String value) { nationality.set(value); }
    public StringProperty nationalityProperty() { return nationality; }

    // Height
    public int getHeightCm() { return heightCm.get(); }
    public void setHeightCm(int value) { heightCm.set(value); }
    public IntegerProperty heightCmProperty() { return heightCm; }

    // Weight
    public int getWeightKg() { return weightKg.get(); }
    public void setWeightKg(int value) { weightKg.set(value); }
    public IntegerProperty weightKgProperty() { return weightKg; }

    // Goals Scored
    public int getGoalsScored() { return goalsScored.get(); }
    public void setGoalsScored(int value) { goalsScored.set(value); }
    public IntegerProperty goalsScoredProperty() { return goalsScored; }

    // Assists
    public int getAssists() { return assists.get(); }
    public void setAssists(int value) { assists.set(value); }
    public IntegerProperty assistsProperty() { return assists; }

    // Yellow Cards
    public int getYellowCards() { return yellowCards.get(); }
    public void setYellowCards(int value) { yellowCards.set(value); }
    public IntegerProperty yellowCardsProperty() { return yellowCards; }

    // Red Cards
    public int getRedCards() { return redCards.get(); }
    public void setRedCards(int value) { redCards.set(value); }
    public IntegerProperty redCardsProperty() { return redCards; }

    // Matches Played
    public int getMatchesPlayed() { return matchesPlayed.get(); }
    public void setMatchesPlayed(int value) { matchesPlayed.set(value); }
    public IntegerProperty matchesPlayedProperty() { return matchesPlayed; }

    // Is Active
    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean value) { isActive.set(value); }
    public BooleanProperty isActiveProperty() { return isActive; }

    // Joined Date
    public LocalDate getJoinedDate() { return joinedDate.get(); }
    public void setJoinedDate(LocalDate value) { joinedDate.set(value); }
    public ObjectProperty<LocalDate> joinedDateProperty() { return joinedDate; }

    @Override
    public String toString() {
        return "#" + jerseyNumber.get() + " - " + position.get();
    }
}