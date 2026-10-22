public class main {
    public static void main(String[] args) {
        Edge road = new Edge(0,1,5);

        System.out.println(
                road.from+"->" + road.to+",cost:"+
                        road.weight
        );

    }
}
