import sqlite3
import os
import tkinter as tk
from tkinter import messagebox
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import numpy as np

def run_gui():
    # 1. Database Logic
    db_path = os.path.join(os.getcwd(), "Data", "database.db")
    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        cursor.execute("SELECT time, score FROM game_stats ORDER BY time ASC")
        data = cursor.fetchall()
        conn.close()
    except Exception as e:
        print(f"Error: {e}")
        data = []

    # 2. Setup Tkinter Window
    root = tk.Tk()
    root.title("Game Data Analytics")
    root.geometry("600x500")

    # Add a Label
    label = tk.Label(root, text="Live Game Statistics", font=("Arial", 16, "bold"))
    label.pack(pady=10)

    # 3. Create the Plot
    fig, ax = plt.subplots(figsize=(5, 4), dpi=100)
    if data:
        times, scores = zip(*data)
        ax.plot(times, scores, marker='o', color='teal')
    else:
        ax.text(0.5, 0.5, "No Data Available", ha='center')

    ax.set_xlabel("Time")
    ax.set_ylabel("Score")

    # 4. Embed Plot in Tkinter
    canvas = FigureCanvasTkAgg(fig, master=root)
    canvas.draw()
    canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

    # Add a Close Button
    btn = tk.Button(root, text="Close Dashboard", command=root.destroy, bg="#ff5555", fg="white")
    btn.pack(pady=10)

    print("Tkinter window launched.")
    root.mainloop()

if __name__ == "__main__":
    run_gui()