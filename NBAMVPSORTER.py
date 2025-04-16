import pandas as pd

df = pd.read_csv('NBA_2024_per_game.csv')

# Drop unnecessary columns like Rk or R
df = df.drop(columns=[col for col in ['R', 'Rk'] if col in df.columns])

# Filter for players with at least 20 games
df = df[df['G'] >= 20]

# Fill missing values
df = df.fillna(0)

# MVP formula
df['MVP_Score'] = (
    df['PTS'] * 0.4 +
    df['AST'] * 0.2 +
    df['TRB'] * 0.2 +
    df['STL'] * 0.1 +
    df['BLK'] * 0.1
)

# Show top MVP candidates
top_mvp = df[['Player', 'Team', 'PTS', 'AST', 'TRB', 'STL', 'BLK', 'MVP_Score']].sort_values(
    by='MVP_Score', ascending=False
)

print(top_mvp.head(10))

top_mvp.head(10).to_csv("mvp_candidates.csv", index=False) 
