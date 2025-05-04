#include<bits/stdc++.h>

using namespace std;
using namespace chrono;

int partition(vector<int> &arr, int low, int high) {
  int pivot = arr[high];
  int i = low - 1;

  for (int j = low; j < high; ++j) {
    if (arr[j] <= pivot) {
      ++i;
      swap(arr[i], arr[j]);
    }
  }
  swap(arr[i + 1], arr[high]);
  return i + 1;
}

void quicksort(vector<int> &arr, int low, int high) {
  if (low < high) {
    int pi = partition(arr, low, high);
    quicksort(arr, low, pi - 1);
    quicksort(arr, pi + 1, high);
  }
}

void printArray(const vector<int>& arr) {
  for (int num : arr) {
    cout << num << " ";
  }
  cout << endl;
}

int main() {
  int n;
  cout << "Enter the number of elements: ";
  cin >> n;

  vector<int> array(n);
  cout << "Enter the elements: ";
  for (int i = 0; i < n; i++) {
    cin >> array[i];
  }

  cout << "\nInitial array: ";
  printArray(array);

  auto start = chrono::high_resolution_clock::now();
  quicksort(array, 0, n - 1);
  auto end = chrono::high_resolution_clock::now();
  auto elapsedTime = chrono::duration_cast<chrono::nanoseconds>(end - start);

  cout << "Sorted array Using Quicksort: ";
  printArray(array);

  cout << "Time taken: " << elapsedTime.count() << " nanoseconds\n";

  return 0;
}
