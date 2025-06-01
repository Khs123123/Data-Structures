import java.util.List;
import java.util.ArrayList;

public class MyDataStructure {
    private ProbingHashTable<Integer, Integer> hashTable;
    private IndexableSkipList skipList;

    // Constructor initializes the hash table and skip list with specified parameters
    // Time Complexity: O(1)
    public MyDataStructure(int N) {
        this.hashTable = new ProbingHashTable<>(new ModularHash(), (int) (Math.log(N) / Math.log(2)), 0.75);
        this.skipList = new IndexableSkipList(0.5);
    }

    // Inserts a value if it doesn't already exist in the data structure
    // Time Complexity: O(log N)
    public boolean insert(int value) {
        if (contains(value)) { // O(1)
            return false;
        }
        hashTable.insert(value, value); // O(1)
        skipList.insert(value); // O(log N)
        return true;
    }

    // Deletes a value if it exists in the data structure
    // Time Complexity: O(log N)
    public boolean delete(int value) {
        if (!contains(value)) { // O(1)
            return false;
        }
        hashTable.delete(value); // O(1)
        AbstractSkipList.SkipListNode node = skipList.search(value); // O(log N)
        if (node != null) {
            skipList.delete(node); // O(log N)
        }
        return true;
    }

    // Checks if a value exists in the data structure
    // Time Complexity: O(1)
    public boolean contains(int value) {
        return hashTable.search(value) != null; // O(1)
    }

    // Finds the rank of a value in the skip list
    // Time Complexity: O(log N)
    public int rank(int value) {
        return skipList.rank(value); // O(log N)
    }

    // Selects the value at a given index in the skip list
    // Time Complexity: O(log N)
    public int select(int index) {
        return skipList.select(index); // O(log N)
    }

    // Returns a list of values within a specified range in the skip list
    // Time Complexity: O(log N + M), where M is the number of nodes in range
    public List<Integer> range(int low, int high) {
        List<Integer> rangeList = new ArrayList<>();
        AbstractSkipList.SkipListNode node = skipList.search(low); // O(log N)
        if (node == null) {
            return null;
        }
        while (node != null && node.key() <= high) { // O(M), where M is the number of nodes in range
            rangeList.add(node.key());
            node = skipList.successor(node); // O(1)
        }
        return rangeList;
    }
}
