#include <stdio.h>
#include <stdlib.h>

#define MAX 20
int arr[MAX];
int n = 0; // Current number of elements in the array

// Function declarations
void create();
void display();
void insert();
void delete();

int main() {
    int choice;

    while (1) {
        printf("Enter your choice: \n1. Create an array\n2. Display the array\n3. Insert an element\n4. Delete an element\n5. Exit \n");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                create();
                break;
            case 2:
                display();
                break;
            case 3:
                insert();
                break;
            case 4:
                delete();
                break;
            case 5:
                exit(0);
            default:
                printf("Wrong choice. Please try again.\n");
        }
    }
}

// Function definitions
void create() {
    int i;
    printf("Enter the size of the array (max %d): ", MAX);
    scanf("%d", &n);
    if (n > MAX) {
        printf("Size exceeds maximum limit. Setting size to %d.\n", MAX);
        n = MAX;
    }
    printf("Enter the elements of the array: ");
    for (i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
}

void display() {
    int i;
    if (n == 0) {
        printf("Array is empty.\n");
        return;
    }
    printf("Array elements are: ");
    for (i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

void insert() {
    int i, pos, elem;
    if (n == MAX) {
        printf("Array is full. Cannot insert new element.\n");
        return;
    }
    printf("Enter the position for the new element (0 to %d): ", n);
    scanf("%d", &pos);
    if (pos < 0 || pos > n) {
        printf("Invalid position.\n");
        return;
    }
    printf("Enter the element to be inserted: ");
    scanf("%d", &elem);

    // Shifting elements to the right to create space for the new element
    for (i = n; i > pos; i--) {
        arr[i] = arr[i - 1];
    }
    arr[pos] = elem; // Inserting new element at the specified position
    n++; // Incrementing the count of elements
}

void delete() {
    int i, pos;
    if (n == 0) {
        printf("Array is empty. Cannot delete element.\n");
        return;
    }
    printf("Enter the position of the element to be deleted (0 to %d): ", n - 1);
    scanf("%d", &pos);
    if (pos < 0 || pos >= n) {
        printf("Invalid position.\n");
        return;
    }
    int elem = arr[pos];
    printf("Element deleted is: %d\n", elem);

    // Shifting elements to the left to overwrite the deleted element
    for (i = pos; i < n - 1; i++) {
        arr[i] = arr[i + 1];
    }
    n--; // Decrementing the count of elements
}
