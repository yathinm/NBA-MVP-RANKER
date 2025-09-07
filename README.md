# NBA MVP Ranker - Enhanced Edition


A comprehensive NBA MVP analysis tool that processes player statistics and ranks the top MVP candidates using multiple advanced scoring algorithms. The application consists of two components: an enhanced Python data processing engine with advanced analytics and a modern Java GUI viewer with professional styling.

## 🏀 Features

### Core Functionality
- **Advanced Data Processing**: Loads and processes NBA player statistics from CSV format
- **Multiple Scoring Algorithms**: 
  - Basic MVP Score (original formula)
  - Advanced MVP Score (with efficiency metrics)
  - Position-Adjusted Score (position-specific weights)
  - Team Impact Score (relative to team performance)
  - Composite Score (weighted combination of all methods)
- **Intelligent Filtering**: Automatically filters players with insufficient playing time (20+ games)
- **Advanced Ranking System**: Multiple sorting and ranking options

### Enhanced Java GUI
- **Modern Professional Interface**: Dark theme with modern styling
- **Real-time Search & Filtering**: Search by player name, filter by team
- **Advanced Sorting**: Sort by any statistical category (ascending/descending)
- **Interactive Data Table**: Professional table with alternating row colors
- **Export Capabilities**: Export to CSV and JSON formats
- **Status Dashboard**: Real-time statistics and analysis summary

### Advanced Analytics
- **Statistical Analysis**: Comprehensive team and position analysis
- **Data Visualization**: Interactive charts and graphs (matplotlib/seaborn)
- **Multiple Export Formats**: CSV, JSON with detailed analysis
- **Command Line Interface**: Full CLI support with customizable options
- **Performance Metrics**: True shooting percentage, assist-to-turnover ratio

## 🛠️ Tech Stack

### Backend (Data Processing)
- **Python 3.7+**
- **Pandas**: Advanced data manipulation and analysis
- **NumPy**: Numerical computing and statistical operations
- **Matplotlib**: Data visualization and chart generation
- **Seaborn**: Statistical data visualization
- **JSON**: Structured data export

### Frontend (GUI Viewer)
- **Java 8+**
- **Swing**: Modern GUI framework with custom styling
- **JTable**: Advanced data display with custom renderers
- **JScrollPane**: Scrollable table interface
- **Custom Components**: Professional buttons, panels, and layouts

### Data Format
- **Input**: NBA player statistics in CSV format
- **Output**: Multiple formats (CSV, JSON) with comprehensive analysis

## 📊 MVP Scoring Algorithms

The application uses multiple sophisticated scoring algorithms:

### 1. Basic MVP Score (Original)
```
MVP_Score = (PTS × 0.4) + (AST × 0.2) + (TRB × 0.2) + (STL × 0.1) + (BLK × 0.1)
```

### 2. Advanced MVP Score (Efficiency-Based)
```
Advanced_Score = (PTS_PG × 0.35) + (AST_PG × 0.25) + (TRB_PG × 0.20) + 
                 (STL_PG × 0.10) + (BLK_PG × 0.10) + (TS% × 50) + (AST_TO_RATIO × 2)
```

### 3. Position-Adjusted Score
Position-specific weights that account for role expectations:
- **Point Guards**: Higher assist weight (40%)
- **Centers**: Higher rebound and block weights (35%, 25%)
- **Forwards**: Balanced scoring and rebounding (35%, 20%)

### 4. Team Impact Score
Measures performance relative to team averages:
```
Team_Impact = Basic_Score + ((Player_Stats - Team_Average) × 0.1)
```

### 5. Composite Score (Final Ranking)
Weighted combination of all methods:
```
Composite = (Basic × 0.3) + (Advanced × 0.3) + (Position × 0.2) + (Team_Impact × 0.2)
```

## 🚀 Quick Start

### Demo Script
To quickly see all the enhanced features in action:

```bash
python3 demo.py
```

This will run the enhanced analysis and showcase all the new features.

## 🚀 How to Run

### Prerequisites

1. **Python 3.7+** installed on your system
2. **Java 8 or higher** installed on your system
3. **NBA player statistics CSV file** (e.g., `NBA_2024_per_game.csv`)

### Step 1: Enhanced Data Processing (Python)

1. **Navigate to the project directory:**
   ```bash
   cd /path/to/NBA-MVP-RANKER
   ```

2. **Install Python dependencies:**
   ```bash
   pip install -r requirements.txt
   ```
   Or manually:
   ```bash
   pip install pandas numpy matplotlib seaborn
   ```

3. **Run the enhanced Python script:**
   ```bash
   # Basic usage
   python NBAMVPSORTEREnhanced.py
   
   # Advanced usage with options
   python NBAMVPSORTEREnhanced.py --input NBA_2024_per_game.csv --top 15 --no-viz
   ```

   **Command Line Options:**
   - `--input, -i`: Specify input CSV file (default: NBA_2024_per_game.csv)
   - `--top, -t`: Number of top players to display (default: 10)
   - `--no-viz`: Skip visualization generation
   - `--export-only`: Only export data, skip analysis display

   This will:
   - Load and preprocess NBA statistics
   - Calculate multiple MVP scoring algorithms
   - Generate comprehensive analysis
   - Display top MVP candidates with detailed statistics
   - Export results to CSV and JSON formats
   - Create data visualizations (if matplotlib/seaborn available)

### Step 2: Enhanced GUI Viewer (Java)

1. **Compile the enhanced Java application:**
   ```bash
   javac MVPViewerEnhanced.java
   ```

2. **Run the enhanced Java GUI:**
   ```bash
   java MVPViewerEnhanced
   ```

   **Features:**
   - Modern professional interface with dark theme
   - Real-time search and filtering capabilities
   - Advanced sorting by any statistical category
   - Export to CSV and JSON formats
   - Interactive data table with professional styling
   - Status dashboard with real-time statistics

## 📋 Input Data Format

The application expects NBA player statistics in CSV format with the following columns:
- `Player`: Player name
- `Team`: Team abbreviation
- `Pos`: Player position
- `G`: Games played
- `PTS`: Total points
- `AST`: Total assists
- `TRB`: Total rebounds
- `STL`: Total steals
- `BLK`: Total blocks
- `FGA`: Field goal attempts
- `FTA`: Free throw attempts
- `TOV`: Turnovers

## 🎯 Sample Output

### Enhanced Analysis Results
The enhanced application generates comprehensive analysis with multiple scoring methods:

```
🏀 TOP 10 MVP CANDIDATES (Composite Score)
================================================================================
 1. Luka Dončić              DAL  Score:  100.0 (2370P 686A 647R)
 2. Nikola Jokić             DEN  Score:   96.8 (2085P 708A 976R)
 3. Giannis Antetokounmpo    MIL  Score:   95.1 (2222P 476A 841R)
 4. Shai Gilgeous-Alexander  OKC  Score:   89.5 (2254P 465A 415R)
 5. Jalen Brunson            NYK  Score:   85.7 (2212P 519A 278R)
```

### Generated Files
- **mvp_candidates_enhanced.csv**: Complete dataset with all scoring methods
- **mvp_analysis.json**: Comprehensive analysis including team and position breakdowns
- **mvp_analysis_dashboard.png**: Visual dashboard with charts and graphs

### JSON Analysis Sample
```json
{
  "mvp_analysis": {
    "timestamp": "2024-01-15T10:30:00",
    "total_players": 150,
    "top_10_players": [
      {
        "player": "Luka Dončić",
        "team": "DAL",
        "pos": "PG",
        "mvp_score_composite": 100.0
      }
    ],
    "team_analysis": {
      "DAL": {"mvp_score_composite": {"mean": 45.2, "max": 100.0, "count": 8}}
    }
  }
}
```