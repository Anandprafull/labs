import java.util.*;

class P8 {

    // Method to validate the input (positive integers and non-zero denominator)
    public static boolean isValidInput(int numerator, int denominator) {
        if (numerator < 0 || denominator <= 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;

        // Continuous input loop
        while (!validInput) {
            try {
                System.out.println("Enter two positive integers (numerator and denominator): ");
                int numerator = sc.nextInt();
                int denominator = sc.nextInt();

                // Validate the input using the helper method
                if (isValidInput(numerator, denominator)) {
                    validInput = true;

                    // Perform floating-point division by casting to double
                    double result = (double) numerator / denominator;
                    System.out.println("Result: " + result);

                } else {
                    throw new IllegalArgumentException("Input must be positive integers with a non-zero denominator.");
                }

            } catch (ArithmeticException e) {
                System.out.println("Error: Cannot divide by zero.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter valid integers.");
                sc.nextLine(); // Clear the buffer
            }
        }
        sc.close();
    }
}
