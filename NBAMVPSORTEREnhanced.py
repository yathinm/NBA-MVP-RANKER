#!/usr/bin/env python3
"""
NBA MVP Ranker - Enhanced Version
Advanced statistical analysis and ranking system for NBA MVP candidates.

Features:
- Multiple scoring algorithms
- Advanced statistical metrics
- Team analysis
- Position-based analysis
- Export to multiple formats
- Data visualization
"""

import pandas as pd
import numpy as np
import json
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime
import argparse
import sys
from pathlib import Path

class NBAMVPAnalyzer:
    def __init__(self, csv_file='NBA_2024_per_game.csv'):
        """Initialize the NBA MVP Analyzer."""
        self.csv_file = csv_file
        self.df = None
        self.analysis_results = {}
        
    def load_data(self):
        """Load and preprocess NBA data."""
        try:
            print(f"Loading data from {self.csv_file}...")
            self.df = pd.read_csv(self.csv_file)
            
            # Clean data
            self.df = self.df.drop(columns=[col for col in ['R', 'Rk'] if col in self.df.columns])
            
            # Filter for players with at least 20 games
            self.df = self.df[self.df['G'] >= 20]
            
            # Fill missing values
            self.df = self.df.fillna(0)
            
            print(f"Loaded {len(self.df)} players with 20+ games")
            return True
            
        except FileNotFoundError:
            print(f"Error: File {self.csv_file} not found!")
            return False
        except Exception as e:
            print(f"Error loading data: {e}")
            return False
    
    def calculate_basic_mvp_score(self):
        """Calculate basic MVP score using the original formula."""
        self.df['MVP_Score_Basic'] = (
            self.df['PTS'] * 0.4 +
            self.df['AST'] * 0.2 +
            self.df['TRB'] * 0.2 +
            self.df['STL'] * 0.1 +
            self.df['BLK'] * 0.1
        )
    
    def calculate_advanced_mvp_score(self):
        """Calculate advanced MVP score with efficiency metrics."""
        # Calculate per-game averages
        self.df['PTS_PG'] = self.df['PTS'] / self.df['G']
        self.df['AST_PG'] = self.df['AST'] / self.df['G']
        self.df['TRB_PG'] = self.df['TRB'] / self.df['G']
        self.df['STL_PG'] = self.df['STL'] / self.df['G']
        self.df['BLK_PG'] = self.df['BLK'] / self.df['G']
        
        # Calculate efficiency metrics
        self.df['TS%'] = self.df['PTS'] / (2 * (self.df['FGA'] + 0.44 * self.df['FTA']))
        self.df['AST_TO_RATIO'] = self.df['AST'] / (self.df['TOV'] + 1)  # +1 to avoid division by zero
        
        # Advanced MVP Score with efficiency
        self.df['MVP_Score_Advanced'] = (
            self.df['PTS_PG'] * 0.35 +
            self.df['AST_PG'] * 0.25 +
            self.df['TRB_PG'] * 0.20 +
            self.df['STL_PG'] * 0.10 +
            self.df['BLK_PG'] * 0.10 +
            self.df['TS%'] * 50 +  # True shooting percentage bonus
            self.df['AST_TO_RATIO'] * 2  # Assist-to-turnover ratio bonus
        )
    
    def calculate_position_adjusted_score(self):
        """Calculate position-adjusted MVP score."""
        # Define position weights
        position_weights = {
            'PG': {'PTS': 0.3, 'AST': 0.4, 'TRB': 0.1, 'STL': 0.1, 'BLK': 0.1},
            'SG': {'PTS': 0.4, 'AST': 0.2, 'TRB': 0.1, 'STL': 0.2, 'BLK': 0.1},
            'SF': {'PTS': 0.35, 'AST': 0.2, 'TRB': 0.2, 'STL': 0.15, 'BLK': 0.1},
            'PF': {'PTS': 0.3, 'AST': 0.15, 'TRB': 0.3, 'STL': 0.1, 'BLK': 0.15},
            'C': {'PTS': 0.25, 'AST': 0.1, 'TRB': 0.35, 'STL': 0.05, 'BLK': 0.25}
        }
        
        self.df['MVP_Score_Position'] = 0
        
        for pos, weights in position_weights.items():
            mask = self.df['Pos'].str.contains(pos, na=False)
            self.df.loc[mask, 'MVP_Score_Position'] = (
                self.df.loc[mask, 'PTS'] * weights['PTS'] +
                self.df.loc[mask, 'AST'] * weights['AST'] +
                self.df.loc[mask, 'TRB'] * weights['TRB'] +
                self.df.loc[mask, 'STL'] * weights['STL'] +
                self.df.loc[mask, 'BLK'] * weights['BLK']
            )
    
    def calculate_team_impact_score(self):
        """Calculate team impact score based on team performance."""
        # Calculate team averages
        team_stats = self.df.groupby('Team').agg({
            'PTS': 'mean',
            'AST': 'mean',
            'TRB': 'mean',
            'STL': 'mean',
            'BLK': 'mean'
        }).reset_index()
        
        team_stats.columns = ['Team', 'Team_PTS_Avg', 'Team_AST_Avg', 'Team_TRB_Avg', 'Team_STL_Avg', 'Team_BLK_Avg']
        
        # Merge team stats
        self.df = self.df.merge(team_stats, on='Team', how='left')
        
        # Calculate team impact (how much better than team average)
        self.df['Team_Impact'] = (
            (self.df['PTS'] - self.df['Team_PTS_Avg']) * 0.4 +
            (self.df['AST'] - self.df['Team_AST_Avg']) * 0.2 +
            (self.df['TRB'] - self.df['Team_TRB_Avg']) * 0.2 +
            (self.df['STL'] - self.df['Team_STL_Avg']) * 0.1 +
            (self.df['BLK'] - self.df['Team_BLK_Avg']) * 0.1
        )
        
        # Normalize team impact score
        self.df['MVP_Score_Team_Impact'] = self.df['MVP_Score_Basic'] + (self.df['Team_Impact'] * 0.1)
    
    def calculate_composite_score(self):
        """Calculate composite MVP score combining all methods."""
        # Normalize all scores to 0-100 scale
        scores = ['MVP_Score_Basic', 'MVP_Score_Advanced', 'MVP_Score_Position', 'MVP_Score_Team_Impact']
        
        for score in scores:
            if score in self.df.columns:
                self.df[f'{score}_Normalized'] = (
                    (self.df[score] - self.df[score].min()) / 
                    (self.df[score].max() - self.df[score].min()) * 100
                )
        
        # Calculate composite score
        self.df['MVP_Score_Composite'] = (
            self.df['MVP_Score_Basic_Normalized'] * 0.3 +
            self.df['MVP_Score_Advanced_Normalized'] * 0.3 +
            self.df['MVP_Score_Position_Normalized'] * 0.2 +
            self.df['MVP_Score_Team_Impact_Normalized'] * 0.2
        )
    
    def generate_analysis(self):
        """Generate comprehensive analysis."""
        print("Generating comprehensive analysis...")
        
        # Calculate all scores
        self.calculate_basic_mvp_score()
        self.calculate_advanced_mvp_score()
        self.calculate_position_adjusted_score()
        self.calculate_team_impact_score()
        self.calculate_composite_score()
        
        # Sort by composite score
        self.df = self.df.sort_values('MVP_Score_Composite', ascending=False).reset_index(drop=True)
        
        # Generate analysis results
        self.analysis_results = {
            'timestamp': datetime.now().isoformat(),
            'total_players': len(self.df),
            'top_10_players': self.df.head(10)[['Player', 'Team', 'Pos', 'MVP_Score_Composite']].to_dict('records'),
            'team_summary': {
                'total_teams': self.df['Team'].nunique(),
                'avg_players_per_team': round(len(self.df) / self.df['Team'].nunique(), 1)
            },
            'position_summary': {
                'total_positions': self.df['Pos'].nunique(),
                'position_counts': self.df['Pos'].value_counts().to_dict()
            }
        }
        
        print("Analysis complete!")
    
    def display_results(self, top_n=10):
        """Display top MVP candidates."""
        print(f"\n🏀 TOP {top_n} MVP CANDIDATES (Composite Score)")
        print("=" * 80)
        
        top_players = self.df.head(top_n)
        
        for i, (_, player) in enumerate(top_players.iterrows(), 1):
            print(f"{i:2d}. {player['Player']:<25} {player['Team']:<4} "
                  f"Score: {player['MVP_Score_Composite']:6.1f} "
                  f"({player['PTS']:4.0f}P {player['AST']:3.0f}A {player['TRB']:3.0f}R)")
    
    def export_to_csv(self, filename='mvp_candidates_enhanced.csv'):
        """Export results to CSV."""
        export_columns = [
            'Player', 'Team', 'Pos', 'G', 'PTS', 'AST', 'TRB', 'STL', 'BLK',
            'MVP_Score_Basic', 'MVP_Score_Advanced', 'MVP_Score_Position', 
            'MVP_Score_Team_Impact', 'MVP_Score_Composite'
        ]
        
        self.df[export_columns].to_csv(filename, index=False)
        print(f"Results exported to {filename}")
    
    def export_to_json(self, filename='mvp_analysis.json'):
        """Export analysis results to JSON."""
        with open(filename, 'w') as f:
            json.dump(self.analysis_results, f, indent=2)
        print(f"Analysis exported to {filename}")
    
    def create_visualizations(self):
        """Create data visualizations."""
        print("Creating visualizations...")
        
        # Set style
        plt.style.use('seaborn-v0_8')
        fig, axes = plt.subplots(2, 2, figsize=(15, 12))
        fig.suptitle('NBA MVP Analysis Dashboard', fontsize=16, fontweight='bold')
        
        # 1. Top 10 MVP Candidates
        top_10 = self.df.head(10)
        axes[0, 0].barh(range(len(top_10)), top_10['MVP_Score_Composite'])
        axes[0, 0].set_yticks(range(len(top_10)))
        axes[0, 0].set_yticklabels([f"{name[:15]}..." if len(name) > 15 else name 
                                   for name in top_10['Player']])
        axes[0, 0].set_xlabel('Composite MVP Score')
        axes[0, 0].set_title('Top 10 MVP Candidates')
        axes[0, 0].invert_yaxis()
        
        # 2. Team MVP Score Distribution
        team_avg = self.df.groupby('Team')['MVP_Score_Composite'].mean().sort_values(ascending=False)
        axes[0, 1].bar(range(len(team_avg)), team_avg.values)
        axes[0, 1].set_xticks(range(len(team_avg)))
        axes[0, 1].set_xticklabels(team_avg.index, rotation=45, ha='right')
        axes[0, 1].set_ylabel('Average MVP Score')
        axes[0, 1].set_title('Average MVP Score by Team')
        
        # 3. Position Analysis
        pos_avg = self.df.groupby('Pos')['MVP_Score_Composite'].mean()
        axes[1, 0].pie(pos_avg.values, labels=pos_avg.index, autopct='%1.1f%%')
        axes[1, 0].set_title('MVP Score Distribution by Position')
        
        # 4. Statistical Correlation
        stats = ['PTS', 'AST', 'TRB', 'STL', 'BLK']
        correlations = [self.df['MVP_Score_Composite'].corr(self.df[stat]) for stat in stats]
        axes[1, 1].bar(stats, correlations)
        axes[1, 1].set_ylabel('Correlation with MVP Score')
        axes[1, 1].set_title('Statistical Correlations')
        axes[1, 1].set_ylim(0, 1)
        
        plt.tight_layout()
        plt.savefig('mvp_analysis_dashboard.png', dpi=300, bbox_inches='tight')
        print("Visualization saved as 'mvp_analysis_dashboard.png'")
        plt.show()
    
    def run_full_analysis(self):
        """Run complete analysis pipeline."""
        if not self.load_data():
            return False
        
        self.generate_analysis()
        self.display_results()
        self.export_to_csv()
        self.export_to_json()
        self.create_visualizations()
        
        return True

def main():
    """Main function with command line interface."""
    parser = argparse.ArgumentParser(description='NBA MVP Analyzer - Enhanced Version')
    parser.add_argument('--input', '-i', default='NBA_2024_per_game.csv',
                       help='Input CSV file (default: NBA_2024_per_game.csv)')
    parser.add_argument('--top', '-t', type=int, default=10,
                       help='Number of top players to display (default: 10)')
    parser.add_argument('--no-viz', action='store_true',
                       help='Skip visualization generation')
    parser.add_argument('--export-only', action='store_true',
                       help='Only export data, skip analysis display')
    
    args = parser.parse_args()
    
    # Check if input file exists
    if not Path(args.input).exists():
        print(f"Error: Input file '{args.input}' not found!")
        print("Please ensure the NBA statistics CSV file is in the current directory.")
        sys.exit(1)
    
    # Create analyzer and run analysis
    analyzer = NBAMVPAnalyzer(args.input)
    
    if not analyzer.load_data():
        sys.exit(1)
    
    analyzer.generate_analysis()
    
    if not args.export_only:
        analyzer.display_results(args.top)
    
    analyzer.export_to_csv()
    analyzer.export_to_json()
    
    if not args.no_viz:
        try:
            analyzer.create_visualizations()
        except ImportError:
            print("Warning: matplotlib/seaborn not available. Skipping visualizations.")
            print("Install with: pip install matplotlib seaborn")
    
    print("\n✅ Analysis complete! Check the generated files:")
    print("  - mvp_candidates_enhanced.csv")
    print("  - mvp_analysis.json")
    print("  - mvp_analysis_dashboard.png (if visualizations enabled)")

if __name__ == "__main__":
    main()
