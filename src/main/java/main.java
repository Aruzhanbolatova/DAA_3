import java.util.ArrayList;
public class main {
    public static void main(String[] args) {
        Graph city = new Graph(3);
        city.addEdge(0, 1, 4);
        city.addEdge(0, 2, 2);
        city.addEdge(1, 2, 1);
        ArrayList<Edge> result = Kruskal.findMST(city);
        long totalWeight = 0;
        for (Edge edge : result) {
            System.out.println(
                    edge.from + " - " + edge.to
                            + ", cost: " + edge.weight
            );
            totalWeight += edge.weight;
        }
        if (result.size() == city.vertices - 1) {
            System.out.println("Graph is connected");
            System.out.println("MST cost: " + totalWeight);
        } else {
            System.out.println("Graph is disconnected: MST does not exist");
            System.out.println("Forest cost: " + totalWeight);
        }
        DirectedGraph dependencies = new DirectedGraph(4);
        dependencies.addEdge(0, 1);
        dependencies.addEdge(0, 2);
        dependencies.addEdge(1, 3);
        dependencies.addEdge(2, 3);
        System.out.println("Topological order: " + TopologicalSort.sort(dependencies));
        dependencies.addEdge(3, 0);
        try {
            TopologicalSort.sort(dependencies);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }

        DirectedGraph roads = new DirectedGraph(5);
        roads.addEdge(0, 1);
        roads.addEdge(1, 0);
        roads.addEdge(1, 2);
        roads.addEdge(2, 3);
        roads.addEdge(3, 2);
        System.out.println("SCC: " + Kosaraju.findSCC(roads));
    }
}