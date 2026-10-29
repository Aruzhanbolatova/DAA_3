import java.util.ArrayList;
public class Graph {
    int vertices;
    ArrayList<Edge> edges;
    public Graph(int vertices) {
        if (vertices < 1) {
            throw new IllegalArgumentException("At least one vertex is required");
        }
        this.vertices = vertices;
        this.edges = new ArrayList<>();
    }
    public void addEdge(int from, int to, int weight) {
        if (from < 0 || from >= vertices
                || to < 0 || to >= vertices) {
            throw new IllegalArgumentException("Invalid district number");
        }
        edges.add(new Edge(from, to, weight));
    }
}