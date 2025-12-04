import java.util.*;

class Person {
    String name, gender;
    int age;

    Person(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    @Override
    public String toString() {
        return "\nName: " + name + "\nGender: " + gender + "\nAge: " + age;
    }
}

class Employee extends Person {
    String company;
    double salary;

    Employee(String name, String gender, int age, String company, double salary) {
        super(name, gender, age);
        this.company = company;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + "\nCompany: " + company + "\nSalary: " + salary;
    }
}

class Student extends Person {
    String school;
    double grade;

    Student(String name, String gender, int age, String school, double grade) {
        super(name, gender, age);
        this.school = school;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSchool: " + school + "\nGrade: " + grade;
    }
}

public class Main {
    public static void main(String[] args) {
        // Hardcoded employee details
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John", "Male", 30, "ABC Corp", 50000));
        employees.add(new Employee("Alice", "Female", 28, "XYZ Ltd", 60000));
        employees.add(new Employee("Bob", "Male", 35, "Tech Co", 70000));
        employees.add(new Employee("Eve", "Female", 32, "Global Inc", 55000));
        employees.add(new Employee("Charlie", "Male", 40, "Innovate LLC", 80000));

        // Hardcoded student details
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("Mark", "Male", 20, "ABC High", 90));
        students.add(new Student("Lucy", "Female", 22, "XYZ University", 85));
        students.add(new Student("Oliver", "Male", 21, "PQR College", 88));
        students.add(new Student("Emma", "Female", 19, "LMN High", 92));
        students.add(new Student("Jack", "Male", 23, "ABC College", 87));

        // Display Details
        System.out.println("\nEmployee Details:");
        for(Employee employee: employees) {
            System.out.println(employee);
        }

        System.out.println("\nStudent Details:");
        for(Student student: students) {
            System.out.println(student);
        }
    }
}
