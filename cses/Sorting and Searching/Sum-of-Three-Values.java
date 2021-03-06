import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class Solution {
    private static final int IO_BUFFERS = 1024 * 1024;
    private static FastReader reader = new FastReader();
    private static FastWriter writer = new FastWriter();

    private static int N, X;
    private static ALP a;

    private static void readInput() throws IOException {
        N = reader.nextInt();
        X = reader.nextInt();
        a = new ALP();
        for (int i = 0; i < N; i++) a.add(new Pair(reader.nextInt(), i));
    }

    private static long sum(int p1, int p2, int p3) {
        long sum = 0;
        sum += a.get(p1).v1;
        sum += a.get(p2).v1;
        sum += a.get(p3).v1;
        return sum;
    }

    private static void solve() throws IOException {
        if (N < 3) {
            writer.println("IMPOSSIBLE");
            return;
        }

        Collections.sort(a, (e1, e2) -> Integer.compare(e1.v1, e2.v1));

        for(int i = 0; i < N - 2; i++) {
            int p1 = i;
            int p2 = p1 + 1;
            int p3 = N - 1;
            long s = 0;

            while (p2 < p3) {
                s =sum(p1, p2, p3);
                if (s == X) {
                    writer.print("%d %d %d", a.get(p1).v2 + 1, a.get(p2).v2 + 1, a.get(p3).v2 + 1);
                    return;
                }
                if (s < X) p2++;
                else p3--;
            }
        }

        writer.println("IMPOSSIBLE");
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            int T = 1;
            for (int t = 0; t < T; t++) {
                readInput();
                solve();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("WRONG INPUT");
        }
        System.err.println("\ntime: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /** Utility class to calculate stats on collection quickly * */
    static class Stats {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        public Stats(List<? extends Number> list) {
            for (Number el : list) process(el.longValue());
        }

        private void process(long el) {
            sum += el;
            min = Math.min(min, el);
            max = Math.max(max, el);
        }
    }

    /** Output processing class for competitions */
    static class FastWriter {
        private final StringBuilder out;

        public FastWriter() {
            out = new StringBuilder(Solution.IO_BUFFERS);
        }

        public FastWriter print(Object object) {
            out.append(object);
            return this;
        }

        public FastWriter print(String format, Object... args) {
            out.append(String.format(format, args));
            return this;
        }

        public FastWriter println(Object object) {
            out.append(object).append("\n");
            return this;
        }

        public void close() throws IOException {
            System.out.print(out);
        }
    }

    /** Input processing class for competitions */
    static class FastReader {
        private final int BUFFER_SIZE = 1 << 24;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public FastReader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public FastReader(String file_name) throws IOException {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg) return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg) return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg) return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null) return;
            din.close();
        }

        // TODO
        //    String[] fillStringArray(int n) throws IOException {
        //      String[] array = new String[n];
        //      for (int i = 0; i < n; i++) array[i] = next();
        //      return array;
        //    }

        ArrayList<Pair> fillPairList(int n) throws IOException {
            ArrayList<Pair> list = new ArrayList<Pair>(n);
            for (int i = 0; i < n; i++) list.add(new Pair(nextInt(), nextInt()));
            return list;
        }

        int[] fillIntegerArray(int n) throws IOException {
            int[] array = new int[n];
            for (int i = 0; i < n; i++) array[i] = nextInt();
            return array;
        }

        long[] fillLongArray(int n) throws IOException {
            long[] array = new long[n];
            for (int i = 0; i < n; i++) array[i] = nextLong();
            return array;
        }

        <T extends List<Long>> T fillList(T list, int n) throws IOException {
            for (int i = 0; i < n; i++) list.add(nextLong());
            return list;
        }
    }

    /** Multiset utility class. It's just like TreeSet, but allows duplicate elements * */
    static class Multiset extends TreeMap<Long, Long> {
        void add(Long value) {
            Long count = get(value);
            put(value, count == null ? 1 : count + 1);
        }

        @java.lang.Override
        public Long remove(Object key) {
            if (!containsKey(key)) return null;

            Long count = get(key);
            if (count > 1) return put((Long) key, count - 1);

            return super.remove(key);
        }

        public Long count() {
            return entrySet().stream().map((v) -> v.getValue()).reduce(0L, Long::sum);
        }
    }

    /** Usefull utility class * */
    static class IMultiset extends TreeMap<Integer, Integer> {
        void add(Integer value) {
            Integer count = get(value);
            put(value, count == null ? 1 : count + 1);
        }

        @java.lang.Override
        public Integer remove(Object key) {
            if (!containsKey(key)) return null;

            Integer count = get(key);
            if (count > 1) return put((Integer) key, count - 1);

            return super.remove(key);
        }

        public Integer count() {
            return entrySet().stream().map((v) -> v.getValue()).reduce(0, Integer::sum);
        }
    }

    /** Alias for ArrayList<Integer> */
    static class ALI extends ArrayList<Integer> {}

    /** Alias for ArrayList<Long> */
    static class ALL extends ArrayList<Long> {}

    /** Alias for ArrayList<Pair> */
    static class ALP extends ArrayList<Pair> {}

    /** General purpose Quad utility class */
    static class Quad {
        int v1;
        int v2;
        int v3;
        int v4;

        public Quad(int v1, int v2, int v3, int v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "{" + v1 + ", " + v2 + ", " + v3 + ", " + v4 + " }";
        }
    }

    /** General purpose Triple utility class */
    static class Triple {
        int v1, v2, v3;

        public Triple(int v1, int v2, int v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v2 = v3;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "{" + v1 + ", " + v2 + ", " + v3 + " }";
        }
    }

    /** General purpose Pair utility class */
    static class Pair {
        int v1;
        int v2;

        public Pair(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "{" + v1 + ", " + v2 + " }";
        }
    }
}
