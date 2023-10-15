package Database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
public class HeapSort_ById {
    public static String[][] Buildheap_AndSort(String filePath) throws IOException {
        BufferedReader bufferedReader_1 = new BufferedReader(new FileReader(filePath));
        BufferedReader bufferedReader_2 = new BufferedReader(new FileReader(filePath));
        bufferedReader_1.readLine();
        bufferedReader_2.readLine();
        String lines;
        int j = 0;
        while((lines = bufferedReader_1.readLine())!=null){
            j++;
        }
        String arr[][] = new String[2][j];
        int k = 0;
        while((lines = bufferedReader_2.readLine())!=null){
            String[] splitArray = lines.split(";");
            int id = Integer.parseInt(splitArray[0]);
            String movieTitle = (splitArray.length > 1 && !splitArray[1].trim().equals("null")) ? splitArray[1].trim() : "";
            String country = (splitArray.length > 2 && !splitArray[2].trim().equals("null")) ? splitArray[2].trim() : "";
            //int yearReleased = (splitArray.length > 3 && !splitArray[3].trim().equals("null")) ? Integer.parseInt(splitArray[3].trim()) : 0;
            //int runtime = (splitArray.length > 4 && !splitArray[4].trim().equals("null")) ? Integer.parseInt(splitArray[4].trim()) : 0;
            String value = String.valueOf(id);
            String result = value.concat("; ").concat("movieTitle: ").concat(movieTitle).concat("; ").concat("country: ").concat(country).concat("; ");
            arr[0][k] = splitArray[0];
            arr[1][k] = result;
            k++;
        }

        for(int i = k / 2 - 1;i>=0;i--){
            heapify(arr,k,i);
        }//Building heap
        for(int i = k - 1;i >= 0;i--){
            //Move current root to the end
            int temp_1 = Integer.parseInt(arr[0][0]);String temp_2 = arr[1][0];
            arr[0][0] = arr[0][i];arr[1][0] = arr[1][i];
            arr[0][i] = String.valueOf(temp_1);arr[1][i] = temp_2;
            heapify(arr,i,0);
        }
        return arr;
    }

    public static void heapify(String[][] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && Integer.parseInt(arr[0][l]) > Integer.parseInt(arr[0][largest])) {
            largest = l;
        }
        if (r < n && Integer.parseInt(arr[0][r]) > Integer.parseInt(arr[0][largest])) {
            largest = r;
        }
        if (largest != i) {
            int swap_1 = Integer.parseInt(arr[0][i]);
            String swap_2 = arr[1][i];
            arr[0][i] = arr[0][largest];
            arr[1][i] = arr[1][largest];
            arr[0][largest] = String.valueOf(swap_1);
            arr[1][largest] = swap_2;
            heapify(arr, n, largest);
        }
    }
}
