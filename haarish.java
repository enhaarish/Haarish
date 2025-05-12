import java.util.*;

class Edge {
    String to;
    double cost;
    String mode; // e.g., WALK, BUS, TRAIN

    public Edge(String to, double cost, String mode) {
        this.to = to;
        this.cost = cost;
        this.mode = mode;
    }
}

class Node implements Comparable<Node> {
    String name;
    double cost;

    public Node(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.cost, other.cost);
    }
}

public class MultimodalRoutePlanner {

    static Map<String, List<Edge>> graph = new HashMap<>();

    public static void addEdge(String from, String to, double cost, String mode) {
        graph.putIfAbsent(from, new ArrayList<>());
        graph.get(from).add(new Edge(to, cost, mode));
    }

    public static void dijkstra(String start, String end) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Map<String, String> transportMode = new HashMap<>();

        for (String key : graph.keySet()) {
            distances.put(key, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.name.equals(end)) break;

            for (Edge edge : graph.getOrDefault(current.name, new ArrayList<>())) {
                double newDist = distances.get(current.name) + edge.cost;
                if (newDist < distances.getOrDefault(edge.to, Double.MAX_VALUE)) {
                    distances.put(edge.to, newDist);
                    pq.add(new Node(edge.to, newDist));
                    prev.put(edge.to, current.name);
                    transportMode.put(edge.to, edge.mode);
                }
            }
        }

        if (!distances.containsKey(end) || distances.get(end) == Double.MAX_VALUE) {
            System.out.println("No path found.");
            return;
        }

        // Reconstruct path
        LinkedList<String> path = new LinkedList<>();
        String current = end;

        while (current != null) {
            path.addFirst(current);
            current = prev.get(current);
        }

        System.out.println("Optimal Route:");
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            System.out.println(from + " -> " + to + " via " + transportMode.get(to));
        }
        System.out.println("Total Cost: " + distances.get(end));
    }

    public static void main(String[] args) {
        // Sample graph
        addEdge("Home", "BusStop1", 1.0, "WALK");
        addEdge("BusStop1", "CityCenter", 3.0, "BUS");
        addEdge("Home", "TrainStation", 1.5, "WALK");
        addEdge("TrainStation", "CityCenter", 2.0, "TRAIN");
        addEdge("CityCenter", "Office", 0.5, "WALK");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Start Location: ");
        String start = sc.nextLine();
        System.out.print("Enter Destination: ");
        String end = sc.nextLine();

        dijkstra(start, end);
    }
}
