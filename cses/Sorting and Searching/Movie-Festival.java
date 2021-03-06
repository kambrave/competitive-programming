import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Solution {
  private static FastReader scanner = new FastReader();
  private static FastWriter log = new FastWriter();
  private static int INF = Integer.MAX_VALUE - 1;
  private static int N;
  private static ArrayList<Pair> movies;
  private static TreeMap<Integer, MS> t;

  private static void readInput() throws IOException {
    N = scanner.nextInt();
    movies = new ArrayList<>(N);
    for (int i = 0; i < N; i++) {
      movies.add(new Pair(scanner.nextInt(), scanner.nextInt()));
    }
  }

  private static int dp(Integer startsAfter) throws IOException {
    if (startsAfter == null) return 0;

    Map.Entry<Integer, MS> entry = t.ceilingEntry(startsAfter);
    if (entry == null) return 0;

    MS value = entry.getValue();
    if (value.memo != -1) return value.memo;

    int max = 0;
    for (Integer end : value.endings) max = Math.max(max, dp(end) + 1);

    value.memo = Math.max(dp(entry.getKey() + 1), max);

    //        System.out.println("-- memo -- " + entry.getKey() + " = " + max);
    return value.memo;
  }

  private static int excludeUncontested() throws IOException {
    int counter = 0;
    int maxEnd = 0;
    for (int i = 0; i < movies.size() - 1; i++) {
      Pair movie = movies.get(i);
      Pair next = movies.get(i + 1);

      if (movie.val1 >= maxEnd && movie.val2 <= next.val1) {
        movies.set(i, null);
        counter++;
      }

      maxEnd = Math.max(movie.val2, maxEnd);
    }

    return counter;
  }

  private static void solve() throws IOException {
    Collections.sort(movies, (a, b) -> Integer.compare(a.val1, b.val1));
    int uncontested = excludeUncontested();

    // constructs treemap
    t = new TreeMap<>();
    for (Pair movie : movies) {
      if (movie == null) continue;
      MS v = t.get(movie.val1);
      if (v == null) {
        v = new MS();
      }
      v.endings.add(movie.val2);
      t.put(movie.val1, v);
    }

    //    System.out.println(t);

    log.print(uncontested + dp(0));
  }

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    try {
      int T = 1;
      for (int t = 0; t < T; t++) {
        readInput();

        solve();
      }
      log.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("WRONG INPUT");
    }
    System.err.println("\ntime: " + (System.currentTimeMillis() - startTime) + "ms");
  }

  static class MS {
    ArrayList<Integer> endings = new ArrayList<>(2);
    int memo = -1;

    @java.lang.Override
    public java.lang.String toString() {
      return "MS{" + "endings=" + endings + ", memo=" + memo + '}';
    }
  }

  /** Competition output utility class */
  static class FastWriter {
    private final StringBuilder bw;

    public FastWriter() {
      this.bw = new StringBuilder(1 << 16);
    }

    public void print(Object object) throws IOException {
      bw.append(object.toString());
    }

    public void print(int i) throws IOException {
      bw.append(Integer.toString(i));
    }

    public void print(long i) throws IOException {
      bw.append(Long.toString(i));
    }

    public void print(String s) throws IOException {
      bw.append(s);
    }

    public void print(String format, Object... args) throws IOException {
      bw.append(String.format(format, args));
    }

    public void println(Object object) throws IOException {
      print(object);
      bw.append("\n");
    }

    public void close() throws IOException {
      System.out.println(bw);
    }
  }

  /** Competition input parser utility class */
  static class FastReader {
    private final int BUFFER_SIZE = 1 << 16;
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

    int[] fillArray(int n) throws IOException {
      int[] array = new int[n];
      for (int i = 0; i < n; i++) array[i] = nextInt();
      return array;
    }

    <T extends List<Integer>> T fillList(T list, int n) throws IOException {
      for (int i = 0; i < n; i++) list.add(nextInt());
      return list;
    }
  }

  /** General purpose Pair utility class */
  static class Pair {
    int val1;
    int val2;

    public Pair(int val1, int val2) {
      this.val1 = val1;
      this.val2 = val2;
    }

    @java.lang.Override
    public java.lang.String toString() {
      return "Pair{" + "val1=" + val1 + ", val2=" + val2 + '}';
    }
  }
}
