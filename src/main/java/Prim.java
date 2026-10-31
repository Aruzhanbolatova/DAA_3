import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Prim {
    public static ArrayList<Edge> findMST(Graph graph) {
        return findMST(graph, new OperationStats());
    }

    public static ArrayList<Edge> findMST(Graph graph, OperationStats stats) {
        ArrayList<ArrayList<Edge>> adjacency = new ArrayList<>();
        for (int i = 0; i < graph.vertices; i++) adjacency.add(new ArrayList<>());
        for (Edge edge : graph.edges) {
            stats.edgeExaminations++;
            adjacency.get(edge.from).add(edge);
            adjacency.get(edge.to).add(new Edge(edge.to, edge.from, edge.weight));
        }
        boolean[] visited = new boolean[graph.vertices];
        ArrayList<Edge> result = new ArrayList<>();
        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> {
            stats.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });
        // A disconnected graph produces a minimum spanning forest.
        for (int start = 0; start < graph.vertices; start++) {
            if (visited[start]) continue;
            visited[start] = true;
            for (Edge edge : adjacency.get(start)) {
                stats.edgeExaminations++;
                queue.add(edge);
            }
            while (!queue.isEmpty()) {
                Edge edge = queue.remove();
                if (visited[edge.to]) continue;
                visited[edge.to] = true;
                result.add(edge);
                for (Edge next : adjacency.get(edge.to)) {
                    stats.edgeExaminations++;
                    if (!visited[next.to]) queue.add(next);
                }
            }
        }
        return result;
    }
}
