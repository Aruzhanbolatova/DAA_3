import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private record Entry(int vertex, long distance) {}

    public static class Result {
        public final long[] distances;
        private final int[] previous;

        private Result(long[] distances, int[] previous) {
            this.distances = distances;
            this.previous = previous;
        }

        public ArrayList<Integer> pathTo(int target) {
            if (target < 0 || target >= distances.length) {
                throw new IllegalArgumentException("Invalid target");
            }
            ArrayList<Integer> path = new ArrayList<>();
            if (distances[target] == Long.MAX_VALUE) return path;
            for (int vertex = target; vertex != -1; vertex = previous[vertex]) {
                path.add(vertex);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static Result shortestPaths(Graph graph, int source) {
        if (source < 0 || source >= graph.vertices) {
            throw new IllegalArgumentException("Invalid source");
        }
        ArrayList<ArrayList<Edge>> adjacency = new ArrayList<>();
        for (int i = 0; i < graph.vertices; i++) adjacency.add(new ArrayList<>());
        for (Edge edge : graph.edges) {
            if (edge.weight < 0) {
                throw new IllegalArgumentException("Dijkstra requires non-negative weights");
            }
            adjacency.get(edge.from).add(edge);
            adjacency.get(edge.to).add(new Edge(edge.to, edge.from, edge.weight));
        }
        long[] distances = new long[graph.vertices];
        int[] previous = new int[graph.vertices];
        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(previous, -1);
        PriorityQueue<Entry> queue = new PriorityQueue<>(Comparator.comparingLong(Entry::distance));
        distances[source] = 0;
        queue.add(new Entry(source, 0));
        while (!queue.isEmpty()) {
            Entry current = queue.remove();
            if (current.distance != distances[current.vertex]) continue;
            for (Edge edge : adjacency.get(current.vertex)) {
                long candidate = current.distance + edge.weight;
                if (candidate < distances[edge.to]) {
                    distances[edge.to] = candidate;
                    previous[edge.to] = current.vertex;
                    queue.add(new Entry(edge.to, candidate));
                }
            }
        }
        return new Result(distances, previous);
    }
}
