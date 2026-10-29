import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WeightedAlgorithmsTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    private static long weight(ArrayList<Edge> edges) {
        long total = 0;
        for (Edge edge : edges) total += edge.weight;
        return total;
    }
    private static void verify(Graph graph) {
        ArrayList<Edge> kruskal = Kruskal.findMST(graph);
        ArrayList<Edge> prim = Prim.findMST(graph);
        check(weight(kruskal) == weight(prim), "Different forest weights");
        check(kruskal.size() == prim.size(), "Different connectivity");
        DSU forest = new DSU(graph.vertices);
        for (Edge edge : prim) check(forest.union(edge.from, edge.to), "Prim cycle");
        for (Edge edge : graph.edges) {
            check(forest.find(edge.from) == forest.find(edge.to), "Forest misses a component");
        }
        for (int source = 0; source < graph.vertices; source++) {
            // Independent Bellman-Ford reference for undirected non-negative graphs.
            long[] expected = new long[graph.vertices];
            Arrays.fill(expected, Long.MAX_VALUE);
            expected[source] = 0;
            for (int pass = 1; pass < graph.vertices; pass++) {
                for (Edge edge : graph.edges) {
                    if (expected[edge.from] != Long.MAX_VALUE) {
                        expected[edge.to] = Math.min(expected[edge.to], expected[edge.from] + edge.weight);
                    }
                    if (expected[edge.to] != Long.MAX_VALUE) {
                        expected[edge.from] = Math.min(expected[edge.from], expected[edge.to] + edge.weight);
                    }
                }
            }
            Dijkstra.Result result = Dijkstra.shortestPaths(graph, source);
            check(Arrays.equals(expected, result.distances), "Wrong distances");
            for (int target = 0; target < graph.vertices; target++) {
                ArrayList<Integer> path = result.pathTo(target);
                if (expected[target] == Long.MAX_VALUE) {
                    check(path.isEmpty(), "Unreachable path must be empty");
                    continue;
                }
                check(path.get(0) == source && path.get(path.size() - 1) == target, "Path endpoints");
                long total = 0;
                for (int i = 1; i < path.size(); i++) {
                    int from = path.get(i - 1), to = path.get(i);
                    long best = Long.MAX_VALUE;
                    for (Edge edge : graph.edges) {
                        if ((edge.from == from && edge.to == to) || (edge.from == to && edge.to == from)) {
                            best = Math.min(best, edge.weight);
                        }
                    }
                    check(best != Long.MAX_VALUE, "Nonexistent path edge");
                    total += best;
                }
                check(total == expected[target], "Path cost");
            }
        }
    }
    public static void main(String[] args) {
        DSU dsu = new DSU(4);
        check(dsu.union(0, 1) && dsu.union(2, 3) && dsu.union(1, 3), "DSU merge");
        check(!dsu.union(0, 3) && dsu.find(0) == dsu.find(3), "DSU connectivity");
        verify(new Graph(1));
        verify(new Graph(4));
        Graph square = new Graph(4);
        square.addEdge(0, 1, 1); square.addEdge(1, 2, 1);
        square.addEdge(2, 3, 1); square.addEdge(3, 0, 1);
        check(weight(Prim.findMST(square)) == 3, "Known MST cost");
        verify(square);
        Graph largeWeights = new Graph(3);
        largeWeights.addEdge(0, 1, Integer.MAX_VALUE);
        largeWeights.addEdge(1, 2, Integer.MAX_VALUE);
        verify(largeWeights);
        Random random = new Random(42);
        for (int run = 0; run < 50; run++) {
            Graph graph = new Graph(10);
            for (int i = 0; i < 25; i++) graph.addEdge(random.nextInt(10), random.nextInt(10), random.nextInt(10));
            verify(graph);
        }
        Graph negative = new Graph(2);
        negative.addEdge(0, 1, -1);
        boolean rejected = false;
        try { Dijkstra.shortestPaths(negative, 0); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "Negative weight accepted");
        System.out.println("Weighted algorithm tests passed");
    }
}
