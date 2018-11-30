import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF unionFind;

  private final int count; // size of row or column of grid
  private int openSites = 0; // number of sites opened
  private final boolean[][] open; // keep track of open sites
  private final int top; // virtual top site
  private final int bottom; // virtual bottom site

  /***
   * <p>
   * create n-by-n grid, with all sites blocked.
   * </p>
   * 
   * @param n size of row, column of grid
   */
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("n should be greater than 0.");
    }
    this.count = n;
    this.open = new boolean[n][n];
    // additional two spaces for virtual top and bottom
    this.unionFind = new WeightedQuickUnionUF(n * n + 2);

    // avoid calculating bottom in a loop
    final int bottomRow = n * (n - 1);
    // virtual top points to n*n
    this.top = n * n;
    this.bottom = this.top + 1;
    for (int i = 0; i < n; i++) {
      unionFind.union(this.top, i);// connect top to first row
      unionFind.union(this.bottom, bottomRow + i); // connect bottom to last row
    }
  }

  /***
   * <p>
   * open site (row, col) if it is not open already.
   * </p>
   * 
   * @param row site row
   * @param col site column
   */
  public void open(int row, int col) {
    if (isOpen(row, col)) {
      return;
    }

    open[row - 1][col - 1] = true;
    openSites++;

    final int p = getSingleIndex(row, col);

    // try and connect self with adjacent cells.
    connectAdjacent(p, row - 1, col);
    connectAdjacent(p, row, col + 1);
    connectAdjacent(p, row + 1, col);
    connectAdjacent(p, row, col - 1);
  }

  /***
   * <p>
   * check if site given by row and column is open.
   * </p>
   * 
   * @param row site row
   * @param col site column
   * @return true if site is open
   */
  public boolean isOpen(int row, int col) {
    validate(row, col);
    return open[row - 1][col - 1];
  }

  /**
   * <p>
   * check if a site (row, col) full.
   * </p>
   * 
   * @param row site row
   * @param col site column
   * @return true if site is full
   */
  public boolean isFull(int row, int col) {
    validate(row, col);
    // site is full open iff it is open and connected to top
    return isOpen(row, col) && unionFind.connected(top, getSingleIndex(row, col));
  }

  /***
   * <p>
   * get number of open sites.
   * </p>
   * 
   * @return number of open sites
   */
  public int numberOfOpenSites() {
    return openSites;
  }

  /**
   * <p>
   * check if a system percolates.
   * </p>
   * 
   * @return true if system percolates
   */
  public boolean percolates() {
    return unionFind.connected(top, bottom);
  }

  private void connectAdjacent(int p, int row, int col) {
    if (row - 1 < 0 || row - 1 >= count) {
      return;
    }
    if (col - 1 < 0 || col - 1 >= count) {
      return;
    }

    if (isOpen(row, col)) {
      final int q = getSingleIndex(row, col);
      if (!unionFind.connected(p, q)) {
        // connect p and q
        unionFind.union(p, q);
      }
    }
  }

  private void validate(int row, int col) {
    if (row - 1 < 0 || row - 1 >= count) {
      throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
    }
    if (col - 1 < 0 || col - 1 >= count) {
      throw new IndexOutOfBoundsException("column index " + col + " out of bounds");
    }
  }

  private int getSingleIndex(int row, int col) {
    return (row - 1) * count + (col - 1);
  }

  // test client (optional)
  public static void main(String[] args) {
    new Percolation(5);
  }
}
