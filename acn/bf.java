import java.util.Arrays;

public class ShortBellmanFord {
    public static void main(String[] args) {
        int vertices = 5;
        int source = 0;
        int INF = Integer.MAX_VALUE;

        int[][] graph = {
            { 0, 30, 15, INF, INF },
            { INF, 0, INF, 20, INF },
            { INF, 10, 0, INF, 25 },
            { INF, INF, INF, 0, 5 },
            { 40, INF, INF, INF, 0 }
        };

        int[] dist = new int[vertices];
        Arrays.fill(dist, INF);
        dist[source] = 0;

        for (int i = 0; i < vertices - 1; i++) {
            for (int u = 0; u < vertices; u++) {
                for (int v = 0; v < vertices; v++) {
                    if (dist[u] != INF && graph[u][v] != INF && dist[u] + graph[u][v] < dist[v]) {
                        dist[v] = dist[u] + graph[u][v];
                    }
                }
            }
        }

        for (int u = 0; u < vertices; u++) {
            for (int v = 0; v < vertices; v++) {
                if (dist[u] != INF && graph[u][v] != INF && dist[u] + graph[u][v] < dist[v]) {
                    System.err.println("Error: Graph contains a negative-weight cycle.");
                    return;
                }
            }
        }

        System.out.println("Distances from source " + source + ": " + Arrays.toString(dist));
    }
}
