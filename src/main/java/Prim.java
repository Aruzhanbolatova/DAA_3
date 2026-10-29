import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Prim {
    public static ArrayList<Edge> findMST(Graph graph) {
        ArrayList<ArrayList<Edge>> adjacency = new ArrayList<>();
        for (int i = 0; i < graph.vertices; i++) adjacency.add(new ArrayList<>());
        for (Edge edge : graph.edges) {
            adjacency.get(edge.from).add(edge);
            adjacency.get(edge.to).add(new Edge(edge.to, edge.from, edge.weight));
        }
        boolean[] visited = new boolean[graph.vertices];
        ArrayList<Edge> result = new ArrayList<>();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        // A disconnected graph produces a minimum spanning forest.
        for (int start = 0; start < graph.vertices; start++) {
            if (visited[start]) continue;
            visited[start] = true;
            queue.addAll(adjacency.get(start));
            while (!queue.isEmpty()) {
                Edge edge = queue.remove();
                if (visited[edge.to]) continue;
                visited[edge.to] = true;
                result.add(edge);
                for (Edge next : adjacency.get(edge.to)) {
                    if (!visited[next.to]) queue.add(next);
                }
            }
        }
        return result;
    }
}
