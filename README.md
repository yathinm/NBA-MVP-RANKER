NBA MVP Ranker

Python-based tool that analyzes NBA player statistics using the Pandas library and ranks the top MVP candidates based on a custom scoring formula.

This project loads per-game NBA player stats (CSV format), processes key performance metrics, and computes an MVP Score using a weighted formula. The top candidates are then ranked and displayed.

You can customize the formula, filters


- Load player stats from a CSV file
- Filter out low-minute or low-game players
- Calculate a custom MVP score using key stats:
  - Points per game (PTS)
  - Assists (AST)
  - Rebounds (TRB)
  - Steals (STL)
  - Blocks (BLK)
- Sort and display top MVP candidates

MVP Scoring Formula

MVP_Score = (PTS * 0.4) + (AST * 0.2) + (TRB * 0.2) + (STL * 0.1) + (BLK * 0.1)
