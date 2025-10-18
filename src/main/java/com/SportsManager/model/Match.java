package com.sportsmanager.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Match {
    private final IntegerProperty id;
    private final StringProperty team1Name;
    private final StringProperty team2Name;
    private final ObjectProperty<LocalDate> matchDate;
    private final StringProperty location;
    private final IntegerProperty team1Score;
    private final IntegerProperty team2Score;
    private final StringProperty status;

    public Match(int id, String team1Name, String team2Name, LocalDate matchDate, 
                 String location, int team1Score, int team2Score, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.team1Name = new SimpleStringProperty(team1Name);
        this.team2Name = new SimpleStringProperty(team2Name);
        this.matchDate = new SimpleObjectProperty<>(matchDate);
        this.location = new SimpleStringProperty(location);
        this.team1Score = new SimpleIntegerProperty(team1Score);
        this.team2Score = new SimpleIntegerProperty(team2Score);
        this.status = new SimpleStringProperty(status);
    }

    public Match(String team1Name, String team2Name, LocalDate matchDate, String location) {
        this(0, team1Name, team2Name, matchDate, location, 0, 0, "Scheduled");
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Team 1 Name
    public String getTeam1Name() { return team1Name.get(); }
    public void setTeam1Name(String value) { team1Name.set(value); }
    public StringProperty team1NameProperty() { return team1Name; }

    // Team 2 Name
    public String getTeam2Name() { return team2Name.get(); }
    public void setTeam2Name(String value) { team2Name.set(value); }
    public StringProperty team2NameProperty() { return team2Name; }

    // Match Date
    public LocalDate getMatchDate() { return matchDate.get(); }
    public void setMatchDate(LocalDate value) { matchDate.set(value); }
    public ObjectProperty<LocalDate> matchDateProperty() { return matchDate; }

    // Location
    public String getLocation() { return location.get(); }
    public void setLocation(String value) { location.set(value); }
    public StringProperty locationProperty() { return location; }

    // Team 1 Score
    public int getTeam1Score() { return team1Score.get(); }
    public void setTeam1Score(int value) { team1Score.set(value); }
    public IntegerProperty team1ScoreProperty() { return team1Score; }

    // Team 2 Score
    public int getTeam2Score() { return team2Score.get(); }
    public void setTeam2Score(int value) { team2Score.set(value); }
    public IntegerProperty team2ScoreProperty() { return team2Score; }

    // Status
    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }
}