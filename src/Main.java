import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static <T> void swap(T[] array, int firstIndex, int secondIndex) {
        try {
            T tmp = array[firstIndex];
            array[firstIndex] = array[secondIndex];
            array[secondIndex] = tmp;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("wrong index");
        }
    }

    static <T> ArrayList<T> getArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static void main(String[] args) {

    }

}
