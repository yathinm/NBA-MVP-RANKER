# NBA MVP Ranker

A comprehensive NBA MVP analysis tool that processes player statistics and ranks the top MVP candidates using a custom scoring algorithm. The application consists of two components: a Python data processing engine and a Java GUI viewer.

## 🏀 Features

- **Data Processing**: Loads and processes NBA player statistics from CSV format
- **MVP Scoring Algorithm**: Calculates MVP scores using a weighted formula based on key performance metrics
- **Player Filtering**: Automatically filters out players with insufficient playing time (less than 20 games)
- **Ranking System**: Sorts and displays top MVP candidates
- **GUI Viewer**: Java-based graphical interface to view results
- **Export Functionality**: Saves results to CSV format for further analysis

## 🛠️ Tech Stack

### Backend (Data Processing)
- **Python 3.x**
- **Pandas**: Data manipulation and analysis
- **CSV Processing**: Native Python CSV handling

### Frontend (GUI Viewer)
- **Java 8+**
- **Swing**: GUI framework for the table viewer
- **JTable**: Data display component
- **JScrollPane**: Scrollable table interface

### Data Format
- **Input**: NBA player statistics in CSV format
- **Output**: Ranked MVP candidates in CSV format

## 📊 MVP Scoring Formula

The application uses a weighted scoring system to rank players:

```
MVP_Score = (PTS × 0.4) + (AST × 0.2) + (TRB × 0.2) + (STL × 0.1) + (BLK × 0.1)
```

**Weight Distribution:**
- **Points (PTS)**: 40% - Primary scoring contribution
- **Assists (AST)**: 20% - Playmaking ability
- **Rebounds (TRB)**: 20% - Rebounding contribution
- **Steals (STL)**: 10% - Defensive impact
- **Blocks (BLK)**: 10% - Defensive impact

## 🚀 How to Run

### Prerequisites

1. **Python 3.x** installed on your system
2. **Java 8 or higher** installed on your system
3. **NBA player statistics CSV file** (e.g., `NBA_2024_per_game.csv`)

### Step 1: Data Processing (Python)

1. **Navigate to the project directory:**
   ```bash
   cd /path/to/NBA-MVP-RANKER
   ```

2. **Install Python dependencies:**
   ```bash
   pip install pandas
   ```

3. **Run the Python script:**
   ```bash
   python NBAMVPSORTER.py
   ```

   This will:
   - Load the NBA statistics from `NBA_2024_per_game.csv`
   - Filter players with at least 20 games played
   - Calculate MVP scores using the weighted formula
   - Display the top 10 MVP candidates in the terminal
   - Export results to `mvp_candidates.csv`

### Step 2: GUI Viewer (Java)

1. **Compile the Java application:**
   ```bash
   javac MVPViewer.java
   ```

2. **Run the Java GUI:**
   ```bash
   java MVPViewer
   ```

   This will open a graphical window displaying the MVP candidates in a sortable table format.

## 📋 Input Data Format

The application expects NBA player statistics in CSV format with the following columns:
- `Player`: Player name
- `Team`: Team abbreviation
- `G`: Games played
- `PTS`: Total points
- `AST`: Total assists
- `TRB`: Total rebounds
- `STL`: Total steals
- `BLK`: Total blocks
