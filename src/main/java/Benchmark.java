import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;

public class Benchmark {
    private static volatile Object sink;
    private static final long SEED = 42;
    private static final int REPEATS = 5;
    private static final int WARMUPS = 10;

    private static void measure(String name, String kind, int v, int e,
                                Function<OperationStats, Object> algorithm,
                                StringBuilder raw, StringBuilder summary, StringBuilder table) {
        for (int run = 0; run < WARMUPS; run++) sink = algorithm.apply(new OperationStats());
        long totalTime = 0, edges = 0, comparisons = 0, finds = 0, unions = 0, traversals = 0;
        for (int run = 1; run <= REPEATS; run++) {
            OperationStats stats = new OperationStats();
            long start = System.nanoTime();
            Object result = algorithm.apply(stats);
            long elapsed = System.nanoTime() - start;
            sink = result;
            raw.append(String.format(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                    name, kind, v, e, run, elapsed, stats.edgeExaminations, stats.comparisons,
                    stats.findCalls, stats.unionCalls, stats.parentTraversals));
            totalTime += elapsed; edges += stats.edgeExaminations; comparisons += stats.comparisons;
            finds += stats.findCalls; unions += stats.unionCalls; traversals += stats.parentTraversals;
        }
        summary.append(String.format(Locale.ROOT, "%s,%s,%d,%d,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f%n",
                name, kind, v, e, totalTime / 5.0, edges / 5.0, comparisons / 5.0,
                finds / 5.0, unions / 5.0, traversals / 5.0));
        table.append(String.format(Locale.ROOT, "| %s | %s | %d | %d | %.1f | %.0f | %.0f | %.0f | %.0f | %.0f |%n",
                name, kind, v, e, totalTime / 5.0, edges / 5.0, comparisons / 5.0,
                finds / 5.0, unions / 5.0, traversals / 5.0));
    }

    private static long weight(ArrayList<Edge> edges) {
        long total = 0;
        for (Edge edge : edges) total += edge.weight;
        return total;
    }

    private static Object expectCycle(DirectedGraph graph, OperationStats stats) {
        try {
            TopologicalSort.sort(graph, stats);
        } catch (IllegalArgumentException expected) {
            return Boolean.TRUE;
        }
        throw new AssertionError("Generated cycle not detected");
    }

    public static void main(String[] args) throws IOException {
        StringBuilder raw = new StringBuilder("algorithm,graph,V,E,run,time_ns,edge_examinations,comparisons,find_calls,union_calls,parent_traversals\n");
        StringBuilder summary = new StringBuilder("algorithm,graph,V,E,average_ns,edge_examinations,comparisons,find_calls,union_calls,parent_traversals\n");
        StringBuilder table = new StringBuilder("# Benchmark results\n\n");
        table.append("Seed: 42. Five timed repetitions after ten warm-up runs per configuration.\n\n");
        table.append("| Algorithm | Graph | V | E | Mean ns | Edge scans | Comparisons | Find calls | Union calls | Parent traversals |\n");
        table.append("|---|---|---:|---:|---:|---:|---:|---:|---:|---:|\n");
        int[][] configurations = {{10, 30}, {100, 500}, {1000, 5000}};
        for (int[] configuration : configurations) {
            int v = configuration[0], e = configuration[1];
            Graph undirected = GraphGenerator.connected(v, e, SEED);
            DirectedGraph dag = GraphGenerator.directed(v, e, false, SEED);
            DirectedGraph cyclic = GraphGenerator.directed(v, e, true, SEED);
            ArrayList<Edge> k = Kruskal.findMST(undirected), p = Prim.findMST(undirected);
            if (k.size() != v - 1 || p.size() != v - 1 || weight(k) != weight(p)) {
                throw new AssertionError("MST benchmark correctness");
            }
            TopologicalSort.sort(dag);
            expectCycle(cyclic, new OperationStats());
            measure("Kruskal", "connected-undirected", v, e, s -> Kruskal.findMST(undirected, s), raw, summary, table);
            measure("Prim", "connected-undirected", v, e, s -> Prim.findMST(undirected, s), raw, summary, table);
            measure("Dijkstra", "connected-undirected", v, e, s -> Dijkstra.shortestPaths(undirected, 0, s), raw, summary, table);
            measure("Kosaraju", "DAG", v, e, s -> Kosaraju.findSCC(dag, s), raw, summary, table);
            measure("Kosaraju", "cyclic", v, e, s -> Kosaraju.findSCC(cyclic, s), raw, summary, table);
            measure("TopologicalSort", "DAG", v, e, s -> TopologicalSort.sort(dag, s), raw, summary, table);
            measure("TopologicalSort", "cyclic", v, e, s -> expectCycle(cyclic, s), raw, summary, table);
        }
        table.append("\nJava: ").append(System.getProperty("java.version"));
        table.append("; VM: ").append(System.getProperty("java.vm.name"));
        table.append("; OS: ").append(System.getProperty("os.name"));
        table.append("; architecture: ").append(System.getProperty("os.arch")).append(".\n");
        Path directory = Path.of("results", "tables");
        Files.createDirectories(directory);
        Files.writeString(directory.resolve("runs.csv"), raw);
        Files.writeString(directory.resolve("benchmark.csv"), summary);
        Files.writeString(directory.resolve("results.md"), table);
        System.out.println(table);
        System.out.println("Tables saved in " + directory.toAbsolutePath());
    }
}
