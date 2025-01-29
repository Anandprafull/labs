#include <stdio.h>
#include <ctype.h>

#define MAX 100

char stack[MAX];
int top = -1;

// Function declarations
void push(char x);
char pop();
int priority(char x);

int main() {
    char exp[MAX], *e, x;
    printf("Enter the infix expression: ");
    scanf("%s", exp);
    
    e = exp;
    while (*e != '\0') {
        if (isalnum(*e)) {
            // If the character is an operand, print it
            printf("%c", *e);
        } else if (*e == '(') {
            // If the character is '(', push it onto the stack
            push(*e);
        } else if (*e == ')') {
            // If the character is ')', pop and print from the stack until '(' is found
            while ((x = pop()) != '(') {
                printf("%c", x);
            }
        } else {
            // If the character is an operator
            while (top != -1 && priority(stack[top]) >= priority(*e)) {
                printf("%c", pop());
            }
            push(*e);
        }
        e++;
    }

    // Pop all the operators from the stack
    while (top != -1) {
        printf("%c", pop());
    }
    printf("\n");
    return 0;
}

void push(char x) {
    stack[++top] = x;
}

char pop() {
    if (top == -1) {
        return -1; // Stack underflow
    } else {
        return stack[top--];
    }
}

int priority(char x) {
    if (x == '(') return 0;
    if (x == '+' || x == '-') return 1;
    if (x == '*' || x == '/') return 2;
    return 0; // For any other character
}
