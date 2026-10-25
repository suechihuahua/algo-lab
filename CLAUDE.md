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
- After each algorithm is finished, update the Progress checklist below and
  remind me what's left.
- Always point out the relevant best coding practices and explain why they
  exist, so I learn to adopt them. When unsure what current practice is,
  look it up (official docs first) instead of guessing, and cite the source.

## Progress checklist

Quiz 1 is fill-in-the-blank hand-tracing, so each algorithm needs a trace
mode that matches the lecture's conventions. An algorithm is done when it has:
implementation · correctness tests · trace output · trace checked against
my notes/tutorials · committed.

**Experiments and plots are paused until my exams are over** (decided 2026-10-20).
Focus on implementation, correctness tests and tracing. Don't suggest Experiment/CSV/plot
work until I say exams are done.

Quiz 1 algorithms:
- [x] Insertion sort (experiment + plot done too)
- [x] Merge sort (mid = lo + (hi−lo)/2, ties taken from the left)
- [x] Quicksort. Lecture = Strategy 1 (Lomuto): pivot swapped to A[end],
      j grows the "< p" part, `A[i] < p` strict. Default pivot = last element (matches
      the notes' worked tree). Recurse on [lo, mid−1] and [mid+1, hi]: the slide's
      Quicksort(A, mid, end) can infinite-loop.
- [ ] Heaps ← in progress. Conventions from my notes (chosen 2026-10-25): 0-based,
      children 2i+1 / 2i+2, parent (i−1)/2; max-heap; build bottom-up from n/2−1 down
      to 0 with maxHeapify (sift-down); insert = add at end + sift-up; extract = last
      element to root + maxHeapify. Code lives in `heaps/Heap.java`.
- [ ] Heapsort (`sorting/HeapSort.java`, uses `heaps.Heap`; build max-heap, then swap
      root with end, shrink, maxHeapify(0))
- [ ] BFS (confirm neighbour order)
- [ ] DFS
- [ ] Dijkstra
- [ ] Bellman-Ford
- [ ] Prim
- [ ] Kruskal (confirm tie-breaking for equal weights)
- [ ] Union-Find (confirm union by rank/size and path compression)

After the quiz: greedy, DP (LCS, 0/1 knapsack, coin change), string matching.

## Layout

| Path | Purpose |
|---|---|
| `java/src/common/` | Shared helpers: `Stats`, `ArrayGen` (package `common`) |
| `java/src/<topic>/` | One package per topic, e.g. `sorting/`: algorithms, `Main` (tests), `Trace`, `Experiment` |
| `java/out/` | Compiled classes (gitignored) |
| `data/` | CSV output from Java experiments |
| `python/` | Analysis and plotting scripts (numpy, pandas, matplotlib) |
| `figures/` | Charts produced by the Python scripts |

Workflow: **Java runs experiments → CSV in `data/` → Python plots into `figures/`.**

## Commands

Run from the repo root (Windows PowerShell). Python always uses the venv.

```powershell
javac -d java/out (Get-ChildItem -Recurse java/src -Filter *.java).FullName
java -cp java/out sorting.Main          # correctness tests
java -cp java/out sorting.Trace [seed]  # random trace, length 2-8
java -cp java/out sorting.Experiment    # writes data/insertion.csv
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