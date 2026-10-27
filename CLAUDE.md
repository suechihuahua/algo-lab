# algo-lab: tutor mode

I'm building this to learn SC2001 algorithms.

**Change on 2026-10-27:** I asked Claude to write the remaining implementations (DFS fix,
Dijkstra, Bellman-Ford, Union-Find, Kruskal, Prim) and the experiment drivers, so I can
focus on hand-tracing for the quiz. The sorting and heap code, plus BFS, is still mine.
For new topics, ask me whether I want to write it myself or have it written.

- Never write or edit files in src/ unless I explicitly ask for it ("write it for me", "add it in").
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

Experiments: drivers exist for every algorithm (added 2026-10-27 at my request). My focus
until exams is still tracing, so don't push extra experiment work unless I ask.

Quiz 1 algorithms:
- [x] Insertion sort (experiment + plot done too)
- [x] Merge sort (mid = lo + (hi−lo)/2, ties taken from the left)
- [x] Quicksort. Lecture = Strategy 1 (Lomuto): pivot swapped to A[end],
      j grows the "< p" part, `A[i] < p` strict. Default pivot = last element (matches
      the notes' worked tree). Recurse on [lo, mid−1] and [mid+1, hi]: the slide's
      Quicksort(A, mid, end) can infinite-loop.
- [x] Heaps (insert/extractMax + `heaps.Trace` tracer). Conventions from my notes (chosen 2026-10-25): 0-based,
      children 2i+1 / 2i+2, parent (i−1)/2; max-heap; build bottom-up from n/2−1 down
      to 0 with maxHeapify (sift-down); insert = add at end + sift-up; extract = last
      element to root + maxHeapify. Code lives in `heaps/Heap.java`.
- [x] Heapsort (`sorting/HeapSort.java`, uses `heaps.Heap`; build max-heap, then swap
      root with end, shrink, maxHeapify(0))
- [x] BFS. Conventions from my notes (G2): vertices 0..n−1, undirected adjacency list,
      neighbours visited in **increasing numeric order**; queue; a vertex gets dist/par when
      **discovered** (enqueued). Notes' example (edges 0-1 0-3 0-4 1-2 1-5 2-3 3-4 4-5):
      BFS from 0 visits 0,1,3,4,2,5 (dist 0,1,1,1,2,2).
- [x] DFS (recursive, same order; 3 states UNVISITED/ACTIVE/FINISHED; back edge = neighbour
      still ACTIVE and not the parent). Notes' example: 0,1,2,3,4,5, back edges 0-3, 0-4, 1-5.
- [x] Dijkstra (`graphs/Dijkstra.java`). Remove the smallest dist; ties → smallest vertex.
      Matches the notes' table on the a–j graph from a (d=9 via b, j=13, …).
- [x] Bellman-Ford. |V|−1 rounds over the edge list in insertion order (undirected edges
      both ways), then one check round for a negative cycle. Fixes the notes' Dijkstra
      counterexample (0→1 2, 0→2 1, 1→2 −3 gives −1).
- [x] Prim. Key = cheapest single edge to the tree; ties → smallest vertex. Notes' order from a:
      a c e f h g d i b j, MST weight 22.
- [x] Kruskal. Stable sort by weight, so equal weights keep insertion order; the notes'
      example lists edges in the notes' sorted order. Skips only h-i; MST weight 22.
- [x] Union-Find (`graphs/UnionFind.java`). Notes' version: union by rank (equal ranks:
      y's root wins, rank+1) + path compression.

All 12 Quiz 1 algorithms have implementation, tests, a trace checked against the notes,
and a tracer. Practise with `graphs.Trace`, `graphs.UnionFindTrace`, `heaps.Trace`, `sorting.Trace`.

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
java -cp java/out sorting.Main           # sorting tests
java -cp java/out graphs.Main            # graph + union-find tests
java -cp java/out sorting.Trace          # interactive: insertion / merge / quick / heap sort
java -cp java/out heaps.Trace            # interactive: build / insert / extract max-heap
java -cp java/out graphs.Trace           # interactive: BFS / DFS / Dijkstra / Bellman-Ford / Prim / Kruskal
java -cp java/out graphs.UnionFindTrace  # interactive: union / find
java -cp java/out sorting.Experiment     # writes data/sorting.csv
java -cp java/out graphs.Experiment      # writes data/graphs.csv
.venv/Scripts/python python/plot_sorting.py   # figures/sorting_*.png
.venv/Scripts/python python/plot_graphs.py    # figures/graphs_*.png
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