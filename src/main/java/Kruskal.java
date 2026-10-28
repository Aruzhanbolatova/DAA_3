import java.util.ArrayList;
import java.util.Comparator;
public class Kruskal {
    public static ArrayList<Edge> findMST(Graph graph) {
        ArrayList<Edge> sortedEdges = new ArrayList<>(graph.edges);
        sortedEdges.sort(
                Comparator.comparingInt(edge -> edge.weight)
        );
        DSU dsu = new DSU(graph.vertices);
        ArrayList<Edge> selectedEdges = new ArrayList<>();
        for (Edge edge : sortedEdges) {
            if (selectedEdges.size() == graph.vertices - 1) {
                break;
            }
            if (dsu.union(edge.from, edge.to)) {
                selectedEdges.add(edge);
            }
        }
        return selectedEdges;
    }
}