public class SkipListUtils {

    public static double calculateExpectedHeight(double p) {
        return 1.0 / p;
    }

    public static boolean[] changedMethodsArray() {
        return new boolean[] {true, true, true, false, false, false, false, false, false}; // insert and delete are modified
    }
}
