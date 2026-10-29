import java.util.ArrayList;

public class DirectedGraph {
    int vertices;
    ArrayList<ArrayList<Integer>> adjacency;

    public DirectedGraph(int vertices) {
        if (vertices < 1) {
            throw new IllegalArgumentException("At least one vertex is required");
        }
        this.vertices = vertices;
        adjacency = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to) {
        if (from < 0 || from >= vertices || to < 0 || to >= vertices) {
            throw new IllegalArgumentException("Invalid district number");
        }
        adjacency.get(from).add(to);
    }
}
