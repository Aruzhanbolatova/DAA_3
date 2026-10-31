public class DSU {
    private final OperationStats stats;
    private int[] parent;
    private int[] size;
    public DSU(int n) {
        this(n, new OperationStats());
    }
    public DSU(int n, OperationStats stats) {
        this.stats = stats;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    public int find(int x) {
        stats.findCalls++;
        if (parent[x] != x) {
            stats.parentTraversals++;
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    public boolean union(int a, int b) {
        stats.unionCalls++;
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return false;
        }
        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        return true;
    }
}