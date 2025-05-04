#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;
using namespace std::chrono;

void printArray(const vector<int>& arr) {
    for (int num : arr)
        cout << num << " ";
    cout << endl;
}

void merge(vector<int>& arr, int left, int mid, int right) {
    int n1 = mid - left + 1, n2 = right - mid;
    vector<int> L(n1), R(n2);
    
    for (int i = 0; i < n1; i++) L[i] = arr[left + i];
    for (int i = 0; i < n2; i++) R[i] = arr[mid + 1 + i];

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) 
        arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
    
    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];
}

void mergeSortRecursive(vector<int>& arr, int left, int right) {
    if (left >= right) return;
    int mid = left + (right - left) / 2;
    mergeSortRecursive(arr, left, mid);
    mergeSortRecursive(arr, mid + 1, right);
    merge(arr, left, mid, right);
}

void mergeSortIterative(vector<int>& arr) {
    int n = arr.size();
    for (int size = 1; size < n; size *= 2) {
        for (int left = 0; left < n - 1; left += 2 * size) {
            int mid = min(left + size - 1, n - 1);
            int right = min(left + 2 * size - 1, n - 1);
            merge(arr, left, mid, right);
        }
    }
}

int partition(vector<int>& arr, int low, int high) {
    int pivot = arr[low], i = low + 1, j = high;
    while (i <= j) {
        while (i <= j && arr[i] <= pivot) i++;
        while (i <= j && arr[j] > pivot) j--;
        if (i < j) swap(arr[i], arr[j]);
    }
    swap(arr[low], arr[j]);
    return j;
}

void quickSortNormal(vector<int>& arr, int low, int high) {
    if (low < high) {
        int pivotIndex = partition(arr, low, high);
        quickSortNormal(arr, low, pivotIndex - 1);
        quickSortNormal(arr, pivotIndex + 1, high);
    }
}

int medianOfThree(vector<int>& arr, int low, int high) {
    int mid = low + (high - low) / 2;
    vector<int> candidates = {arr[low], arr[mid], arr[high]};
    sort(candidates.begin(), candidates.end());
    return candidates[1];
}

int partitionModified(vector<int>& arr, int low, int high) {
    int pivot = medianOfThree(arr, low, high);
    int pivotIndex = (arr[low] == pivot) ? low : (arr[high] == pivot) ? high : low + (high - low) / 2;
    swap(arr[pivotIndex], arr[low]);
    
    return partition(arr, low, high);
}

void quickSortModified(vector<int>& arr, int low, int high) {
    if (low < high) {
        int pivotIndex = partitionModified(arr, low, high);
        quickSortModified(arr, low, pivotIndex - 1);
        quickSortModified(arr, pivotIndex + 1, high);
    }
}

int main() {
    vector<int> originalArr = {34, 7, 23, 32, 5, 62, 32, 7, 4};

    vector<int> arr = originalArr;
    cout << "\nOriginal Array: ";
    printArray(arr);
    auto start = high_resolution_clock::now();
    mergeSortIterative(arr);
    auto stop = high_resolution_clock::now();
    auto duration = duration_cast<nanoseconds>(stop - start);
    
    cout << "\nSorted using Iterative Merge Sort: ";
    printArray(arr);
    cout << "Execution Time: " << duration.count() << " ns\n";

    arr = originalArr;
    start = high_resolution_clock::now();
    mergeSortRecursive(arr, 0, arr.size() - 1);
    stop = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(stop - start);

    cout << "\nSorted using Recursive Merge Sort: ";
    printArray(arr);
    cout << "Execution Time: " << duration.count() << " ns\n";

    arr = originalArr;
    start = high_resolution_clock::now();
    quickSortNormal(arr, 0, arr.size() - 1);
    stop = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(stop - start);

    cout << "\nSorted using Normal Quick Sort: ";
    printArray(arr);
    cout << "Execution Time: " << duration.count() << " ns\n";

    arr = originalArr;
    start = high_resolution_clock::now();
    quickSortModified(arr, 0, arr.size() - 1);
    stop = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(stop - start);

    cout << "\nSorted using Quick Sort (Median of Three): ";
    printArray(arr);
    cout << "Execution Time: " << duration.count() << " ns\n";

    return 0;
}
