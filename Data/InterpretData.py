import matplotlib.pyplot as plt
import numpy as np
import sys

def run():
    print("Generating plot...")
    xpoints = np.array([0, 6])
    ypoints = np.array([0, 250])

    plt.plot(xpoints, ypoints)
    plt.xlabel("time");
    plt.ylabel("score");
    # Save to a file in your project folder instead of dumping to console
    plt.savefig("output_plot.png")
    print("Plot saved to output_plot.png")

if __name__ == "__main__":
    run()