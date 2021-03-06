import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Solution {
  private static final int IO_BUFFERS = 128 * 1024;
  private static FastReader reader = new FastReader();
  private static FastWriter writer = new FastWriter();

  private static int N, K;
  private static AL<Pair> movies;

  private static void readInput() throws IOException {
    N = reader.nextInt();
    K = reader.nextInt();
    movies = new AL<Pair>();
    for (int i = 0; i < N; i++) {
      int start = reader.nextInt();
      int end = reader.nextInt();
      movies.add(new Pair(end, start));
    }
  }

  private static void solve() {
    Collections.sort(movies);
    TMultiset<Integer> ms = new TMultiset<>();
    for (int i = 0; i < K; i++) ms.add(0);

    int counter = 0;

    for (Pair movie : movies) {
      Integer floor = ms.floorKey(movie.v2);
      if (floor == null) {
        continue;
      }

      ms.remove(floor);
      ms.add(movie.v1);

      counter++;
    }

    writer.print(counter);
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
    private DataInputStream din;
    private byte[] buffer;
    private int bufferPointer, bytesRead;

    public FastReader() {
      din = new DataInputStream(System.in);
      buffer = new byte[Solution.IO_BUFFERS];
      bufferPointer = bytesRead = 0;
    }

    public FastReader(String file_name) throws IOException {
      din = new DataInputStream(new FileInputStream(file_name));
      buffer = new byte[Solution.IO_BUFFERS];
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
      bytesRead = din.read(buffer, bufferPointer = 0, Solution.IO_BUFFERS);
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

    Pair[] fillPairArray(int n) throws IOException {
      Pair[] array = new Pair[n];
      for (int i = 0; i < n; i++) array[i] = new Pair(nextInt(), nextInt());
      return array;
    }

    <T extends List<Long>> T fillList(T list, int n) throws IOException {
      for (int i = 0; i < n; i++) list.add(nextLong());
      return list;
    }
  }

  /** HMultiset utility class. It's just like TreeSet, but allows duplicate elements * */
  static class HMultiset<T> extends HashMap<T, Integer> {
    void add(T key) {
      Integer count = get(key);
      put(key, count == null ? 1 : count + 1);
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Integer remove(Object key) {
      if (!containsKey(key)) return null;

      int count = get(key);
      if (count > 1) return put((T) key, count - 1);

      return super.remove(key);
    }
  }

  /** Tree Multiset utility class * */
  static class TMultiset<T extends Number> extends TreeMap<T, Integer> {
    private int size = 0;
    private T head = null, tail = null;

    void add(T value) {
      Integer count = get(value);
      size++;
      if (count == null) {
        put(value, 1);
        recalculateEdges();
      } else {
        put(value, count + 1);
      }
    }

    private void recalculateEdges() {
      try {
        head = firstKey();
      } catch (NoSuchElementException e) {
        head = null;
      }
      try {
        tail = lastKey();
      } catch (NoSuchElementException e) {
        tail = null;
      }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Integer remove(Object key) {
      if (!containsKey(key)) {
        return null;
      }

      size--;

      Integer value = get(key);
      if (value > 1) {
        return put((T) key, value - 1);
      }

      Integer result = super.remove(key);
      recalculateEdges();
      return result;
    }

    @java.lang.Override
    public int size() {
      return size;
    }
  }

  /** Alias for ArrayList */
  static class AL<T> extends ArrayList<T> {}

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
  static class Pair implements Comparable<Pair> {
    int v1;
    int v2;

    public Pair(int v1, int v2) {
      this.v1 = v1;
      this.v2 = v2;
    }

    public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;
      if (!super.equals(object)) return false;
      Pair pair = (Pair) object;
      return v1 == pair.v1 && v2 == pair.v2;
    }

    public int hashCode() {
      return Objects.hash(super.hashCode(), v1, v2);
    }

    public int compareTo(Pair p) {
      return v1 == p.v1 ? Integer.compare(v2, p.v2) : Integer.compare(v1, p.v1);
    }

    @java.lang.Override
    public java.lang.String toString() {
      return "(" + v1 + ", " + v2 + ")";
    }
  }
}
