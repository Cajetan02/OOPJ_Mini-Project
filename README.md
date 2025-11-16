# Sports Event Manager Pro 🏆

A comprehensive JavaFX-based desktop application for managing multi-sport tournaments, teams, matches, and standings with cloud-based data storage and multi-user authentication.

## ✨ Features

### 🔐 **User Authentication & Role-Based Access Control**
- Secure user registration and login system
- Password encryption using BCrypt
- Role-based permissions (Admin, Manager, Player)
- Session management with automatic token generation
- User profile management

### 🎯 **Multi-Sport Management**
- Support for multiple sports with different scoring systems:
  - **GOALS** - Football, Hockey
  - **POINTS** - Basketball, Volleyball
  - **RUNS** - Cricket
  - **SETS** - Tennis, Badminton
- Add, edit, and delete sports (Admin only)
- Real-time sport filtering across all modules

### 👥 **Team Management**
- Create and manage teams per sport
- Track team statistics:
  - Wins, Draws, Losses
  - Goals For/Against
  - Goal Difference
  - Total Points (3 for win, 1 for draw)
- Assign coaches to teams
- Search and filter teams
- Double-click to edit team details

### ⚽ **Match Scheduling & Results**
- Schedule matches with date, time, and location
- Automatic team filtering based on selected sport
- Update match results with score tracking
- Automatic standings calculation upon result entry
- Match status tracking (Scheduled/Completed)
- Prevent scheduling matches in the past
- Search matches by teams or location

### 🏆 **Live Standings**
- Real-time standings calculation
- Automatic sorting by:
  1. Points (descending)
  2. Goal Difference (descending)
  3. Goals For (descending)
- Visual highlighting for top 3 positions:
  - 🥇 Gold for 1st place
  - 🥈 Silver for 2nd place
  - 🥉 Bronze for 3rd place
- Statistics summary (total teams, matches, goals)

### 🏟️ **Tournament System**
- Create and manage tournaments with:
  - Tournament types (League, Knockout, Group + Knockout)
  - Start and end dates
  - Prize money tracking
  - Status management (Upcoming, Ongoing, Completed, Cancelled)
  - Winner assignment
- Tournament-specific features:
  - Add/remove teams from tournaments
  - Share tournaments via unique codes
  - Tournament statistics dashboard
  - User-specific tournament views (Managers see only their tournaments)
- Team management within tournaments
- Tournament member roles and permissions

### 🎨 **Modern Dark UI Theme**
- Beautiful gradient-based dark theme
- Neon purple accent colors
- Smooth animations and transitions
- Toast notifications for user feedback:
  - ✅ Success notifications (green)
  - ❌ Error notifications (red)
  - ⚠️ Warning notifications (orange)
  - ℹ️ Info notifications (blue)
- Responsive table designs with hover effects
- Card-based layout for better organization

### ☁️ **Cloud Database Integration**
- Supabase PostgreSQL backend
- Connection pooling for optimal performance
- Real-time data synchronization
- Automatic connection status monitoring
- Detailed connection diagnostics
- SQLite fallback for offline mode

### 🔒 **Security Features**
- Encrypted password storage
- Session token management
- Permission-based UI element control
- User activity tracking
- Secure credential storage

## 🛠️ Technology Stack

- **Java 25** - Core programming language
- **JavaFX 21** - Modern UI framework
- **Supabase (PostgreSQL)** - Cloud database
- **SQLite** - Local database fallback
- **Maven** - Build and dependency management
- **JDBC** - Database connectivity
- **BCrypt** - Password hashing
- **Gson** - JSON processing
- **OkHttp** - HTTP client for API calls

## 📁 Project Structure

```
OOPJ_Mini-Project/
├── src/
│   └── main/
│       ├── java/com/sportsmanager/
│       │   ├── Main.java                           # Application entry point
│       │   ├── controller/
│       │   │   ├── LoginController.java            # Login UI controller
│       │   │   ├── RegistrationController.java     # Registration UI controller
│       │   │   └── MainController.java             # Main application controller
│       │   ├── dao/
│       │   │   ├── SupabaseConnection.java         # Cloud database connection
│       │   │   ├── SportDAO.java                   # Sport data access
│       │   │   ├── TeamDAO.java                    # Team data access
│       │   │   ├── MatchDAO.java                   # Match data access
│       │   │   ├── TournamentDAO.java              # Tournament data access
│       │   │   └── TournamentUserDAO.java          # Tournament permissions
│       │   ├── model/
│       │   │   ├── User.java                       # User entity
│       │   │   ├── Sport.java                      # Sport entity
│       │   │   ├── Team.java                       # Team entity
│       │   │   ├── Match.java                      # Match entity
│       │   │   ├── Tournament.java                 # Tournament entity
│       │   │   └── Player.java                     # Player entity
│       │   └── util/
│       │       ├── SessionManager.java             # Session & permissions
│       │       ├── NotificationUtil.java           # Toast notifications
│       │       └── ConfigManager.java              # Configuration manager
│       └── resources/
│           ├── fxml/
│           │   ├── login.fxml                      # Login screen layout
│           │   ├── registration.fxml               # Registration layout
│           │   └── main.fxml                       # Main UI layout
│           ├── css/
│           │   └── style.css                       # Dark theme styles
│           ├── config.properties                   # Database configuration
│           └── application.properties              # App settings
├── pom.xml                                         # Maven configuration
├── sports_manager_local.db                         # SQLite fallback (auto-created)
└── README.md                                       # This file
```

## 🚀 Prerequisites

- **Java Development Kit (JDK) 17 or higher**
- **IntelliJ IDEA** (Community or Ultimate Edition)
- **Maven** (Usually bundled with IntelliJ)
- **Supabase Account** (for cloud database) - Optional, SQLite fallback available

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd OOPJ_Mini-Project
```

### 2. Configure Database Connection

#### Option A: Using Supabase (Recommended)

1. Create a free account at [Supabase](https://supabase.com)
2. Create a new project
3. Go to **Settings → Database**
4. Copy your connection details
5. Update `src/main/resources/config.properties`:

```properties
# Supabase Configuration
supabase.url=YOUR_SUPABASE_URL
supabase.anon.key=YOUR_ANON_KEY

# Database Connection
supabase.db.host=YOUR_HOST
supabase.db.port=6543
supabase.db.name=postgres
supabase.db.user=postgres.YOUR_PROJECT_REF
supabase.db.password=YOUR_PASSWORD
```

6. Run the SQL schema from Supabase SQL Editor (see Database Schema section)

#### Option B: Using SQLite (Local Only)

No configuration needed! The application will automatically use SQLite if Supabase is unavailable.

### 3. Open Project in IntelliJ IDEA

1. Launch **IntelliJ IDEA**
2. Click **File** → **Open**
3. Navigate to the project folder and select it
4. Click **OK**
5. IntelliJ will detect it as a Maven project and import it automatically

### 4. Verify Project SDK

1. Go to **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. Under **Project Settings** → **Project**
3. Ensure **SDK** is set to Java 17 or higher
4. Set **Language level** to "17 - Sealed types, always strict floating-point semantics"
5. Click **OK**

### 5. Download Dependencies

1. IntelliJ should automatically download dependencies
2. If not, open **Maven** tool window (View → Tool Windows → Maven)
3. Click the **Reload** button (circular arrows icon)
4. Wait for all dependencies to download

### 6. Run the Application

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

### 7. Build Executable JAR (Optional)

```bash
mvn clean package
```

The JAR will be created in the `target/` directory.

## 📊 Database Schema

### Supabase PostgreSQL Schema

Run this SQL in your Supabase SQL Editor:

```sql
-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users Table
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'player' CHECK (role IN ('admin', 'manager', 'player')),
    avatar_url TEXT,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT true,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Credentials (Separate for security)
CREATE TABLE user_credentials (
    user_id UUID PRIMARY KEY REFERENCES profiles(id) ON DELETE CASCADE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sports Table
CREATE TABLE sports (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    scoring_type VARCHAR(20) NOT NULL CHECK (scoring_type IN ('GOALS', 'POINTS', 'RUNS', 'SETS')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Teams Table
CREATE TABLE teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    coach VARCHAR(100) NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    draws INTEGER DEFAULT 0,
    points INTEGER DEFAULT 0,
    goals_for INTEGER DEFAULT 0,
    goals_against INTEGER DEFAULT 0,
    sport_id INTEGER NOT NULL REFERENCES sports(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(name, sport_id)
);

-- Matches Table
CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    team1_name VARCHAR(100) NOT NULL,
    team2_name VARCHAR(100) NOT NULL,
    match_date DATE NOT NULL,
    location VARCHAR(200) NOT NULL,
    team1_score INTEGER DEFAULT 0,
    team2_score INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'Scheduled' CHECK (status IN ('Scheduled', 'Completed')),
    sport_id INTEGER NOT NULL REFERENCES sports(id) ON DELETE CASCADE,
    tournament_id INTEGER REFERENCES tournaments(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tournaments Table
CREATE TABLE tournaments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sport_id INTEGER NOT NULL REFERENCES sports(id) ON DELETE CASCADE,
    tournament_type VARCHAR(20) NOT NULL CHECK (tournament_type IN ('league', 'knockout', 'group_knockout')),
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'upcoming' CHECK (status IN ('upcoming', 'ongoing', 'completed', 'cancelled')),
    description TEXT,
    prize_money DECIMAL(10,2) DEFAULT 0,
    winner_team_id INTEGER REFERENCES teams(id) ON DELETE SET NULL,
    share_code VARCHAR(20) UNIQUE,
    created_by UUID REFERENCES profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tournament Teams (Many-to-Many)
CREATE TABLE tournament_teams (
    tournament_id INTEGER REFERENCES tournaments(id) ON DELETE CASCADE,
    team_id INTEGER REFERENCES teams(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tournament_id, team_id)
);

-- Tournament Members (User access to tournaments)
CREATE TABLE tournament_members (
    tournament_id INTEGER REFERENCES tournaments(id) ON DELETE CASCADE,
    user_id UUID REFERENCES profiles(id) ON DELETE CASCADE,
    role VARCHAR(20) DEFAULT 'viewer' CHECK (role IN ('admin', 'manager', 'viewer')),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tournament_id, user_id)
);

-- Insert default sports
INSERT INTO sports (name, scoring_type) VALUES
    ('Football', 'GOALS'),
    ('Basketball', 'POINTS'),
    ('Cricket', 'RUNS'),
    ('Tennis', 'SETS'),
    ('Hockey', 'GOALS'),
    ('Volleyball', 'POINTS');
```

## 👤 User Roles & Permissions

### Admin 👑
- Full system access
- Manage sports (add/edit/delete)
- Manage all teams and matches
- View all tournaments
- Delete any data
- User management

### Manager 📋
- Create and manage tournaments
- Add/edit teams and matches
- View own tournaments only
- Cannot delete sports
- Limited to assigned sports

### Player ⚽
- View-only access to data
- Can register and login
- Join tournaments via share codes
- View standings and statistics
- Cannot modify data

## 🎮 How to Use

### First Time Setup

1. **Register an Account**
   - Click "Register here" on login screen
   - Fill in username (min 3 chars), full name, email
   - Set password (min 6 chars)
   - Select role (start with "player" or "manager")
   - Click "Create Account"

2. **Login**
   - Enter your username and password
   - Click "Login"
   - You'll be redirected to the main application

### Managing Sports (Admin Only)

1. Go to **🎯 Sports** tab
2. Enter sport name (e.g., "Football")
3. Select scoring type from dropdown
4. Click **Add Sport**
5. Sports appear in the table below
6. Double-click a sport to edit
7. Select and click **Delete** to remove

### Adding Teams

1. Go to **👥 Teams** tab
2. Select a sport from the dropdown at the top
3. Enter team name and coach name
4. Click **Add Team**
5. Teams appear with stats (0-0-0 initially)
6. Use search bar to filter teams

### Scheduling Matches

1. Go to **⚽ Matches** tab
2. Select a sport from the dropdown
3. Select Team 1 and Team 2
4. Pick a match date (cannot be in past)
5. Enter location
6. Click **Schedule Match**

### Updating Match Results

1. Go to **⚽ Matches** tab
2. Select a scheduled match from the table
3. Enter scores for both teams
4. Click **Update Result**
5. Standings automatically update!

### Viewing Standings

1. Go to **🏆 Standings** tab
2. Select a sport from the dropdown
3. View real-time standings sorted by points
4. Top 3 teams highlighted with colors
5. Statistics summary shown at top

### Managing Tournaments

1. Go to **🏆 Tournaments** tab
2. Click **Create Tournament**
3. Fill in tournament details:
   - Name
   - Sport
   - Type (League/Knockout/Group+Knockout)
   - Dates
   - Prize money (optional)
4. Click **Add Tournament**
5. Use **Manage Teams** to add teams
6. Update status as tournament progresses
7. Set winner when completed

### Sharing Tournaments

1. Select a tournament
2. Note the Tournament ID shown
3. Share this ID with other users
4. They can use "Join Tournament" feature
5. Enter the share code to participate

## 🔧 Configuration

### config.properties

```properties
# Application Info
app.name=Sports Event Manager Pro
app.version=3.0
app.environment=production

# Supabase Configuration
supabase.url=YOUR_SUPABASE_URL
supabase.anon.key=YOUR_ANON_KEY

# Database Connection
supabase.db.host=YOUR_HOST
supabase.db.port=6543
supabase.db.name=postgres
supabase.db.user=postgres.YOUR_PROJECT_REF
supabase.db.password=YOUR_PASSWORD

# Local SQLite Fallback
local.db.enabled=true
local.db.path=sports_manager_local.db

# Points System
points.win=3
points.draw=1
points.loss=0

# Features Toggle
feature.tournaments.enabled=true
feature.players.enabled=true
feature.notifications.enabled=true
```

## 🐛 Troubleshooting

### Issue: "JavaFX runtime components are missing"
**Solution:** Ensure JavaFX is properly configured in Maven dependencies. Run:
```bash
mvn clean install
```

### Issue: Cannot connect to Supabase
**Solution:**
1. Check `config.properties` has correct credentials
2. Test connection in Supabase SQL Editor
3. Verify project is active (not paused)
4. Check firewall/VPN settings
5. Application will fallback to SQLite automatically

### Issue: Login fails with "Invalid credentials"
**Solution:**
1. Verify username and password
2. Check if user exists in database
3. Try registering a new account
4. Check database connection status

### Issue: UI not displaying correctly
**Solution:**
1. Verify `style.css` is being loaded
2. Check console for errors
3. Restart the application
4. Clear Maven cache: `mvn clean`

### Issue: Maven dependencies not downloading
**Solution:**
1. Check internet connection
2. Go to File → Settings → Build → Maven
3. Click "Update" or "Reimport"
4. Try: `mvn clean install -U`

### Issue: Session expires too quickly
**Solution:** Adjust session timeout in `config.properties`:
```properties
session.timeout.minutes=30
```

## 📱 Key Application Screens

### Login Screen
Modern dark-themed login with gradient header and secure authentication

### Main Dashboard
Multi-tab interface with:
- 🎯 Sports Management
- 👥 Team Management
- 🏆 Live Standings
- ⚽ Match Scheduling
- 🏟️ Tournament System

### Live Standings
Real-time standings with:
- Color-coded top 3 positions
- Automatic sorting by points
- Goal difference tracking
- Statistics summary

### Match Management
- Schedule matches with date/location
- Update results with automatic standings calculation
- Search and filter matches
- Status tracking

### Tournament System
- Create and manage tournaments
- Add/remove teams
- Share tournaments via codes
- Track tournament progress

## 🎨 Theme Customization

The application uses a dark theme with customizable colors in `style.css`:

- **Primary Color:** `#667eea` (Purple)
- **Success Color:** `#48bb78` (Green)
- **Danger Color:** `#f56565` (Red)
- **Warning Color:** `#ed8936` (Orange)
- **Info Color:** `#4299e1` (Blue)

Modify these in `src/main/resources/css/style.css` to customize the look.

## 🔒 Security Features

- ✅ Password encryption with BCrypt
- ✅ Session token management
- ✅ Role-based access control
- ✅ SQL injection prevention via PreparedStatements
- ✅ Separate credential storage
- ✅ Connection pooling for security
- ✅ Automatic session timeout

## 📈 Future Enhancements

- [ ] Player statistics tracking
- [ ] Match scheduling calendar view
- [ ] Export data to PDF/Excel
- [ ] Real-time match updates
- [ ] Email notifications
- [ ] Mobile app version
- [ ] Multi-language support
- [ ] Advanced analytics dashboard
- [ ] Tournament bracket visualization
- [ ] Social media integration

## 🤝 Contributing

This is an educational project for the OOPJ course. Contributions, suggestions, and feedback are welcome!

## 📄 License

Educational project for Object-Oriented Programming with Java (OOPJ) course.

## 👨‍💻 Development Team

Developed as a mini-project for OOPJ course demonstrating:
- Object-Oriented Programming principles
- JavaFX GUI development
- Database integration
- Cloud services integration
- User authentication
- Role-based access control

## 📞 Support

For issues or questions:
1. Check the Troubleshooting section
2. Review console logs for error messages
3. Verify database configuration
4. Check Supabase connection status

---

**Built with ❤️ using Java, JavaFX, and Supabase**
