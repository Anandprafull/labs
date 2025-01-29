#include <stdio.h>
#include <ctype.h>

#define MAX 20

int stack[MAX];
int top = -1;

// Function to push an element onto the stack
void push(int x) {
    if (top < MAX - 1) {
        stack[++top] = x;
    } else {
        printf("Stack Overflow\n");
    }
}

// Function to pop an element from the stack
int pop() {
    if (top == -1) {
        printf("Stack Underflow\n");
        return -1; // Return -1 to indicate underflow
    } else {
        return stack[top--];
    }
}

int main() {
    char exp[MAX];
    char *e;
    int n1, n2, n3, num;

    printf("Enter the postfix expression: ");
    scanf("%s", exp);
    
    e = exp;
    while (*e != '\0') {
        if (isdigit(*e)) {
            // Convert character to integer and push onto the stack
            num = *e - '0'; // '0' is ASCII 48, so subtracting gives the integer value
            push(num);
        } else {
            // Pop two operands from the stack
            n1 = pop();
            n2 = pop();
            switch (*e) {
                case '+':
                    n3 = n2 + n1; // Note: n2 is popped first, so it is the left operand
                    break;
                case '-':
                    n3 = n2 - n1;
                    break;
                case '*':
                    n3 = n2 * n1;
                    break;
                case '/':
                    if (n1 != 0) {
                        n3 = n2 / n1;
                    } else {
                        printf("Division by zero error\n");
                        return -1; // Exit on division by zero
                    }
                    break;
                default:
                    printf("Invalid operator: %c\n", *e);
                    return -1; // Exit on invalid operator
            }
            // Push the result back onto the stack
            push(n3);
        }
        e++;
    }

    // The final result will be the only element left in the stack
    printf("\nThe result of expression %s = %d\n\n", exp, pop());
    return 0;
}
