#include <stdio.h>

// Function to perform the Tower of Hanoi
void ToH(int n, char source, char spare, char dest);

// Static variable to count the steps
static int step = 0;

int main() {
    int n;
    printf("\nEnter the number of rings: ");
    scanf("%d", &n);

    // Move n rings from A to C with B as auxiliary
    ToH(n, 'A', 'B', 'C');
    return 0;
}

// Recursive function to solve the Tower of Hanoi problem
void ToH(int n, char A, char B, char C) {
    if (n == 1) {
        // Base case: Move a single disk from source to destination
        printf("\nStep %d: Move disk %d from %c to %c", ++step, n, A, C);
    } else {
        // Move n-1 disks from source to spare, using destination as auxiliary
        ToH(n - 1, A, C, B);
        
        // Move the nth disk from source to destination
        printf("\nStep %d: Move disk %d from %c to %c", ++step, n, A, C);
        
        // Move the n-1 disks from spare to destination, using source as auxiliary
        ToH(n - 1, B, A, C);
    }
}
