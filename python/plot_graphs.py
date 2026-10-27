"""Plots data/graphs.csv (from graphs.Experiment): predicted vs measured operation counts,
and running time against V + E. Run from the repo root."""
import pandas as pd
import matplotlib.pyplot as plt

import chart_style as cs

ALGORITHMS = ["BFS", "DFS", "Dijkstra", "Bellman-Ford", "Prim", "Kruskal"]
COUNTED = ["Dijkstra", "Bellman-Ford", "Prim", "Kruskal"]
OPS_NAME = {"Dijkstra": "relaxation attempts", "Bellman-Ford": "relaxation attempts",
            "Prim": "key checks", "Kruskal": "edges examined"}


# What each algorithm's count should be on a connected undirected graph.
def predicted(algorithm, v, e):
    if algorithm in ("Dijkstra", "Prim"):
        return e                    # each edge is tried once, from whichever end finishes first
    if algorithm == "Bellman-Ford":
        return (v - 1) * 2 * e      # V-1 rounds, both directions of every edge
    if algorithm == "Kruskal":
        return e                    # upper bound: it stops once V-1 edges are in the tree
    raise ValueError(algorithm)


def main():
    cs.apply()
    df = pd.read_csv("data/graphs.csv")
    colour = {alg: cs.SERIES[i] for i, alg in enumerate(ALGORITHMS)}
    marker = {alg: cs.MARKERS[i] for i, alg in enumerate(ALGORITHMS)}

    # Table view.
    rows = []
    for alg in COUNTED:
        g = df[df["algorithm"] == alg].groupby("V")[["E", "ops"]].mean()
        for v, r in g.iterrows():
            rows.append((alg, v, r["E"], r["ops"], predicted(alg, v, r["E"])))
    table = pd.DataFrame(rows, columns=["algorithm", "V", "E", "measured", "predicted"])
    table["ratio"] = table["measured"] / table["predicted"]
    print(table.to_string(index=False, float_format=lambda v: f"{v:,.3f}"))

    # 1) Operation counts: one small chart per counted algorithm.
    fig, axes = plt.subplots(2, 2, figsize=(9, 7))
    for ax, alg in zip(axes.flat, COUNTED):
        g = df[df["algorithm"] == alg].groupby("V")[["E", "ops"]].mean()
        pred = [predicted(alg, v, e) for v, e in zip(g.index, g["E"])]
        ax.plot(g.index, pred, "--", color=cs.PREDICTED, linewidth=1.5, label="predicted")
        ax.plot(g.index, g["ops"], marker[alg], color=colour[alg], label="measured (mean of 3)")
        ax.set_xscale("log", base=2)
        ax.set_yscale("log")
        ax.set_title(alg)
        ax.set_xlabel("V (vertices, E = 5V)")
        ax.set_ylabel(OPS_NAME[alg])
        ax.legend(loc="upper left")
    fig.suptitle("Operation counts on random graphs: predicted vs measured", color=cs.INK)
    fig.tight_layout()
    fig.savefig("figures/graphs_ops.png", dpi=150)

    # 2) Time against V + E, all algorithms on one chart.
    fig, ax = plt.subplots(figsize=(8, 5))
    ends = []
    for alg in ALGORITHMS:
        sub = df[df["algorithm"] == alg].copy()
        sub["size"] = sub["V"] + sub["E"]
        g = sub.groupby("size")["time_ns"].median() / 1e6
        ax.plot(g.index, g.values, "-" + marker[alg], color=colour[alg], label=alg)
        ends.append((g.index[-1], g.values[-1], alg))
    ax.set_xscale("log", base=2)
    ax.set_yscale("log")
    cs.label_line_ends(ax, ends)
    ax.set_xlabel("V + E")
    ax.set_ylabel("time (ms, median of 3)")
    ax.set_title("Running time on random graphs (Bellman-Ford only up to V = 4000)")
    ax.legend(loc="upper left")
    fig.tight_layout()
    fig.savefig("figures/graphs_time.png", dpi=150)
    print("saved figures/graphs_ops.png and figures/graphs_time.png")


if __name__ == "__main__":
    main()
