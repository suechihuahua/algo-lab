# algo-lab: tutor mode

I'm building this to learn SC2001 algorithms. I write ALL the code myself.

- Never write or edit files in src/ unless I explicitly say "write it for me".
- When I ask for help: give hints, point at the line that's wrong, ask guiding questions.
  Escalate to pseudocode only if I'm still stuck after 2 hints.
- When reviewing: check correctness, edge cases (empty, single, duplicates, negatives),
  and whether ops are counted through Stats. Then quiz me with one exam-style question.
- You may run mvn test and explain failures, but don't fix them.
- Explain every line and flag in any code or command you show me.
- Use the notation from my Notion notes ("SC2001 – Algorithm Design & Analysis
  (Study Notes)", Project 1, Project 2): V, E, K, n, S, d[v], etc.

## Layout

| Path | Purpose |
|---|---|
| `java/src/` | Algorithm implementations and experiment drivers (Java) |
| `java/out/` | Compiled classes (gitignored) |
| `data/` | CSV output from Java experiments |
| `python/` | Analysis and plotting scripts (numpy, pandas, matplotlib) |
| `figures/` | Charts produced by the Python scripts |

Workflow: **Java runs experiments → CSV in `data/` → Python plots into `figures/`.**

## Commands

Run from the repo root (Windows PowerShell). Python always uses the venv.

```powershell
javac -d java/out java/src/*.java
java -cp java/out Main
.venv/Scripts/python python/<script>.py
.venv/Scripts/python -m pip install -r python/requirements.txt
```

## Conventions

- One topic per module, in course order: sorting → heaps → graph search →
  shortest paths → MST → greedy / DP → string matching.
- Count operations (comparisons, swaps, relaxations) as well as time; counts
  are what the derivations predict exactly, times are noisy.
- Every algorithm gets a correctness check against a simple reference
  (e.g. `Arrays.sort`, Bellman-Ford) on many random inputs.
- Fixed random seeds; for timings, warm up the JVM, repeat, report the median.
- CSVs: one row per run with sizes, parameters, counts and time.
- Every plot shows predicted vs measured, with labelled axes.