import sqlite3
import os
import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import numpy as np

def get_db_connection():
    db_path = os.path.join(os.getcwd(), "database.db")
    return sqlite3.connect(db_path)

def run_gui():
    root = tk.Tk()
    root.title("Hackathon Pro: Analytics & Rankings")
    root.geometry("1000x700")

    # --- Data Fetching ---
    try:
        conn = get_db_connection()
        cursor = conn.cursor()

        # 1. Trend Data (Last 10 games)
        cursor.execute("SELECT testTime, score1, score2, score3 FROM gameData ORDER BY id DESC LIMIT 10")
        recent_data = cursor.fetchall()[::-1] # Reverse to show chronological order

        # 2. Rankings (Sum of all scores per user)
        cursor.execute("""
                       SELECT username, SUM(score1 + score2 + score3) as total
                       FROM gameData
                       GROUP BY username
                       ORDER BY total DESC
                           LIMIT 5
                       """)
        rankings = cursor.fetchall()

        # 3. Averages for Radar Chart
        cursor.execute("SELECT AVG(score1), AVG(score2), AVG(score3) FROM gameData")
        avg_scores = cursor.fetchone()

        conn.close()
    except Exception as e:
        print(f"DB Error: {e}")
        recent_data, rankings, avg_scores = [], [], [0,0,0]

    # --- UI Layout ---
    main_frame = tk.Frame(root)
    main_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

    # Left Side: Graphs
    graph_frame = tk.Frame(main_frame)
    graph_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

    # Right Side: Leaderboard
    leader_frame = tk.LabelFrame(main_frame, text=" 🏆 Global Leaderboard ", font=("Arial", 12, "bold"))
    leader_frame.pack(side=tk.RIGHT, fill=tk.Y, padx=10)

    for i, (name, score) in enumerate(rankings):
        tk.Label(leader_frame, text=f"{i+1}. {name}: {score} pts", font=("Arial", 11)).pack(anchor="w", pady=5, padx=10)

    # --- Plotting ---
    fig = plt.figure(figsize=(8, 10))

    # Graph 1: Score Progression (Line)
    ax1 = fig.add_subplot(211)
    if recent_data:
        times = [r[0] for r in recent_data]
        ax1.plot(times, [r[1] for r in recent_data], 'o-', label="Reaction")
        ax1.plot(times, [r[2] for r in recent_data], 's-', label="Memory")
        ax1.plot(times, [r[3] for r in recent_data], '^-', label="Typing")
        ax1.legend()
    ax1.set_title("Performance History")
    ax1.set_ylabel("Score")

    # Graph 2: Skill Distribution (Bar Chart of Averages)
    ax2 = fig.add_subplot(212)
    categories = ['Reaction', 'Memory', 'Typing']
    ax2.bar(categories, avg_scores, color=['#3498db', '#e74c3c', '#2ecc71'])
    ax2.set_title("Average Skill Strengths")

    fig.tight_layout()

    canvas = FigureCanvasTkAgg(fig, master=graph_frame)
    canvas.draw()
    canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

    root.mainloop()

if __name__ == "__main__":
    run_gui()