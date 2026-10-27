"""Shared chart style for the experiment plots (light theme, PNG output)."""
import matplotlib.pyplot as plt

SURFACE = "#fcfcfb"
INK = "#0b0b0b"
INK_SECONDARY = "#52514e"
MUTED = "#898781"
GRID = "#e1e0d9"
AXIS = "#c3c2b7"
PREDICTED = "#898781"          # predictions are drawn as a neutral dashed line

# Categorical colours in fixed order (validated for colour-blind separation).
# Each algorithm keeps its colour in every chart.
SERIES = ["#2a78d6", "#eb6834", "#1baf7a", "#eda100", "#e87ba4", "#008300"]
MARKERS = ["o", "s", "^", "D", "v", "P"]   # second encoding, so colour is never alone


def apply():
    plt.rcParams.update({
        "figure.facecolor": SURFACE,
        "axes.facecolor": SURFACE,
        "savefig.facecolor": SURFACE,
        "axes.edgecolor": AXIS,
        "axes.labelcolor": INK_SECONDARY,
        "axes.titlecolor": INK,
        "axes.titlesize": 11,
        "axes.labelsize": 9,
        "axes.grid": True,
        "grid.color": GRID,
        "grid.linewidth": 0.6,
        "xtick.color": MUTED,
        "ytick.color": MUTED,
        "xtick.labelsize": 8,
        "ytick.labelsize": 8,
        "axes.spines.top": False,
        "axes.spines.right": False,
        "lines.linewidth": 2,
        "lines.markersize": 6,
        "legend.frameon": False,
        "legend.fontsize": 8,
        "legend.labelcolor": INK_SECONDARY,
        "font.size": 9,
    })


def label_line_ends(ax, ends, min_gap_pt=11):
    """Direct labels at the ends of lines, in ink (not the series colour).
    ends = [(x, y, text), ...]. Labels that would overlap are pushed apart vertically."""
    fig = ax.figure
    ax.margins(x=0.12)              # room on the right for the labels
    fig.tight_layout()
    fig.canvas.draw()
    to_pt = 72.0 / fig.dpi
    placed = []
    for x, y, text in ends:
        _, y_px = ax.transData.transform((x, y))
        placed.append([y_px * to_pt, x, y, text])
    placed.sort()
    for i in range(1, len(placed)):
        if placed[i][0] - placed[i - 1][0] < min_gap_pt:
            placed[i][0] = placed[i - 1][0] + min_gap_pt
    for target_pt, x, y, text in placed:
        _, y_px = ax.transData.transform((x, y))
        shift = target_pt - y_px * to_pt
        ax.annotate(text, (x, y), xytext=(8, shift), textcoords="offset points",
                    va="center", fontsize=8, color=INK_SECONDARY)
