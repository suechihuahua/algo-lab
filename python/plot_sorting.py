"""Plots data/sorting.csv (from sorting.Experiment): predicted vs measured comparisons,
and running time, for all four sorts on random input. Run from the repo root."""
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

import chart_style as cs

ALGORITHMS = ["insertion", "merge", "quick", "heap"]


def harmonic(n):
    return np.array([np.sum(1.0 / np.arange(1, k + 1)) for k in n])


# Average-case comparison counts on random input (n distinct keys).
def predicted(algorithm, n):
    n = np.asarray(n, dtype=float)
    if algorithm == "insertion":
        return n * (n - 1) / 4 + n - harmonic(n)            # notes: sum of k/2 + k/(k+1)
    if algorithm == "merge":
        return n * np.log2(n) - 1.2645 * n                  # n log n minus the average merge saving
    if algorithm == "quick":
        return 2 * (n + 1) * harmonic(n) - 4 * n            # Lomuto, m-1 comparisons per partition
    if algorithm == "heap":
        return 2 * n * np.log2(n)                           # leading term: 2 per sift-down level
    raise ValueError(algorithm)


def main():
    cs.apply()
    df = pd.read_csv("data/sorting.csv")
    rnd = df[df["input"] == "random"]

    # Table view: measured / predicted for every algorithm and size.
    rows = []
    for alg in ALGORITHMS:
        g = rnd[rnd["algorithm"] == alg].groupby("n")["comparisons"].mean()
        for n, measured in g.items():
            rows.append((alg, n, measured, float(predicted(alg, [n])[0])))
    table = pd.DataFrame(rows, columns=["algorithm", "n", "measured", "predicted"])
    table["ratio"] = table["measured"] / table["predicted"]
    print(table.to_string(index=False, float_format=lambda v: f"{v:,.3f}"))

    # 1) Comparisons: one small chart per algorithm (one measured series + its prediction).
    fig, axes = plt.subplots(2, 2, figsize=(9, 7))
    for i, alg in enumerate(ALGORITHMS):
        ax = axes.flat[i]
        g = rnd[rnd["algorithm"] == alg].groupby("n")["comparisons"].mean()
        n = np.array(g.index)
        ax.plot(n, predicted(alg, n), "--", color=cs.PREDICTED, linewidth=1.5, label="predicted")
        ax.plot(n, g.values, cs.MARKERS[i], color=cs.SERIES[i], label="measured (mean of 5)")
        ax.set_xscale("log", base=2)
        ax.set_yscale("log")
        ax.set_title(f"{alg} sort")
        ax.set_xlabel("n (array size)")
        ax.set_ylabel("key comparisons")
        ax.legend(loc="upper left")
    fig.suptitle("Comparisons on random input: predicted vs measured", color=cs.INK)
    fig.tight_layout()
    fig.savefig("figures/sorting_comparisons.png", dpi=150)

    # 2) Time: all four on one chart (same units, one axis), median of 5 runs.
    fig, ax = plt.subplots(figsize=(8, 5))
    ends = []
    for i, alg in enumerate(ALGORITHMS):
        g = rnd[rnd["algorithm"] == alg].groupby("n")["time_ns"].median() / 1e6
        ax.plot(g.index, g.values, "-" + cs.MARKERS[i], color=cs.SERIES[i], label=alg)
        ends.append((g.index[-1], g.values[-1], alg))
    ax.set_xscale("log", base=2)
    ax.set_yscale("log")
    cs.label_line_ends(ax, ends)
    ax.set_xlabel("n (array size)")
    ax.set_ylabel("time (ms, median of 5)")
    ax.set_title("Running time on random input")
    ax.legend(loc="upper left")
    fig.tight_layout()
    fig.savefig("figures/sorting_time.png", dpi=150)
    print("saved figures/sorting_comparisons.png and figures/sorting_time.png")


if __name__ == "__main__":
    main()
