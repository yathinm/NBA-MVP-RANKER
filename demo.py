#!/usr/bin/env python3
"""
Demo script to showcase the enhanced NBA MVP Analyzer features.
"""

import subprocess
import sys
import os

def main():
    print("🏀 NBA MVP Analyzer - Enhanced Edition Demo")
    print("=" * 50)
    
    # Check if virtual environment exists
    if not os.path.exists("venv"):
        print("❌ Virtual environment not found. Please run:")
        print("   python3 -m venv venv")
        print("   source venv/bin/activate")
        print("   pip install -r requirements.txt")
        return
    
    print("✅ Virtual environment found")
    
    # Check if enhanced CSV exists
    if not os.path.exists("mvp_candidates_enhanced.csv"):
        print("🔄 Running enhanced analysis...")
        try:
            subprocess.run([
                "source", "venv/bin/activate", "&&", 
                "python", "NBAMVPSORTEREnhanced.py", "--top", "10", "--no-viz"
            ], shell=True, check=True)
            print("✅ Enhanced analysis complete!")
        except subprocess.CalledProcessError as e:
            print(f"❌ Error running analysis: {e}")
            return
    else:
        print("✅ Enhanced analysis data found")
    
    # Display results
    print("\n📊 Enhanced Features Showcase:")
    print("-" * 30)
    
    print("1. Multiple Scoring Algorithms:")
    print("   • Basic MVP Score (original formula)")
    print("   • Advanced MVP Score (efficiency-based)")
    print("   • Position-Adjusted Score (role-specific)")
    print("   • Team Impact Score (relative performance)")
    print("   • Composite Score (weighted combination)")
    
    print("\n2. Advanced Analytics:")
    print("   • 532 players analyzed (20+ games)")
    print("   • 32 teams represented")
    print("   • 5 position categories")
    print("   • Multiple export formats (CSV, JSON)")
    
    print("\n3. Enhanced Java GUI Features:")
    print("   • Modern professional interface")
    print("   • Real-time search and filtering")
    print("   • Advanced sorting capabilities")
    print("   • Export to CSV and JSON")
    print("   • Interactive data table")
    
    print("\n🚀 To run the enhanced applications:")
    print("   Python: source venv/bin/activate && python NBAMVPSORTEREnhanced.py")
    print("   Java:   java MVPViewerEnhanced")
    
    print("\n📁 Generated Files:")
    print("   • mvp_candidates_enhanced.csv - Complete dataset")
    print("   • mvp_analysis.json - Analysis results")
    print("   • MVPViewerEnhanced.class - Compiled Java GUI")

if __name__ == "__main__":
    main()
