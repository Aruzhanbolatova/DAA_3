import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class KosarajuTest {
    private static void verify(DirectedGraph graph) {
        boolean[][] reach = new boolean[graph.vertices][graph.vertices];
        for (int i = 0; i < graph.vertices; i++) {
            reach[i][i] = true;
            for (int j : graph.adjacency.get(i)) reach[i][j] = true;
        }
        for (int k = 0; k < graph.vertices; k++) {
            for (int i = 0; i < graph.vertices; i++) {
                for (int j = 0; j < graph.vertices; j++) {
                    reach[i][j] |= reach[i][k] && reach[k][j];
                }
            }
        }
        ArrayList<ArrayList<Integer>> components = Kosaraju.findSCC(graph);
        int[] group = new int[graph.vertices];
        Arrays.fill(group, -1);
        for (int c = 0; c < components.size(); c++) {
            if (components.get(c).isEmpty()) throw new AssertionError("Empty SCC");
            for (int vertex : components.get(c)) {
                if (group[vertex] != -1) throw new AssertionError("Repeated vertex");
                group[vertex] = c;
            }
        }
        for (int i = 0; i < graph.vertices; i++) {
            if (group[i] == -1) throw new AssertionError("Missing vertex");
            for (int j = 0; j < graph.vertices; j++) {
                if ((group[i] == group[j]) != (reach[i][j] && reach[j][i])) {
                    throw new AssertionError("Wrong SCC membership");
                }
            }
        }
    }
    public static void main(String[] args) {
        verify(new DirectedGraph(1));
        verify(new DirectedGraph(5));
        DirectedGraph example = new DirectedGraph(5);
        example.addEdge(0, 1); example.addEdge(1, 0); example.addEdge(1, 2);
        example.addEdge(2, 3); example.addEdge(3, 2);
        verify(example);
        example.addEdge(3, 0);
        verify(example);
        DirectedGraph self = new DirectedGraph(1);
        self.addEdge(0, 0); verify(self);
        Random random = new Random(42);
        for (int run = 0; run < 100; run++) {
            DirectedGraph graph = new DirectedGraph(10);
            for (int i = 0; i < 30; i++) graph.addEdge(random.nextInt(10), random.nextInt(10));
            verify(graph);
        }
        DirectedGraph chain = new DirectedGraph(1000);
        for (int i = 0; i < 999; i++) chain.addEdge(i, i + 1);
        if (Kosaraju.findSCC(chain).size() != 1000) throw new AssertionError("Chain SCCs");
        chain.addEdge(999, 0);
        if (Kosaraju.findSCC(chain).size() != 1) throw new AssertionError("Cycle SCC");
        System.out.println("Kosaraju tests passed");
    }
}
