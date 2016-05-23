/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selectionproblem;
import java.util.Random;
import java.util.Arrays;

public class SelectionProblem {
    
    static final int N = 10;
    static final int MAX = 1000;
    static final int MIN = 0;
    static final int M = 10;
   
    public static void main(String args[]) {
        final int k = 1;  // 1, N/4, N/2, 3*N/4, N
        int kthElement = 0;
        long start, end, nanoseconds = 0;   // start and end timers
        double avgNanoseconds, milliseconds = 0.0; 
        
        int[] array = new int[N];
        Random rand = new Random();     // random generator
        for(int i = 0; i < N; i++)      // fill array with random integers
            array[i] = rand.nextInt(MAX - MIN) + MIN;
        
        int[] tempArray = new int[N];   // a temp array
        
        ////////////// Algorithm 1 ////////////////////
        System.arraycopy(array, 0, tempArray, 0, array.length); // copy array
       
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            kthElement = SelectKth1(tempArray, 0, N-1, k);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Algorithm 1(MergeSort) average runtime: ");
        System.out.printf("%.5f", milliseconds);
        System.out.println();
        System.out.println(k + "th smallest element: "  + kthElement);
        
        ///////////// Algorithm 2 ///////////////////
        System.arraycopy(array, 0, tempArray, 0, array.length); // copy array
        
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            kthElement = SelectKth2(tempArray, 0, N-1, k);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Algorithm 2(Partition Iterative) average runtime: ");
        System.out.printf("%.5f", milliseconds);
        System.out.println();
        System.out.println(k + "th smallest element: " + kthElement);
        
        ///////////// Algorithm 3 ///////////////////
        System.arraycopy(array, 0, tempArray, 0, array.length); // copy array
        
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            kthElement = SelectKth3(tempArray, 0, N-1, k);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Algorithm 3(Partition Recursive) average runtime: ");
        System.out.printf("%.5f", milliseconds);
        System.out.println();
        System.out.println(k + "th smallest element: " + kthElement);
        
        ///////////// Algorithm 4 ///////////////////
        System.arraycopy(array, 0, tempArray, 0, array.length); // copy array
        
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            kthElement = SelectKth4(tempArray, 0, N-1, k);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Algorithm 4(MedianOfMedians) average runtime: ");
        System.out.printf("%.5f", milliseconds);
        System.out.println();
        System.out.println(k + "th smallest element: " + kthElement);
        
//        System.out.println();
//        System.out.println("Before sorted ");
//        for(int i : array) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
//        System.out.println();
//        
//        System.out.println("After sorted ");
//        for(int i : tempArray) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
//        System.out.println();
    }

    public static int SelectKth1(int[] array, int low, int high, int k) { // mergesort
        if (low < high) {
            int mid = low + (high - low) / 2;       // middle index
            SelectKth1(array, low, mid, k);            // recursive call left half
            SelectKth1(array, mid + 1, high, k);        // recursive call right half
            Merge(low, mid, high, array);           // call Merge
        }
        return array[k-1];
    }
 
    public static int SelectKth2(int[] array, int low, int high, int k) {   // from textbook
        int p = Partition1(array, low, high);

        while(p != k-1) {
            if(k-1 < p) 
                high = p-1;
            else
                low = p+1;

            p = Partition1(array, low, high);
        }
        return array[p];
    }  
 
    public static int SelectKth3(int[] array, int low, int high, int k) {   // from textbook
        int pivotPoint;

        if(low == high) 
            return array[low];
        else {
            pivotPoint = Partition1(array, low, high);
            if(k-1 == pivotPoint)
                return array[pivotPoint];
            else if(k-1 < pivotPoint)
                return SelectKth3(array, low, pivotPoint-1, k);
            else
                return SelectKth3(array, pivotPoint+1, high, k);
        }
    }  

    public static int Select(int n, int[] array, int k) {
        return SelectKth4(array, 0, n, k);
    }

    public static int SelectKth4(int[] array, int low, int high, int k) {    
        if(k > 0 && k <= high-low+1) {    // if k < number of elemnts in array
            int n = high-low+1;              // number of elements in array

            // divide into groups of 5, find median, store in median array
            int i;
            int[] median = new int[(n+4)/5];    
            for(i = 0; i < n/5; i++) {
                median[i] = getMedian(array, low+i*5, 5);
            }
            if(i*5 < n) {         // for last group with less than 5 elements
                median[i] = getMedian(array, low+i*5, n%5);
                i++;
            }    

            int medianOfMedians = (i == 1) ? median[i-1] 
                    : SelectKth4(median, 0, i-1, i/2);

            int pos = Partition2(array, low, high, medianOfMedians);

            if(pos-low == k-1)        // if position is same as k
                return array[pos];
            if(pos-low > k-1)     // if position is more, recursive call left
                return SelectKth4(array, low, pos-1, k);
            // else recursive call right
            return SelectKth4(array, pos+1, high, k-pos+low-1);
        }
        return N;   // if k > size of array
    }

    public static void Merge(int low, int mid, int high, int[] array) {
            int[] U = new int[N];           // temporary array holder
            for (int i = low; i <= high; i++) { // copy over to U array
                U[i] = array[i];
            }
            int i = low, j = mid + 1, k = low;
            while(i <= mid && j <= high) {     // place in array sorted order
                if (U[i] <= U[j]) {
                    array[k] = U[i];
                    i++;
                } else {
                    array[k] = U[j];
                    j++;
                }
                k++;
            }
            while(i <= mid) {           // fill in the rest if first half
                array[k] = U[i];                        
                k++;
                i++;
            }
        }
    // Partitions array so all elements < pivot is on left 
    // and elements > pivot on right, returns pivot index
    public static int Partition1(int[] array, int low, int high) {
        int i, j, temp;
        int pivotItem;

        pivotItem = array[low];
        j = low;

        for(i = low+1; i <= high; i++) {
            if(array[i] < pivotItem) {
                j++;
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        temp = array[low];
        array[low] = array[j];
        array[j] = temp;

        return j;
    }

    public static int Partition2(int[] array, int low, int high, int x) {
        int i, temp;
        for(i=low; i < high; i++) // search for medianOfMedians and put at end
            if(array[i] == x)
                break;

        temp = array[i];            // swap array[i] and array[high]
        array[i] = array[high];
        array[high] = temp;

        i = low;
        for(int j = low; j <= high-1; j++) {
            if(array[j] <= x) {
                temp = array[i];            // swap array[i] and array[j]
                array[i] = array[j];
                array[j] = temp;
                i++;
            }
        }
        temp = array[i];            // swap array[i] and array[high]
        array[i] = array[high];
        array[high] = temp;

        return i;
    }
    // find median of array of size 5
    public static int getMedian(int[] array, int low, int n) {
        Arrays.sort(array, low, low+n);     // sort array
        return array[(low + (low+n))/2];      // return middle element     
    }
}

