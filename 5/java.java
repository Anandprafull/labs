import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// =================================================================
// 1. BASIC MATH & ALGORITHMS (Roots, Array Multiply, Bubble Sort)
// =================================================================
public class StudyGuide {

    // 1a. Quadratic Roots (ax^2 + bx + c = 0)
    public static void roots(double a, double b, double c) {
        double d = b * b - 4 * a * c;
        if (a == 0) return;
        if (d >= 0) {
            double r1 = (-b + Math.sqrt(d)) / (2 * a);
            double r2 = (-b - Math.sqrt(d)) / (2 * a);
            System.out.printf("Roots: %.2f, %.2f\n", r1, r2);
        } else {
            double real = -b / (2 * a);
            double imag = Math.sqrt(-d) / (2 * a);
            System.out.printf("Roots: %.2f + i%.2f, %.2f - i%.2f\n", real, imag, real, imag);
        }
    }

    // 1b. Array Multiplication (Element-wise)
    public static int[] multiply(int[] a, int[] b) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) result[i] = a[i] * b[i];
        return result;
    }

    // 1c. Bubble Sort
    public static void bubble(int[] arr, boolean asc) {
        int n = arr.length;
        int[] sorted = Arrays.copyOf(arr, n);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (asc ? (sorted[j] > sorted[j + 1]) : (sorted[j] < sorted[j + 1])) {
                    int temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted " + (asc ? "Asc" : "Desc") + ": " + Arrays.toString(sorted));
    }


    // =================================================================
    // 2. EMPLOYEE DATABASE (Class, List, Stream Operations)
    // (Helper class definition is placed outside the main class below)
    // =================================================================
    private static void demoEmployeeDB() {
        List<Employee2> db = new ArrayList<>();
        db.add(new Employee2("Alice", 101, "Sales", 30, "Rep", 50000));
        db.add(new Employee2("Bob", 102, "Purchase", 45, "Manager", 90000));
        db.add(new Employee2("Charlie", 103, "Sales", 25, "Trainee", 40000));
        db.add(new Employee2("Eve", 105, "Purchase", 50, "Senior Manager", 110000));
        
        // a. Display all
        System.out.println("\n--- 2. Employee Database ---");
        db.forEach(Employee2::display);
        
        // b. Sum of Sales salary
        double sumSales = db.stream()
                            .filter(e -> e.dept.equals("Sales"))
                            .mapToDouble(e -> e.salary)
                            .sum();
        System.out.printf("Sales Dept Total Salary: $%.0f\n", sumSales);
        
        // c. Highest paid manager in Purchase
        Employee2 highestPaid = null;
        for (Employee2 e : db) {
            if (e.dept.equals("Purchase") && e.desig.contains("Manager")) {
                if (highestPaid == null || e.salary > highestPaid.salary) {
                    highestPaid = e;
                }
            }
        }
        System.out.print("Highest Paid Purchase Manager: ");
        if (highestPaid != null) highestPaid.display();
    }
    
    // =================================================================
    // 3. COMPLEX NUMBER (Encapsulation and Comparison)
    // (Helper class definition is placed outside the main class below)
    // =================================================================
    private static void demoComplex() {
        Complex c1 = new Complex(5, 3);
        Complex c2 = new Complex(1.5, -2.5);
        System.out.println("\n--- 3. Complex Numbers ---");
        System.out.println("c1 + c2 = " + c1.add(c2));
        System.out.println("c1 - c2 = " + c1.subtract(c2));
        System.out.println("c1 == c2? " + c1.compare(c2));
    }

    // =================================================================
    // 4. INHERITANCE (Person -> Student/Employee)
    // (Helper classes are placed outside the main class below)
    // =================================================================
    private static void demoInheritance() {
        List<Person> list = new ArrayList<>();
        list.add(new Student4("Jane", 20, "S1001"));
        list.add(new Employee4("Louis", 45, "Partner"));
        
        System.out.println("\n--- 4. Inheritance/Polymorphism ---");
        for (Person p : list) {
            p.displayDetails();
        }
    }
    
    // =================================================================
    // 5. COMPILE-TIME POLYMORPHISM (Method Overloading)
    // =================================================================
    // Version 1: Compare two full strings
    public static int usrstrcmp(String s1, String s2) {
        System.out.printf("Comparing full: '%s' vs '%s'\n", s1, s2);
        return s1.compareTo(s2);
    }
    // Version 2: Compare specified number (n) of characters
    public static int usrstrcmp(String s1, String s2, int n) {
        System.out.printf("Comparing first %d chars\n", n);
        int limit = Math.min(n, Math.min(s1.length(), s2.length()));
        for (int i = 0; i < limit; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return s1.charAt(i) - s2.charAt(i);
            }
        }
        return 0; 
    }
    private static void demoOverloading() {
        String sA = "hello world";
        String sB = "hello java";
        System.out.println("\n--- 5. Method Overloading ---");
        usrstrcmp(sA, sB);
        usrstrcmp(sA, sB, 5); // Overloaded call
    }

    // =================================================================
    // 6. ABSTRACT CLASS (Bank and Interest)
    // (Helper classes are placed outside the main class below)
    // =================================================================
    private static void demoAbstractBank() {
        ArrayList<Bank> accounts = new ArrayList<>();
        accounts.add(new CityBank("CB001", 10000));
        accounts.add(new SBIBank("SB002", 20000));

        System.out.println("\n--- 6. Abstract Class/Polymorphism ---");
        for (Bank acc : accounts) {
            acc.display();
            System.out.printf(", Interest: $%.2f\n", acc.calculateInterest());
        }
    }

    // =================================================================
    // 7. MULTI-THREADED PRODUCER-CONSUMER
    // (Helper classes are placed outside the main class below)
    // =================================================================
    private static void demoProducerConsumer() throws InterruptedException {
        System.out.println("\n--- 7. Producer/Consumer (Output will vary) ---");
        Buffer b = new Buffer();
        Producer p = new Producer(b);
        Consumer c = new Consumer(b);
        p.start();
        c.start();
        Thread.sleep(3000); // Give threads time to run
    }

    // =================================================================
    // 8. EXCEPTION HANDLING (Custom Exceptions)
    // =================================================================
    private static class NegativeNumberException extends Exception {
        public NegativeNumberException(String msg) { super(msg); }
    }
    private static class DivideByZeroException extends Exception {
        public DivideByZeroException(String msg) { super(msg); }
    }
    public static double safeDivide(int num, int den) 
        throws NegativeNumberException, DivideByZeroException 
    {
        if (num < 0 || den < 0) throw new NegativeNumberException("Input must be positive.");
        if (den == 0) throw new DivideByZeroException("Denominator is zero.");
        return (double) num / den;
    }
    private static void demoExceptions() {
        System.out.println("\n--- 8. Custom Exceptions ---");
        // Case 1: Success
        try { System.out.printf("Success: %.2f\n", safeDivide(10, 2)); } catch(Exception e) {}
        // Case 2: Divide by Zero
        try { safeDivide(10, 0); } 
        catch (DivideByZeroException e) { System.err.println("HANDLED: " + e.getMessage()); } 
        catch (Exception e) {}
        // Case 3: Negative Input
        try { safeDivide(-5, 2); } 
        catch (NegativeNumberException e) { System.err.println("HANDLED: " + e.getMessage()); } 
        catch (Exception e) {}
    }


    // =================================================================
    // 9. INTERFACE (Compute for Conversion)
    // (Helper classes are placed outside the main class below)
    // =================================================================
    private static void demoInterface() {
        Compute c1 = new GigaToByte();
        System.out.println("\n--- 9. Interface (Conversion) ---");
        System.out.printf("2.5 GB = %.0f Bytes\n", c1.convert(2.5));
    }

    // =================================================================
    // MAIN METHOD: Runs all 9 demonstrations
    // =================================================================
    public static void main(String[] args) throws InterruptedException {
        // 1. Basic Operations
        System.out.println("--- 1. Basic Ops ---");
        roots(1, -5, 6);
        System.out.println("Multiplied: " + Arrays.toString(multiply(new int[]{2, 3}, new int[]{5, 1})));
        bubble(new int[]{5, 1, 4}, true);
        
        // 2. Employee Database
        demoEmployeeDB();
        
        // 3. Complex Numbers
        demoComplex();
        
        // 4. Inheritance
        demoInheritance();
        
        // 5. Method Overloading
        demoOverloading();

        // 6. Abstract Bank
        demoAbstractBank();

        // 7. Producer/Consumer
        demoProducerConsumer();
        
        // 8. Custom Exceptions
        demoExceptions();

        // 9. Interface
        demoInterface();
    }
}

// =================================================================
// HELPER CLASSES (Required for above demos)
// =================================================================

// 2. Employee Class
class Employee2 {
    String name, dept, desig; int id, age; double salary;
    public Employee2(String n, int i, String d, int a, String g, double s) {
        name = n; id = i; dept = d; age = a; desig = g; salary = s;
    }
    public void display() {
        System.out.printf("  ID:%d, Name:%s, Dept:%s, Sal:$%.0f\n", id, name, dept, salary);
    }
}

// 3. Complex Class
class Complex {
    private double r, i;
    public Complex(double real, double imaginary) { r = real; i = imaginary; }
    public Complex add(Complex other) { return new Complex(this.r + other.r, this.i + other.i); }
    public Complex subtract(Complex other) { return new Complex(this.r - other.r, this.i - other.i); }
    public boolean compare(Complex other) { return this.r == other.r && this.i == other.i; }
    public String toString() { return String.format("(%.1f %s %.1fi)", r, (i >= 0 ? "+" : "-"), Math.abs(i)); }
}

// 4. Inheritance Classes
class Person {
    protected String name; protected int age;
    public Person(String n, int a) { name = n; age = a; }
    public void displayDetails() {
        System.out.printf("Name: %s, Age: %d", name, age);
    }
}
class Student4 extends Person {
    private String studentId;
    public Student4(String n, int a, String id) { super(n, a); studentId = id; }
    @Override
    public void displayDetails() { super.displayDetails(); System.out.printf(", Type: Student, ID: %s\n", studentId); }
}
class Employee4 extends Person {
    private String designation;
    public Employee4(String n, int a, String d) { super(n, a); designation = d; }
    @Override
    public void displayDetails() { super.displayDetails(); System.out.printf(", Type: Employee, Desig: %s\n", designation); }
}

// 6. Abstract Bank Classes
abstract class Bank {
    protected String accNum; protected double balance;
    public Bank(String num, double bal) { accNum = num; balance = bal; }
    public void display() { System.out.printf("[%s] Balance: $%.2f", getClass().getSimpleName(), balance); }
    public abstract double calculateInterest();
}
class CityBank extends Bank {
    public CityBank(String num, double bal) { super(num, bal); }
    @Override public double calculateInterest() { return balance * 0.050; }
}
class SBIBank extends Bank {
    public SBIBank(String num, double bal) { super(num, bal); }
    @Override public double calculateInterest() { return balance * 0.045; }
}

// 7. Producer/Consumer Classes
class Buffer {
    private Queue<Integer> data = new LinkedList<>(); private final int CAPACITY = 3;
    public synchronized void put(int v) throws InterruptedException {
        while (data.size() == CAPACITY) { System.out.println(">> Buffer FULL. P waiting."); wait(); }
        data.add(v); System.out.printf("Producer PUT: %d (Size: %d)\n", v, data.size()); notifyAll();
    }
    public synchronized int get() throws InterruptedException {
        while (data.isEmpty()) { System.out.println("<< Buffer EMPTY. C waiting."); wait(); }
        int v = data.poll(); System.out.printf("Consumer GOT: %d (Size: %d)\n", v, data.size()); notifyAll();
        return v;
    }
}
class Producer extends Thread {
    Buffer b; public Producer(Buffer b) { this.b = b; }
    public void run() { try { for (int i = 1; i <= 3; i++) { b.put(i * 10); Thread.sleep(200); } } catch (InterruptedException e) {} }
}
class Consumer extends Thread {
    Buffer b; public Consumer(Buffer b) { this.b = b; }
    public void run() { try { for (int i = 0; i < 3; i++) { b.get(); Thread.sleep(500); } } catch (InterruptedException e) {} }
}

// 9. Interface Classes
interface Compute {
    double convert(double value);
}
class GigaToByte implements Compute {
    private static final double FACTOR = 1024 * 1024 * 1024;
    @Override public double convert(double gigabytes) { return gigabytes * FACTOR; }
}
