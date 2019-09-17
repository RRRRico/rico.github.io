package minesweeper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Minesweeper {
  private final char[][] mtx;
  private final int m;
  private final int n;
  private final int k;
  private int flags;
  private Set<Integer> set;

  private static final char BOOM = 'X';
  private static final char INIT = '-';
  private static final char MARK = 'M';
  private static final char BLANK = 'O';

  private static final int[][] MOVES = {
    {-1, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
  };

  public Minesweeper(int m, int n, int k) {
    this.m = n;
    this.n = n;
    this.k = k;
    this.set = new HashSet<>();
    mtx = new char[m][n];
    for (int i = 0; i < m; i++) Arrays.fill(mtx[i], INIT);

    ThreadLocalRandom random = ThreadLocalRandom.current();
    while (set.size() < k) {
      int idx = random.nextInt(m * n);
      set.add(idx);
    }
    //    for (int i = 0; i < m * n; i++) {
    //      if (!set.contains(i)) System.out.println(i / n + "," + i % n);
    //    }
  }

  public void print(boolean withBoom) {
    if (withBoom) {
      for (int idx : set) mtx[idx / n][idx % n] = BOOM;
    }
    System.out.print("  ");
    for (int j = 0; j < n; j++) {
      System.out.print(j + ",");
    }
    System.out.println();
    System.out.print("  ");
    for (int j = 0; j < n; j++) {
      System.out.print("--");
    }
    System.out.println();
    for (int i = 0; i < m; i++) {
      System.out.print(i + "|");
      for (int j = 0; j < n; j++) {
        System.out.print(mtx[i][j] + ",");
      }
      System.out.println();
    }
  }

  private int index(int i, int j) {
    return i * n + j;
  }

  public boolean click(int i, int j) {
    if (set.contains(index(i, j))) return false;
    dfs(i, j);
    return true;
  }

  private void dfs(int i, int j) {
    int cnt = count(i, j);
    if (cnt > 0) {
      mtx[i][j] = (char) ('0' + cnt);
      return;
    }
    mtx[i][j] = BLANK;
    for (int[] move : MOVES) {
      int x = i + move[0];
      int y = j + move[1];
      if (x >= 0 && x < m && y >= 0 && y < n && mtx[x][y] == INIT) dfs(x, y);
    }
  }

  public void mark(int i, int j) {
    flags++;
    mtx[i][j] = MARK;
  }

  public void unmark(int i, int j) {
    flags--;
    mtx[i][j] = INIT;
  }

  public boolean end() {
    return flags == k;
  }

  private int count(int i, int j) {
    int count = 0;
    for (int[] move : MOVES) {
      int x = i + move[0];
      int y = j + move[1];
      if (x >= 0 && x < m && y >= 0 && y < n && set.contains(index(x, y))) count++;
    }
    return count;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("matrix size m n k?");
    String[] strs = scanner.nextLine().split("\\s");
    int m = Integer.parseInt(strs[0]);
    int n = Integer.parseInt(strs[1]);
    int k = Integer.parseInt(strs[2]);

    Minesweeper minesweeper = new Minesweeper(m, n, k);

    while (!minesweeper.end()) {
      System.out.println("(C)lick or (M)ark on which cell?");
      String temp = scanner.nextLine();
      String[] actions = temp.split("\\s");

      int i = Integer.parseInt(actions[1]);
      int j = Integer.parseInt(actions[2]);

      switch (actions[0]) {
        case "C":
          boolean con = minesweeper.click(i, j);
          if (!con) {
            minesweeper.print(true);
            System.out.println("Boom shakalaka!");
            return;
          }
          break;
        case "M":
          minesweeper.mark(i, j);
          break;
        case "U":
          minesweeper.unmark(i, j);
          break;
        default:
          throw new IllegalArgumentException("Bad operation. E.g. C 9 3");
      }
      minesweeper.print(false);
    }

    System.out.println("Congratulations!");
    minesweeper.print(true);
  }
}
