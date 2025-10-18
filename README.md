# Sports Event Manager 🏆

A comprehensive JavaFX-based desktop application for managing multi-sport tournaments, teams, matches, and standings.

## Features

- 🎯 **Multi-Sport Support** - Manage multiple sports with different scoring systems (Goals, Points, Runs, Sets)
- 👥 **Team Management** - Add, edit, and track teams with coaches and statistics
- ⚽ **Match Scheduling** - Schedule and manage matches with dates and locations
- 🏆 **Live Standings** - Automatic standings calculation based on match results
- 📊 **Statistics Tracking** - Track wins, draws, losses, goals, and points
- 🎨 **Modern Dark UI** - Beautiful gradient-based dark theme with neon accents

## Technology Stack

- **Java 17+** - Core programming language
- **JavaFX 21** - UI framework
- **SQLite** - Local database for data persistence
- **Maven** - Build and dependency management
- **JDBC** - Database connectivity

## Project Structure

```
OOPJ_Mini-Project/
├── src/
│   └── main/
│       ├── java/com/sportsmanager/
│       │   ├── Main.java                    # Application entry point
│       │   ├── controller/
│       │   │   └── MainController.java      # Main UI controller
│       │   ├── dao/
│       │   │   ├── DatabaseConnection.java  # Database connection manager
│       │   │   ├── SportDAO.java           # Sport data access
│       │   │   ├── TeamDAO.java            # Team data access
│       │   │   └── MatchDAO.java           # Match data access
│       │   └── model/
│       │       ├── Sport.java              # Sport entity
│       │       ├── Team.java               # Team entity
│       │       └── Match.java              # Match entity
│       └── resources/
│           ├── fxml/
│           │   └── main.fxml               # Main UI layout
│           ├── css/
│           │   └── style.css               # Dark theme styles
│           └── application.properties       # Configuration
├── pom.xml                                  # Maven configuration
├── sports_manager.db                        # SQLite database (auto-created)
└── README.md                                # This file
```

## Prerequisites

- **Java Development Kit (JDK) 17 or higher**
- **IntelliJ IDEA** (Community or Ultimate Edition)
- **Maven** (Usually bundled with IntelliJ)

## Setup Instructions for IntelliJ IDEA

### 1. Clone or Download the Project

```bash
git clone <your-repository-url>
cd OOPJ_Mini-Project
```

### 2. Open Project in IntelliJ IDEA

1. Launch **IntelliJ IDEA**
2. Click **File** → **Open**
3. Navigate to your project folder and select it
4. Click **OK**
5. IntelliJ will detect it as a Maven project and import it automatically

### 3. Verify Project SDK

1. Go to **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. Under **Project Settings** → **Project**
3. Ensure **SDK** is set to Java 17 or higher
4. Set **Language level** to "17 - Sealed types, always strict floating-point semantics"
5. Click **OK**

### 4. Maven Configuration

1. IntelliJ should automatically download dependencies
2. If not, open **Maven** tool window (View → Tool Windows → Maven)
3. Click the **Reload** button (circular arrows icon)
4. Wait for all dependencies to download

### 5. Run the Application

**Method 1: Run from Main class**
1. Navigate to `src/main/java/com/sportsmanager/Main.java`
2. Right-click on the file
3. Select **Run 'Main.main()'**

**Method 2: Run Maven JavaFX goal**
1. Open **Maven** tool window
2. Expand **sports-event-manager** → **Plugins** → **javafx**
3. Double-click **javafx:run**

**Method 3: Create Run Configuration**
1. Click **Run** → **Edit Configurations**
2. Click **+** → **Maven**
3. Name: "Sports Manager"
4. Command line: `javafx:run`
5. Click **OK** and run

### 6. Build Executable JAR (Optional)

```bash
mvn clean package
```

The JAR will be created in the `target/` directory.

## How to Use the Application

### 1. Managing Sports
1. Go to **🎯 Sports** tab
2. Enter sport name (e.g., "Football")
3. Select scoring type from dropdown:
   - **GOALS** - For football, hockey
   - **POINTS** - For basketball, volleyball
   - **RUNS** - For cricket
   - **SETS** - For tennis, badminton
4. Click **Add Sport**

### 2. Adding Teams
1. Go to **👥 Teams** tab
2. Select a sport from the dropdown
3. Enter team name and coach name
4. Click **Add Team**
5. Teams will appear in the table with initial stats (0-0-0)

### 3. Scheduling Matches
1. Go to **⚽ Matches** tab
2. Select a sport from the dropdown
3. Select Team 1 and Team 2
4. Pick a date and enter location
5. Click **Schedule Match**

### 4. Updating Match Results
1. Go to **⚽ Matches** tab
2. Select a scheduled match from the table
3. Enter scores for both teams
4. Click **Update Result**
5. Standings will automatically update!

### 5. Viewing Standings
1. Go to **🏆 Standings** tab
2. Select a sport from the dropdown
3. Click **Refresh** to see latest standings
4. Teams are sorted by:
   - Points (3 for win, 1 for draw, 0 for loss)
   - Goal Difference
   - Goals For

## Database Schema

The application uses SQLite with three main tables:

### Sports Table
```sql
CREATE TABLE sports (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    scoring_type TEXT NOT NULL
)
```

### Teams Table
```sql
CREATE TABLE teams (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    coach TEXT NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    draws INTEGER DEFAULT 0,
    points INTEGER DEFAULT 0,
    goals_for INTEGER DEFAULT 0,
    goals_against INTEGER DEFAULT 0,
    sport_id INTEGER NOT NULL,
    FOREIGN KEY (sport_id) REFERENCES sports(id)
)
```

### Matches Table
```sql
CREATE TABLE matches (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    team1_name TEXT NOT NULL,
    team2_name TEXT NOT NULL,
    match_date DATE NOT NULL,
    location TEXT NOT NULL,
    team1_score INTEGER DEFAULT 0,
    team2_score INTEGER DEFAULT 0,
    status TEXT DEFAULT 'Scheduled',
    sport_id INTEGER NOT NULL,
    FOREIGN KEY (sport_id) REFERENCES sports(id)
)
```

## Useless Files to Delete

The following files can be safely deleted as they're not needed for IntelliJ IDEA:

### DevContainer Files (for VS Code/Codespaces only)
```
.devcontainer/
scripts/
run.log
```

### Duplicate Compiled Files
```
target/classes/application.properties  # Duplicate
target/classes/css/                     # Duplicate
target/classes/fxml/                    # Duplicate
```

Note: The entire `target/` folder is auto-generated and can be deleted. Maven will recreate it when you build.

### VS Code Settings
```
.vscode/
```

## Troubleshooting

### Issue: "JavaFX runtime components are missing"
**Solution:** Ensure JavaFX is properly configured in your Maven dependencies. Run:
```bash
mvn clean install
```

### Issue: Database not found
**Solution:** The database is auto-created on first run. Ensure you have write permissions in the project directory.

### Issue: UI not displaying correctly
**Solution:** Check that `style.css` is being loaded. Verify the path in `Main.java`.

### Issue: Can't add teams or view standings
**Solution:** 
1. Ensure you've added at least one sport first
2. Select a sport from the dropdown before adding teams
3. Check console for any error messages

### Issue: Maven dependencies not downloading
**Solution:**
1. Check internet connection
2. Go to File → Settings → Build → Maven
3. Click "Update" or "Reimport"
4. Try: `mvn clean install -U`

## Default Sports Preloaded

On first run, these sports are automatically added:
- ⚽ Football (GOALS)
- 🏀 Basketball (POINTS)
- 🏏 Cricket (RUNS)
- 🎾 Tennis (SETS)

## Key Features Explained

### Points System
- **Win:** 3 points
- **Draw:** 1 point
- **Loss:** 0 points

### Standings Calculation
Teams are ranked by:
1. Total Points (descending)
2. Goal Difference (descending)
3. Goals For (descending)

### Match Status
- **Scheduled** - Match not yet played
- **Completed** - Result entered

## License

Educational project for OOPJ course.

## Contributors

Developed as a mini-project for Object-Oriented Programming with Java (OOPJ) course.

---

**Need Help?** Check the IntelliJ IDEA console for error messages or debug information.