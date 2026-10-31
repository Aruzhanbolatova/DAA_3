import java.util.HashSet;
import java.util.Random;

public class GraphGenerator {
    public static Graph connected(int vertices, int edges, long seed) {
        validate(vertices, edges, true);
        if (edges < vertices - 1) throw new IllegalArgumentException("Too few edges for connectivity");
        Graph graph = new Graph(vertices);
        Random random = new Random(seed);
        HashSet<Long> used = new HashSet<>();
        for (int v = 1; v < vertices; v++) {
            int u = random.nextInt(v);
            used.add(key(u, v));
            graph.addEdge(u, v, 1 + random.nextInt(100));
        }
        while (graph.edges.size() < edges) {
            int a = random.nextInt(vertices), b = random.nextInt(vertices);
            int u = Math.min(a, b), v = Math.max(a, b);
            if (u != v && used.add(key(u, v))) graph.addEdge(u, v, 1 + random.nextInt(100));
        }
        return graph;
    }

    public static DirectedGraph directed(int vertices, int edges, boolean cyclic, long seed) {
        validate(vertices, edges, !cyclic);
        if (cyclic && (vertices < 2 || edges < 2)) throw new IllegalArgumentException("Need two cycle edges");
        DirectedGraph graph = new DirectedGraph(vertices);
        HashSet<Long> used = new HashSet<>();
        Random random = new Random(seed);
        int count = 0;
        if (cyclic) {
            graph.addEdge(0, 1); graph.addEdge(1, 0);
            used.add(key(0, 1)); used.add(key(1, 0)); count = 2;
        }
        while (count < edges) {
            int u = random.nextInt(vertices), v = random.nextInt(vertices);
            if (!cyclic && u > v) { int temp = u; u = v; v = temp; }
            if (u != v && used.add(key(u, v))) { graph.addEdge(u, v); count++; }
        }
        return graph;
    }
    private static long key(int u, int v) { return ((long) u << 32) | (v & 0xffffffffL); }
    private static void validate(int vertices, int edges, boolean half) {
        long maximum = (long) vertices * (vertices - 1) / (half ? 2 : 1);
        if (vertices < 1 || edges < 0 || edges > maximum) throw new IllegalArgumentException("Invalid graph size");
    }
}
