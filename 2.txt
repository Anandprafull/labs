#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAMELEN 50
#define MAX_DEPTLEN 50

typedef struct employee {
    char Emp_name[MAX_NAMELEN];
    int Emp_id;
    char Dept_name[MAX_DEPTLEN];
    float Salary;
} EMPLOYEE;

// Function to find the total salary of employees of a specified department
float total_salary_by_dept(EMPLOYEE *emp_array, int n, char *dept_name) {
    float total_salary = 0;
    for (int i = 0; i < n; i++) {
        if (strcmp(emp_array[i].Dept_name, dept_name) == 0) {
            total_salary += emp_array[i].Salary;
        }
    }
    return total_salary;
}

// Function to display employee details
void display_employees(EMPLOYEE *emp_array, int n) {
    for (int i = 0; i < n; i++) {
        printf("Employee %d name: %s\n", i + 1, emp_array[i].Emp_name);
        printf("Employee %d id: %d\n", i + 1, emp_array[i].Emp_id);
        printf("Employee %d dept: %s\n", i + 1, emp_array[i].Dept_name);
        printf("Employee %d salary: %.2f\n", i + 1, emp_array[i].Salary);
        printf("\n");
    }
}

int main() {
    int n;
    char dept_name[MAX_DEPTLEN];
    EMPLOYEE *emp_array; // Pointer to structure

    printf("Enter the number of employees: ");
    scanf("%d", &n);

    // Dynamically allocate memory for n employees
    emp_array = (EMPLOYEE *)malloc(n * sizeof(EMPLOYEE));
    if (emp_array == NULL) {
        printf("Memory allocation failed!\n");
        return 1; // Exit if memory allocation fails
    }

    // Read employee details
    for (int i = 0; i < n; i++) {
        printf("Enter the name of employee %d: ", i + 1);
        scanf("%s", emp_array[i].Emp_name);
        printf("Enter the id of employee %d: ", i + 1);
        scanf("%d", &emp_array[i].Emp_id);
        printf("Enter the department of employee %d: ", i + 1);
        scanf("%s", emp_array[i].Dept_name);
        printf("Enter the salary of employee %d: ", i + 1);
        scanf("%f", &emp_array[i].Salary);
        printf("\n");
    }

    // Display employee details
    printf("The employee details are:\n");
    display_employees(emp_array, n);

    // Calculate total salary for a specified department
    printf("Enter the department name to calculate total salary: ");
    scanf("%s", dept_name);
    float total_salary = total_salary_by_dept(emp_array, n, dept_name);
    printf("Total salary of employees in %s department is %.2f\n", dept_name, total_salary);

    // Free allocated memory
    free(emp_array);
    return 0;
}
