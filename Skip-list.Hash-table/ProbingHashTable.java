import java.util.Arrays;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    private final HashFactory<K> hashFactory;
    private final double maxLoadFactor;
    private int capacity;
    private int size;
    private HashFunctor<K> hashFunc;
    private Element<K, V>[] table;
    private final Element<K, V> DELETED = new Element<>(null, null);

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k; // 2^k
        this.size = 0;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new Element[this.capacity];
    }

    @Override
    public V search(K key) {
        int index = hashFunc.hash(key);
        for (int i = 0; i < capacity; i++) {
            int probeIndex = (index + i) % capacity;
            Element<K, V> element = table[probeIndex];
            if (element == null) {
                return null;
            }
            if (element != DELETED && element.key().equals(key)) {
                return element.satelliteData();
            }
        }
        return null;
    }

    @Override
    public void insert(K key, V value) {
        if ((double) size / capacity >= maxLoadFactor) {
            rehash();
        }
        int index = hashFunc.hash(key);
        for (int i = 0; i < capacity; i++) {
            int probeIndex = (index + i) % capacity;
            if (table[probeIndex] == null || table[probeIndex] == DELETED || table[probeIndex].key().equals(key)) {
                if (table[probeIndex] == null || table[probeIndex] == DELETED) {
                    size++;
                }
                table[probeIndex] = new Element<>(key, value);
                return;
            }
        }
    }

    @Override
    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        for (int i = 0; i < capacity; i++) {
            int probeIndex = (index + i) % capacity;
            Element<K, V> element = table[probeIndex];
            if (element == null) {
                return false;
            }
            if (element != DELETED && element.key().equals(key)) {
                table[probeIndex] = DELETED;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    private void rehash() {
        int newCapacity = capacity * 2;
        Element<K, V>[] newTable = new Element[newCapacity];
        HashFunctor<K> newHashFunc = hashFactory.pickHash((int)(Math.log(newCapacity) / Math.log(2)));

        for (Element<K, V> element : table) {
            if (element != null && element != DELETED) {
                int newIndex = newHashFunc.hash(element.key()) % newCapacity;
                int i = 0;
                while (newTable[(newIndex + i) % newCapacity] != null) {
                    i++;
                }
                newTable[(newIndex + i) % newCapacity] = element;
            }
        }

        table = newTable;
        capacity = newCapacity;
        hashFunc = newHashFunc;
    }
}
