import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    private Random rand;
    private HashingUtils utils;

    public ModularHash() {
        rand = new Random(System.currentTimeMillis());
        utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int k){
            this.m = (int) Math.pow(2, k); // m = 2^k
            long suspectPrime = utils.genLong((long)Integer.MAX_VALUE + 1,Long.MAX_VALUE);
            while (((suspectPrime & 1) == 0) || !utils.runMillerRabinTest(suspectPrime, 50)){
                suspectPrime = utils.genLong((long)Integer.MAX_VALUE + 1,Long.MAX_VALUE);
            }

            p = suspectPrime;
            this.a = rand.nextInt(Integer.MAX_VALUE - 1) + 1; // a is in the range [1, Integer.MAX_VALUE)
            this.b = rand.nextInt(Integer.MAX_VALUE); // b is in the range [0, Integer.MAX_VALUE)
        }

        @Override
        public int hash(Integer key) {
            long hashValue = ((long) a * key + b) % p;
            return (int) HashingUtils.mod(hashValue, m); // Ensure the result is positive
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
