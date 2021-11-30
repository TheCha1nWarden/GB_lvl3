import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainTest {


    @ParameterizedTest
    @MethodSource("dataForTestCorrectValueTask2")
    public void shouldReturnCorrectIntArrayInTask2_whenIntArrayPassed(int[] result, int[] inputArray) {
        Assertions.assertArrayEquals(result, Main.task2(inputArray));
    }

    public static Stream<Arguments> dataForTestCorrectValueTask2() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] {1, 7}, new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7}));
        out.add(Arguments.arguments(new int[] {2, 5, 9, 2, 3, 2, 1, 7}, new int[] {4, 2, 5, 9, 2, 3, 2, 1, 7}));
        out.add(Arguments.arguments(new int[] {3, 0, 1, 6}, new int[] {3, 2, 7, 9, 4, 3, 0, 1, 6}));
        out.add(Arguments.arguments(new int[] {}, new int[] {1, 9, 0, 9, 8, 3, 2, 1, 4}));
        return out.stream();
    }


    @ParameterizedTest
    @MethodSource("dataForTestThrowRuntimeExceptionAtTask2")
    public void shouldThrowRuntimeExceptionInTask2_whenInputArrayHaventNumber4(int[] inputArray) {
        Assertions.assertThrows(RuntimeException.class, () -> {Main.task2(inputArray);});
    }

    public static Stream<Arguments> dataForTestThrowRuntimeExceptionAtTask2() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] {1, 2, 2, 3, 1, 7}));
        out.add(Arguments.arguments(new int[] {1}));
        out.add(Arguments.arguments(new int[] {}));
        return out.stream();
    }

    @Test
    public void shouldThrowNullPointerExceptionInTask2_whenNullPassed() {
        Assertions.assertThrows(NullPointerException.class, () -> {Main.task2(null);});
    }

    @ParameterizedTest
    @MethodSource("dataForTestCorrectValueTask3")
    public void shouldReturnCorrectBooleanInTask3_whenIntArrayPassed(boolean result, int[] inputArray) {
        Assertions.assertEquals(result, Main.task3(inputArray));
    }

    public static Stream<Arguments> dataForTestCorrectValueTask3() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(true, new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7}));
        out.add(Arguments.arguments(true, new int[] {4, 2, 5, 9, 2, 3, 2, 9, 7}));
        out.add(Arguments.arguments(true, new int[] {3, 2, 7, 9, 2, 3, 0, 1, 6}));
        out.add(Arguments.arguments(false, new int[] {5, 9, 0, 9, 8, 3, 2, 9, 7}));
        out.add(Arguments.arguments(false, new int[] {}));
        return out.stream();
    }

    @Test
    public void shouldThrowNullPointerExceptionInTask3_whenNullPassed() {
        Assertions.assertThrows(NullPointerException.class, () -> {Main.task3(null);});
    }







}
