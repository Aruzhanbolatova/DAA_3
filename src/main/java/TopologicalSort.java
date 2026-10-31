import java.util.ArrayDeque;
import java.util.ArrayList;

public class TopologicalSort {
    public static ArrayList<Integer> sort(DirectedGraph graph) {
        return sort(graph, new OperationStats());
    }

    public static ArrayList<Integer> sort(DirectedGraph graph, OperationStats stats) {
        int[] indegree = new int[graph.vertices];
        for (int from = 0; from < graph.vertices; from++) {
            for (int to : graph.adjacency.get(from)) {
                stats.edgeExaminations++;
                indegree[to]++;
            }
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int vertex = 0; vertex < graph.vertices; vertex++) {
            if (indegree[vertex] == 0) {
                queue.add(vertex);
            }
        }

        ArrayList<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.remove();
            order.add(vertex);
            for (int neighbor : graph.adjacency.get(vertex)) {
                stats.edgeExaminations++;
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (order.size() != graph.vertices) {
            throw new IllegalArgumentException("Graph contains a cycle: topological ordering is impossible");
        }
        return order;
    }
}
