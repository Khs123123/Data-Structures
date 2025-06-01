import java.util.NoSuchElementException;

public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;
    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
    }

    @Override
    public SkipListNode find(int key) {
        SkipListNode current = head;
        for (int level = head.height(); 0 <= level; level--) {
            while (current.getNext(level) != null && current.getNext(level).key() < key) {
                current = current.getNext(level);
            }
        }
        current = current.getNext(0);
        if (current.key() == key) {
            return current;
        } else {
            return current.getPrev(0);
        }
    }
    @Override
    public int generateHeight() {
        int height = 0;
        while (Math.random() < probability ) {
            height++;
        }
        return height;
    }

    public int rank(int key) {
        SkipListNode node = head;
        int rank = 0;

        for (int level = head.height(); level >= 0; level--) {
            while (node.getNext(level) != tail && node.getNext(level).key() < key) {
                // Accumulate sizes as we traverse
                rank += 1; // Increment for each node skipped
                node = node.getNext(level);
            }
        }

        // Verify if the exact key exists
        node = node.getNext(0); // Move to the next node at level 0
        if (node == null || node.key() != key) {
            throw new NoSuchElementException("Key not found in skip list");
        }

        return rank + 1; // Rank starts from 1
    }






    public int select(int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be greater than 0");
        }

        SkipListNode node = head;
        int cumulativeIndex = 0;

        for (int level = head.height(); level >= 0; level--) {
            while (node.getNext(level) != tail &&
                    (cumulativeIndex + 1) < index) {
                cumulativeIndex++;
                node = node.getNext(level);
            }
        }

        // Move to the next node at level 0
        node = node.getNext(0);
        if (node == null || cumulativeIndex + 1 != index) {
            throw new NoSuchElementException("Index exceeds bounds of the skip list");
        }

        return node.key();
    }

}
