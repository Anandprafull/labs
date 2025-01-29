#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 5

int stack[MAX];
int top = -1;

// Function declarations
void push(int x);
int pop();
void display();
void check_palindrome();
void check_palindrome2();

int main() {
    int choice, x;
    while (1) {
        printf("\n1. Push ");
        printf("\n2. Pop ");
        printf("\n3. Display ");
        printf("\n4. Quit ");
        printf("\n5. Check Palindrome ");
        printf("\nEnter your choice: ");
        scanf("%d", &choice);
        switch (choice) {
            case 1:
                printf("Enter the element to be pushed: ");
                scanf("%d", &x);
                push(x);
                break;
            case 2:
                x = pop();
                if (x != -1) {
                    printf("Popped element is: %d\n", x);
                }
                break;
            case 3:
                display();
                break;
            case 4:
                exit(0);
            case 5:
                check_palindrome();
                check_palindrome2();
                break;
            default:
                printf("Invalid choice\n");
        }
    }
}

void push(int x) {
    if (top == MAX - 1) {
        printf("Stack Overflow\n");
    } else {
        top++;
        stack[top] = x;
    }
}

int pop() {
    if (top == -1) {
        printf("Stack Underflow\n");
        return -1; // Return -1 to indicate underflow
    } else {
        return stack[top--];
    }
}

void display() {
    if (top == -1) {
        printf("Stack is empty\n");
    } else {
        printf("Stack elements are: ");
        for (int i = top; i >= 0; i--) {
            printf("%d ", stack[i]);
        }
        printf("\n");
    }
}

// Check if a string is palindrome
void check_palindrome() {
    char str[20];
    printf("Resetting stack\n");
    top = -1; // Reset the stack
    printf("Enter the string: ");
    scanf("%s", str);
    
    // Push each character of the string onto the stack
    for (int i = 0; str[i] != '\0'; i++) {
        push(str[i]);
    }

    // Check if the string is a palindrome
    int flag = 0;
    for (int j = 0; str[j] != '\0'; j++) {
        if (str[j] != pop()) {
            flag = 1; // If characters do not match, set flag
            break;
        }
    }

    if (flag == 1) {
        printf("String is not a palindrome\n");
    } else {
        printf("String is a palindrome\n");
    }
}

// Check if top & bottom half of stack are mirror images of each other
void check_palindrome2() {
    int floor = 0, ceil = top, flag = 0;
    while (floor < ceil) {
        if (stack[floor] != stack[ceil]) {
            flag = 1; // If elements do not match, set flag
            break;
        }
        floor++;
        ceil--;
    }

    if (flag == 1) {
        printf("Stack is not a palindrome\n");
    } else {
        printf("Stack is a palindrome\n");
    }
}
