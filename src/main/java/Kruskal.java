import java.util.ArrayList;
import java.util.Comparator;
public class Kruskal {
    public static ArrayList<Edge> findMST(Graph graph) {
        return findMST(graph, new OperationStats());
    }

    public static ArrayList<Edge> findMST(Graph graph, OperationStats stats) {
        ArrayList<Edge> sortedEdges = new ArrayList<>(graph.edges);
        sortedEdges.sort(
                (a, b) -> {
                    stats.comparisons++;
                    return Integer.compare(a.weight, b.weight);
                }
        );
        DSU dsu = new DSU(graph.vertices, stats);
        ArrayList<Edge> selectedEdges = new ArrayList<>();
        for (Edge edge : sortedEdges) {
            if (selectedEdges.size() == graph.vertices - 1) {
                break;
            }
            stats.edgeExaminations++;
            if (dsu.union(edge.from, edge.to)) {
                selectedEdges.add(edge);
            }
        }
        return selectedEdges;
    }
}