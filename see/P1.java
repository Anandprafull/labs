import java.util.*;

public class P1 {
    public static void findQuadraticRoots(double p, double q) {
        double discriminant = p*p - 4*q;
        if (discriminant >= 0) {
            double root1 = (-p + Math.sqrt(discriminant)) / 2;
            double root2 = (-p - Math.sqrt(discriminant)) / 2;
            System.out.println("Roots: " + root1 + "," + root2);
        } else {
            System.out.println("No real roots, discriminant is negative.");
        }
    }

    public static void multiplyArrays(int[] x, int[] y) {
        if (x.length != y.length) {
            System.out.println("Arrays must be of the same length.");
            return;
        }
        int[] result = new int[x.length];
        for (int i=0; i < x.length; i++) {
            result[i] = x[i] * y[i];
        }
        System.out.println("Product: " + Arrays.toString(result));
    }

    public static void bubbleSortAsc(int[] arr) {
        for (int i=0; i < arr.length - 1; i++) {
            for (int j=0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    public static void bubbleSortDesc(int[] arr) {
        for(int i=0; i < arr.length - 1; i++) {
            for(int j=0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Quadratic Equation Roots (For x^2 + px + q = 0, p=-3, q=2)
        double p = -3, q = 2;
        findQuadraticRoots(p, q);

        // Multiply Arrays
        int[] x = {1, 2, 3}, y = {4, 5, 6};
        multiplyArrays(x, y);

        // Bubble Sort
        int[] arr1 = {3, 2, 5, 4, 9};
        bubbleSortAsc(arr1);
        System.out.println("Sorted(Ascending): " + Arrays.toString(arr1));

        int[] arr2 = {3, 2, 5, 4, 9};
        bubbleSortDesc(arr2);
        System.out.println("Sorted (Descending): " + Arrays.toString(arr2));
    }
}
