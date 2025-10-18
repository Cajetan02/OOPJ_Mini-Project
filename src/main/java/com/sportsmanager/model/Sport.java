package com.sportsmanager.model;

import javafx.beans.property.*;

public class Sport {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty scoringType; // "POINTS", "GOALS", "RUNS", "SETS"

    public Sport(int id, String name, String scoringType) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.scoringType = new SimpleStringProperty(scoringType);
    }

    public Sport(String name, String scoringType) {
        this(0, name, scoringType);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Scoring Type
    public String getScoringType() { return scoringType.get(); }
    public void setScoringType(String value) { scoringType.set(value); }
    public StringProperty scoringTypeProperty() { return scoringType; }

    @Override
    public String toString() {
        return name.get();
    }
}