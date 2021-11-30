import java.util.Arrays;

public class Main {
    public static int[] task2(int[] array) {
        int[] outputArray = null;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 4) {
                outputArray = new int[array.length - i - 1];
                for (int j = 0; j < outputArray.length; j++) {
                    outputArray[j] = array[i + 1 + j];
                }
                return outputArray;
            }
        }
            throw new RuntimeException("Number 4 not found");
    }

    public static boolean task3(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1 || array[i] == 4) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(task2(new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7})));
        System.out.println(task3(new int[] {3, 2, 2, 5, 2, 3, 2, 7, 7}));
    }
}
