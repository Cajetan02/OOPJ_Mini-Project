package com.sportsmanager.model;

import javafx.beans.property.*;

public class Team {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty coach;
    private final IntegerProperty wins;
    private final IntegerProperty losses;

    public Team(int id, String name, String coach, int wins, int losses) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.coach = new SimpleStringProperty(coach);
        this.wins = new SimpleIntegerProperty(wins);
        this.losses = new SimpleIntegerProperty(losses);
    }

    public Team(String name, String coach) {
        this(0, name, coach, 0, 0);
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

    @Override
    public String toString() {
        return name.get();
    }
}