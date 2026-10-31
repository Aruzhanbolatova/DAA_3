import java.util.ArrayList;
public class TopologicalSortTest {
    private static void checkOrder(DirectedGraph graph) {
        ArrayList<Integer> order = TopologicalSort.sort(graph);
        if (order.size() != graph.vertices) throw new AssertionError("Missing vertices");
        boolean[] seen = new boolean[graph.vertices];
        int[] position = new int[graph.vertices];
        for (int i = 0; i < order.size(); i++) {
            int vertex = order.get(i);
            if (seen[vertex]) throw new AssertionError("Duplicate vertex");
            seen[vertex] = true;
            position[vertex] = i;
        }
        for (int from = 0; from < graph.vertices; from++) {
            for (int to : graph.adjacency.get(from)) {
                if (position[from] >= position[to]) throw new AssertionError("Invalid edge order");
            }
        }
    }
    private static void checkCycle(DirectedGraph graph) {
        try {
            TopologicalSort.sort(graph);
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Cycle was not detected");
    }
    public static void main(String[] args) {
        checkOrder(new DirectedGraph(1));
        checkOrder(new DirectedGraph(4));
        DirectedGraph dag = new DirectedGraph(6);
        dag.addEdge(0, 2);
        dag.addEdge(1, 2);
        dag.addEdge(2, 3);
        dag.addEdge(2, 3);
        dag.addEdge(4, 5);
        checkOrder(dag);
        checkOrder(dag);
        dag.addEdge(3, 1);
        checkCycle(dag);
        DirectedGraph selfLoop = new DirectedGraph(1);
        selfLoop.addEdge(0, 0);
        checkCycle(selfLoop);
        System.out.println("Topological sorting tests passed");
    }
}
