package com.sportsmanager.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Tournament model - represents a sports tournament
 */
public class Tournament {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty sportId;
    private final StringProperty sportName;
    private final StringProperty tournamentType; // league, knockout, group_knockout
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;
    private final StringProperty status; // upcoming, ongoing, completed, cancelled
    private final StringProperty description;
    private final DoubleProperty prizeMoney;
    private final IntegerProperty winnerTeamId;
    private final StringProperty winnerTeamName;

    public Tournament(int id, String name, int sportId, String sportName,
                      String tournamentType, LocalDate startDate, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.sportId = new SimpleIntegerProperty(sportId);
        this.sportName = new SimpleStringProperty(sportName);
        this.tournamentType = new SimpleStringProperty(tournamentType);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(null);
        this.status = new SimpleStringProperty(status);
        this.description = new SimpleStringProperty("");
        this.prizeMoney = new SimpleDoubleProperty(0.0);
        this.winnerTeamId = new SimpleIntegerProperty(0);
        this.winnerTeamName = new SimpleStringProperty("");
    }

    public Tournament(String name, int sportId, String tournamentType,
                      LocalDate startDate) {
        this(0, name, sportId, "", tournamentType, startDate, "upcoming");
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Sport ID
    public int getSportId() { return sportId.get(); }
    public void setSportId(int value) { sportId.set(value); }
    public IntegerProperty sportIdProperty() { return sportId; }

    // Sport Name
    public String getSportName() { return sportName.get(); }
    public void setSportName(String value) { sportName.set(value); }
    public StringProperty sportNameProperty() { return sportName; }

    // Tournament Type
    public String getTournamentType() { return tournamentType.get(); }
    public void setTournamentType(String value) { tournamentType.set(value); }
    public StringProperty tournamentTypeProperty() { return tournamentType; }

    // Start Date
    public LocalDate getStartDate() { return startDate.get(); }
    public void setStartDate(LocalDate value) { startDate.set(value); }
    public ObjectProperty<LocalDate> startDateProperty() { return startDate; }

    // End Date
    public LocalDate getEndDate() { return endDate.get(); }
    public void setEndDate(LocalDate value) { endDate.set(value); }
    public ObjectProperty<LocalDate> endDateProperty() { return endDate; }

    // Status
    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }

    // Description
    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }
    public StringProperty descriptionProperty() { return description; }

    // Prize Money
    public double getPrizeMoney() { return prizeMoney.get(); }
    public void setPrizeMoney(double value) { prizeMoney.set(value); }
    public DoubleProperty prizeMoneyProperty() { return prizeMoney; }

    // Winner Team ID
    public int getWinnerTeamId() { return winnerTeamId.get(); }
    public void setWinnerTeamId(int value) { winnerTeamId.set(value); }
    public IntegerProperty winnerTeamIdProperty() { return winnerTeamId; }

    // Winner Team Name
    public String getWinnerTeamName() { return winnerTeamName.get(); }
    public void setWinnerTeamName(String value) { winnerTeamName.set(value); }
    public StringProperty winnerTeamNameProperty() { return winnerTeamName; }

    @Override
    public String toString() {
        return name.get() + " (" + status.get() + ")";
    }
}