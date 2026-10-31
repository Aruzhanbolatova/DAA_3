# Benchmark results

Seed: 42. Five timed repetitions after ten warm-up runs per configuration.

| Algorithm | Graph | V | E | Mean ns | Edge scans | Comparisons | Find calls | Union calls | Parent traversals |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| Kruskal | connected-undirected | 10 | 30 | 14566.8 | 14 | 107 | 44 | 14 | 16 |
| Prim | connected-undirected | 10 | 30 | 27491.8 | 90 | 203 | 0 | 0 | 0 |
| Dijkstra | connected-undirected | 10 | 30 | 17291.6 | 90 | 82 | 0 | 0 | 0 |
| Kosaraju | DAG | 10 | 30 | 12608.0 | 90 | 0 | 0 | 0 | 0 |
| Kosaraju | cyclic | 10 | 30 | 10424.8 | 90 | 0 | 0 | 0 | 0 |
| TopologicalSort | DAG | 10 | 30 | 8083.6 | 60 | 0 | 0 | 0 | 0 |
| TopologicalSort | cyclic | 10 | 30 | 3383.4 | 30 | 0 | 0 | 0 | 0 |
| Kruskal | connected-undirected | 100 | 500 | 154400.2 | 164 | 3827 | 582 | 164 | 254 |
| Prim | connected-undirected | 100 | 500 | 191116.8 | 1500 | 7697 | 0 | 0 | 0 |
| Dijkstra | connected-undirected | 100 | 500 | 197908.2 | 1500 | 2403 | 0 | 0 | 0 |
| Kosaraju | DAG | 100 | 500 | 63125.0 | 1500 | 0 | 0 | 0 | 0 |
| Kosaraju | cyclic | 100 | 500 | 59208.4 | 1500 | 0 | 0 | 0 | 0 |
| TopologicalSort | DAG | 100 | 500 | 63799.8 | 1000 | 0 | 0 | 0 | 0 |
| TopologicalSort | cyclic | 100 | 500 | 27091.8 | 500 | 0 | 0 | 0 | 0 |
| Kruskal | connected-undirected | 1000 | 5000 | 727667.0 | 3356 | 50369 | 12879 | 3356 | 6167 |
| Prim | connected-undirected | 1000 | 5000 | 919591.8 | 15000 | 110958 | 0 | 0 | 0 |
| Dijkstra | connected-undirected | 1000 | 5000 | 480700.4 | 15000 | 36698 | 0 | 0 | 0 |
| Kosaraju | DAG | 1000 | 5000 | 262824.6 | 15000 | 0 | 0 | 0 | 0 |
| Kosaraju | cyclic | 1000 | 5000 | 290016.4 | 15000 | 0 | 0 | 0 | 0 |
| TopologicalSort | DAG | 1000 | 5000 | 152475.0 | 10000 | 0 | 0 | 0 | 0 |
| TopologicalSort | cyclic | 1000 | 5000 | 56016.6 | 5042 | 0 | 0 | 0 | 0 |

Java: 25.0.1; VM: OpenJDK 64-Bit Server VM; OS: Mac OS X; architecture: aarch64.
