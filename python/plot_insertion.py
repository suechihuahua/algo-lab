import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("data/insertion.csv")

# average the random trials for each n
avg = df[df["input"] == "random"].groupby("n")["comparisons"].mean()

n = np.array(avg.index)
harmonic = np.array([np.sum(1 / np.arange(1, k + 1)) for k in n])
predicted = n * (n - 1) / 4 + n - harmonic

print(pd.DataFrame({"measured": avg.values, "predicted": predicted,
                    "ratio": avg.values / predicted}, index=n))

plt.plot(n, predicted, "-", label="predicted n(n-1)/4 + n - H_n")
plt.plot(n, avg.values, "o", label="measured (mean of random trials)")
for kind in ["sorted", "reversed"]:
    rows = df[df["input"] == kind]
    plt.plot(rows["n"], rows["comparisons"], "x--", label=kind)

plt.xlabel("n (array size)")
plt.ylabel("key comparisons")
plt.title("Insertion sort: comparisons vs n")
plt.legend()
plt.savefig("figures/insertion_comparisons.png", dpi=150)
print("saved figures/insertion_comparisons.png")