import java.util.ArrayList;
import java.util.HashSet;

public class GraphGeneratorTest {
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
    private static String fingerprint(DirectedGraph graph) { return graph.adjacency.toString(); }
    public static void main(String[] args) {
        for (int[] size : new int[][]{{10,30}, {100,500}, {1000,5000}}) {
            int v = size[0], e = size[1];
            Graph graph = GraphGenerator.connected(v, e, 42);
            Graph copy = GraphGenerator.connected(v, e, 42);
            check(graph.edges.size() == e, "Edge count");
            HashSet<String> pairs = new HashSet<>();
            for (int i = 0; i < e; i++) {
                Edge a = graph.edges.get(i), b = copy.edges.get(i);
                check(a.from == b.from && a.to == b.to && a.weight == b.weight, "Seed reproducibility");
                check(a.from != a.to && a.weight >= 1 && a.weight <= 100, "Edge constraints");
                check(pairs.add(a.from + ":" + a.to), "Duplicate edge");
            }
            check(Kruskal.findMST(graph).size() == v - 1, "Connectivity");
            DirectedGraph dag = GraphGenerator.directed(v, e, false, 42);
            check(fingerprint(dag).equals(fingerprint(GraphGenerator.directed(v, e, false, 42))), "DAG seed");
            ArrayList<Integer> order = TopologicalSort.sort(dag);
            int[] position = new int[v];
            for (int i = 0; i < v; i++) position[order.get(i)] = i;
            int count = 0;
            for (int from = 0; from < v; from++) {
                HashSet<Integer> neighbors = new HashSet<>();
                for (int to : dag.adjacency.get(from)) {
                    check(neighbors.add(to) && position[from] < position[to], "DAG edges"); count++;
                }
            }
            check(count == e, "DAG count");
            DirectedGraph cyclic = GraphGenerator.directed(v, e, true, 42);
            check(fingerprint(cyclic).equals(fingerprint(GraphGenerator.directed(v, e, true, 42))), "Cycle seed");
            count = cyclic.adjacency.stream().mapToInt(ArrayList::size).sum();
            check(count == e, "Cycle count");
            boolean rejected = false;
            try { TopologicalSort.sort(cyclic); } catch (IllegalArgumentException expected) { rejected = true; }
            check(rejected, "Guaranteed cycle");
            OperationStats stats = new OperationStats();
            Kosaraju.findSCC(cyclic, stats);
            check(stats.edgeExaminations == 3L * e, "SCC counter");
            stats = new OperationStats();
            TopologicalSort.sort(dag, stats);
            check(stats.edgeExaminations == 2L * e, "Topological counter");
            stats = new OperationStats();
            Dijkstra.shortestPaths(graph, 0, stats);
            check(stats.edgeExaminations == 3L * e, "Dijkstra counter");
            stats = new OperationStats();
            Prim.findMST(graph, stats);
            check(stats.edgeExaminations == 3L * e, "Prim counter");
        }
        System.out.println("Graph generator and operation counter tests passed");
    }
}
