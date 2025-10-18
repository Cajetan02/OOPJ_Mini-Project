package com.sportsmanager.model;

import javafx.beans.property.*;

public class Team {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty coach;
    private final IntegerProperty wins;
    private final IntegerProperty losses;
    private final IntegerProperty draws;
    private final IntegerProperty points;
    private final IntegerProperty goalsFor;
    private final IntegerProperty goalsAgainst;
    private final IntegerProperty sportId;

    // Full constructor with all 10 parameters
    public Team(int id, String name, String coach, int wins, int losses, int draws,
                int points, int goalsFor, int goalsAgainst, int sportId) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.coach = new SimpleStringProperty(coach);
        this.wins = new SimpleIntegerProperty(wins);
        this.losses = new SimpleIntegerProperty(losses);
        this.draws = new SimpleIntegerProperty(draws);
        this.points = new SimpleIntegerProperty(points);
        this.goalsFor = new SimpleIntegerProperty(goalsFor);
        this.goalsAgainst = new SimpleIntegerProperty(goalsAgainst);
        this.sportId = new SimpleIntegerProperty(sportId);
    }

    // Simple constructor for new teams
    public Team(String name, String coach, int sportId) {
        this(0, name, coach, 0, 0, 0, 0, 0, 0, sportId);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Coach
    public String getCoach() { return coach.get(); }
    public void setCoach(String value) { coach.set(value); }
    public StringProperty coachProperty() { return coach; }

    // Wins
    public int getWins() { return wins.get(); }
    public void setWins(int value) { wins.set(value); }
    public IntegerProperty winsProperty() { return wins; }

    // Losses
    public int getLosses() { return losses.get(); }
    public void setLosses(int value) { losses.set(value); }
    public IntegerProperty lossesProperty() { return losses; }

    // Draws
    public int getDraws() { return draws.get(); }
    public void setDraws(int value) { draws.set(value); }
    public IntegerProperty drawsProperty() { return draws; }

    // Points
    public int getPoints() { return points.get(); }
    public void setPoints(int value) { points.set(value); }
    public IntegerProperty pointsProperty() { return points; }

    // Goals For
    public int getGoalsFor() { return goalsFor.get(); }
    public void setGoalsFor(int value) { goalsFor.set(value); }
    public IntegerProperty goalsForProperty() { return goalsFor; }

    // Goals Against
    public int getGoalsAgainst() { return goalsAgainst.get(); }
    public void setGoalsAgainst(int value) { goalsAgainst.set(value); }
    public IntegerProperty goalsAgainstProperty() { return goalsAgainst; }

    // Sport ID
    public int getSportId() { return sportId.get(); }
    public void setSportId(int value) { sportId.set(value); }
    public IntegerProperty sportIdProperty() { return sportId; }

    // Calculated: Goal Difference
    public int getGoalDifference() {
        return getGoalsFor() - getGoalsAgainst();
    }

    @Override
    public String toString() {
        return name.get();
    }
}