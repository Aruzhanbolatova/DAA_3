public class main {
    public static void main(String[] args) {
        DSU dsu = new DSU(3);
        System.out.println(dsu.find(0) == dsu.find(1));
        System.out.println(dsu.union(0, 1));
        System.out.println(dsu.find(0) == dsu.find(1));
        System.out.println(dsu.union(0, 1));
        System.out.println(dsu.find(0) == dsu.find(2));
    }
}