#include <stdio.h>

// Function declaration/prototype
int binarySearch(int arr[], int low, int high, int key);

// Driver code
int main(void) {
    int n, x, i;

    // Input the number of elements in the array
    printf("Enter the number of elements in the array: ");
    scanf("%d", &n);
    
    int arr[n]; // Declare the array with size n

    // Input the elements of the array
    printf("Enter %d elements in the array (sorted): \n", n);
    for (i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }

    // Input the element to be searched
    printf("Enter the element to be searched: ");
    scanf("%d", &x);

    // Perform binary search
    int result = binarySearch(arr, 0, n - 1, x);

    // Output the result
    (result == -1) ? printf("Element is not present in array\n")
                   : printf("Element is present at index %d\n", result);

    return 0;
}

/* A recursive binary search function. It returns the location of key
in given array arr[low, ..., high] if present, otherwise -1 */
int binarySearch(int arr[], int low, int high, int key) {
    if (low <= high) {
        int mid = (low + high) / 2; // Calculate mid index

        // If the element is present at the middle itself
        if (arr[mid] == key)
            return mid;

        // If element is smaller than mid, then it can only be present in left subarray
        if (arr[mid] > key)
            return binarySearch(arr, low, mid - 1, key);

        // Else the element can only be present in right subarray
        return binarySearch(arr, mid + 1, high, key);
    }

    // We reach here when the element is not present in the array
    return -1;
}
