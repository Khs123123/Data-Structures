import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private int size;
    private HashFunctor<K> hashFunc;
    private List<Element<K,V>>[] table;

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k; // 2^k
        this.size = 0;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new List[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            this.table[i] = new LinkedList<>();
        }
    }

    @Override
    public V search(K key) {
        int index = hashFunc.hash(key);
        List<Element<K, V>> bucket = table[index];
        for (Element<K, V> element : bucket) {
            if (element.key().equals(key)) {
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
        List<Element<K, V>> bucket = table[index];
        for (Element<K, V> element : bucket) {
            if (element.key().equals(key)) {
                element.setSatData(value);
                return;
            }
        }
        bucket.add(new Element<>(key, value));
        size++;
    }

    @Override
    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        List<Element<K, V>> bucket = table[index];
        for (Element<K, V> element : bucket) {
            if (element.key().equals(key)) {
                bucket.remove(element);
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
        List<Element<K, V>>[] newTable = new List[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList<>();
        }

        HashFunctor<K> newHashFunc = hashFactory.pickHash((int)(Math.log(newCapacity) / Math.log(2)));
        for (List<Element<K, V>> bucket : table) {
            for (Element<K, V> element : bucket) {
                int newIndex = newHashFunc.hash(element.key());
                newTable[newIndex].add(element);
            }
        }

        table = newTable;
        capacity = newCapacity;
        hashFunc = newHashFunc;
    }
}