public class Ford {
    private static final int MAX_VALUE = 999;

    public static void main(String[] args) {
        int n = 5;
        int src = 0; 
        
        int[][] A = {
            {0,  6,  7, 999, 999},
            {999, 0,  8,  5, 999},
            {999, 999, 0,  9, 999},
            {999, 999, 999, 0,  2},
            {3,  999, 999, 999, 0}
        };

        int[] D = new int[n];  
        for (int i = 0; i < n; i++) D[i] = MAX_VALUE;
        D[src] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (A[u][v] != MAX_VALUE && D[u] != MAX_VALUE && D[v] > D[u] + A[u][v]) {
                        D[v] = D[u] + A[u][v];
                    }
                }
            }
        }

        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (A[u][v] != MAX_VALUE && D[u] != MAX_VALUE && D[v] > D[u] + A[u][v]) {
                    System.out.println("Graph contains negative weight cycle");
                    return;
                }
            }
        }

        System.out.println("Shortest distances from source vertex " + (src + 1));
        for (int i = 0; i < n; i++) {
            System.out.println("Distance to vertex " + (i + 1) + " is " + D[i]);
        }
    }
}
