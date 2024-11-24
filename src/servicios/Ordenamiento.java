package servicios;
import java.util.ArrayList;
/**
 *
 * 
 */
public class Ordenamiento {
    

    public static <T extends Comparable<T>> void quickSort(ArrayList<T> list, int low, int high) {
        if (low < high) {
            // Encuentra el índice de partición
            int partitionIndex = partition(list, low, high);

            // Recursivamente ordena los elementos antes y después de la partición
            quickSort(list, low, partitionIndex - 1);
            quickSort(list, partitionIndex + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(ArrayList<T> list, int low, int high) {
        T pivot = list.get(high); // Tomar el último elemento como pivote
        int i = (low - 1); // Índice del elemento más pequeño

        for (int j = low; j < high; j++) {
            // Si el elemento actual es menor o igual que el pivote
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;

                // Intercambiar list[i] y list[j]
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        // Intercambiar list[i + 1] y list[high] (o el pivote)
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1; // Devolver el índice de partición
    }

}
