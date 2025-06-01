import java.util.Collections; // can be useful
import java.util.Random;

public class HashingExperimentUtils {
    final private static int k = 16;

    public static double[] measureInsertionsProbing() {
        double[] alphas = {1.0 / 2, 3.0 / 4, 7.0 / 8, 15.0 / 16};
        double[] averageInsertionTimes = new double[alphas.length];
        Random random = new Random();
        final int size = (int) Math.pow(2, k);

        for (int i = 0; i < alphas.length; i++) {
            ProbingHashTable<Integer, Integer> hashTable = new ProbingHashTable<>(new ModularHash(), k, alphas[i]);
            int numItems = (int) (alphas[i] * size);
            long totalInsertionTime = 0;

            for (int j = 0; j < numItems; j++) {
                int key = random.nextInt();
                long startTime = System.nanoTime();
                hashTable.insert(key, key);
                long endTime = System.nanoTime();
                totalInsertionTime += (endTime - startTime);
            }

            averageInsertionTimes[i] = (double) totalInsertionTime / numItems;
        }

        return averageInsertionTimes;
    }

    public static double[] measureSearchesProbing() {
        double[] alphas = {1.0 / 2, 3.0 / 4, 7.0 / 8, 15.0 / 16};
        double[] averageSearchTimes = new double[alphas.length];
        Random random = new Random();
        final int size = (int) Math.pow(2, k);

        for (int i = 0; i < alphas.length; i++) {
            ProbingHashTable<Integer, Integer> hashTable = new ProbingHashTable<>(new ModularHash(), k, alphas[i]);
            int numItems = (int) (alphas[i] * size);

            Integer[] keys = new Integer[numItems];
            for (int j = 0; j < numItems; j++) {
                int key = random.nextInt();
                hashTable.insert(key, key);
                keys[j] = key;
            }

            long totalSearchTime = 0;
            for (int j = 0; j < numItems; j++) {
                int key;
                if (j % 2 == 0) {
                    key = keys[random.nextInt(numItems)];
                } else {
                    key = random.nextInt();
                }
                long startTime = System.nanoTime();
                hashTable.search(key);
                long endTime = System.nanoTime();
                totalSearchTime += (endTime - startTime);
            }

            averageSearchTimes[i] = (double) totalSearchTime / numItems;
        }

        return averageSearchTimes;
    }

    public static double[] measureInsertionsChaining() {
        double[] alphas = {1.0 / 2, 3.0 / 4, 1.0, 3.0 / 2, 2.0};
        double[] averageInsertionTimes = new double[alphas.length];
        Random random = new Random();
        final int size = (int) Math.pow(2, k);

        for (int i = 0; i < alphas.length; i++) {
            ChainedHashTable<Integer, Integer> hashTable = new ChainedHashTable<>(new ModularHash(), k, alphas[i]);
            int numItems = (int) (alphas[i] * size);
            long totalInsertionTime = 0;

            for (int j = 0; j < numItems; j++) {
                int key = random.nextInt();
                long startTime = System.nanoTime();
                hashTable.insert(key, key);
                long endTime = System.nanoTime();
                totalInsertionTime += (endTime - startTime);
            }

            averageInsertionTimes[i] = (double) totalInsertionTime / numItems;
        }

        return averageInsertionTimes;
    }

    public static double[] measureSearchesChaining() {
        double[] alphas = {1.0 / 2, 3.0 / 4, 1.0, 3.0 / 2, 2.0};
        double[] averageSearchTimes = new double[alphas.length];
        Random random = new Random();
        final int size = (int) Math.pow(2, k);

        for (int i = 0; i < alphas.length; i++) {
            ChainedHashTable<Integer, Integer> hashTable = new ChainedHashTable<>(new ModularHash(), k, alphas[i]);
            int numItems = (int) (alphas[i] * size);

            Integer[] keys = new Integer[numItems];
            for (int j = 0; j < numItems; j++) {
                int key = random.nextInt();
                hashTable.insert(key, key);
                keys[j] = key;
            }

            long totalSearchTime = 0;
            for (int j = 0; j < numItems; j++) {
                int key;
                if (j % 2 == 0) {
                    key = keys[random.nextInt(numItems)];
                } else {
                    key = random.nextInt();
                }
                long startTime = System.nanoTime();
                hashTable.search(key);
                long endTime = System.nanoTime();
                totalSearchTime += (endTime - startTime);
            }

            averageSearchTimes[i] = (double) totalSearchTime / numItems;
        }

        return averageSearchTimes;
    }
}

