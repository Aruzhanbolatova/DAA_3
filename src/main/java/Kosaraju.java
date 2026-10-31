import java.util.ArrayList;
import java.util.Collections;

public class Kosaraju {
    public static ArrayList<ArrayList<Integer>> findSCC(DirectedGraph graph) {
        return findSCC(graph, new OperationStats());
    }

    public static ArrayList<ArrayList<Integer>> findSCC(DirectedGraph graph, OperationStats stats) {
        boolean[] visited = new boolean[graph.vertices];
        ArrayList<Integer> order = new ArrayList<>();
        for (int vertex = 0; vertex < graph.vertices; vertex++) {
            if (!visited[vertex]) {
                fillOrder(vertex, graph, visited, order, stats);
            }
        }

        DirectedGraph reversed = new DirectedGraph(graph.vertices);
        for (int from = 0; from < graph.vertices; from++) {
            for (int to : graph.adjacency.get(from)) {
                stats.edgeExaminations++;
                reversed.addEdge(to, from);
            }
        }

        visited = new boolean[graph.vertices];
        Collections.reverse(order);
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        for (int vertex : order) {
            if (!visited[vertex]) {
                ArrayList<Integer> component = new ArrayList<>();
                collectComponent(vertex, reversed, visited, component, stats);
                components.add(component);
            }
        }
        return components;
    }

    private static void fillOrder(int vertex, DirectedGraph graph,
                                  boolean[] visited, ArrayList<Integer> order, OperationStats stats) {
        visited[vertex] = true;
        for (int neighbor : graph.adjacency.get(vertex)) {
            stats.edgeExaminations++;
            if (!visited[neighbor]) {
                fillOrder(neighbor, graph, visited, order, stats);
            }
        }
        order.add(vertex);
    }

    private static void collectComponent(int vertex, DirectedGraph graph,
                                          boolean[] visited, ArrayList<Integer> component, OperationStats stats) {
        visited[vertex] = true;
        component.add(vertex);
        for (int neighbor : graph.adjacency.get(vertex)) {
            stats.edgeExaminations++;
            if (!visited[neighbor]) {
                collectComponent(neighbor, graph, visited, component, stats);
            }
        }
    }
}
