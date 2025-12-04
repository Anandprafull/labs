import java.util.*;
import java.util.concurrent.*;

class P7 {
    private final int[] buf = new int; // Bounded buffer with capacity 5
    private int in = 0, out = 0, count = 0;
    private final Random random = new Random();

    // Method for producing items into the buffer
    synchronized void put(int n) throws InterruptedException {
        while (count == buf.length) {
            wait();
        }
        buf[in] = n;
        in = (in + 1) % buf.length;
        count++;
        System.out.println("Produced: " + n);
        notifyAll();
    }

    // Method for consuming items from the buffer
    synchronized int get() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        int item = buf[out];
        out = (out + 1) % buf.length;
        count--;
        System.out.println("Consumed: " + item);
        notifyAll();
        return item;
    }

    public static void main(String[] args) {
        P7 pc = new P7();
        ExecutorService executor = Executors.newFixedThreadPool(6); // 3 producers, 3 consumers

        // Create multiple producer threads
        for (int i=0; i < 3; i++) {
            int producerId = i + 1;
            executor.submit(() -> {
                try {
                    for (int i1=0; i1 < 10; i1++) {
                        int item = producerId * 10 + i1;
                        pc.put(item);
                        Thread.sleep(pc.random.nextInt(500));
                    }
                } catch (InterruptedException ignored) {}
            });
        }

        // Create multiple consumer threads
        for (int i=0; i < 3; i++) {
            executor.submit(() -> {
                try {
                    while (true) {
                        pc.get();
                        Thread.sleep(pc.random.nextInt(1000));
                    }
                } catch (InterruptedException ignored) {}
            });
        }
    }
}
