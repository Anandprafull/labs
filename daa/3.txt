#include<bits/stdc++.h>

using namespace std;
using namespace chrono;

void merge(vector<int> &arr, int left, int mid, int right) {
  int n1 = mid - left + 1;
  int n2 = right - mid;

  vector<int> leftArr(n1);
  vector<int> rightArr(n2);

  for (int i = 0; i < n1; ++i) {
    leftArr[i] = arr[left + i];
  }
  for (int j = 0; j < n2; ++j) {
    rightArr[j] = arr[mid + 1 + j];
  }

  int i = 0, j = 0, k = left;

  while (i < n1 && j < n2) {
    if (leftArr[i] <= rightArr[j]) {
      arr[k] = leftArr[i];
      ++i;
    } else {
      arr[k] = rightArr[j];
      ++j;
    }
    ++k;
  }

  while (i < n1) {
    arr[k] = leftArr[i];
    ++i;
    ++k;
  }

  while (j < n2) {
    arr[k] = rightArr[j];
    ++j;
    ++k;
  }
}

void mergeSort(vector<int> &arr, int left, int right) {
  if (left < right) {
    int mid = left + (right - left) / 2;

    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);

    merge(arr, left, mid, right);
  }
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

  cout << "Initial array: ";
  for (int i = 0; i < n; i++) {
    cout << array[i] << " ";
  }
  cout << endl;

  auto start = high_resolution_clock::now();
  mergeSort(array, 0, array.size() - 1);
  auto end = high_resolution_clock::now();
  auto elapsedTime = duration_cast<nanoseconds>(end - start);

  cout << "Sorted array Using Merge Sort: ";
  for (int num : array) {
    cout << num << " ";
  }
  cout << "\nTime taken: " << elapsedTime.count() << " nanoseconds\n";

  return 0;
}
