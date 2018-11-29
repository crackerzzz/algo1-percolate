import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private final WeightedQuickUnionUF unionFind;
	private final int count;
	private int openSites = 0;
	private final boolean[][] open;
	private final boolean[][] full;
	private final int top; // virtual top site
	private final int bottom; // virtual bottom site

	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n should be greater than 0.");
		}
		this.count = n;
		this.open = new boolean[n][n];
		this.full = new boolean[n][n];
		this.unionFind = new WeightedQuickUnionUF(n * n + 2); // additional two spaces for virtual top and bottom

		// avoid calculating bottom in a loop
		final int bottomRow = n * (n - 1);
		// virtual top points to n*n
		this.top = n * n;
		this.bottom = this.top + 1;
		for (int i = 0; i < n; i++) {
			unionFind.union(this.top, i);
			unionFind.union(this.bottom, bottomRow + i);
		}
		System.out.println();
	}

	// open site (row, col) if it is not open already
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

		// openAdjacent(p, row - 1, col);
		// openAdjacent(p, row, col + 1);
		// openAdjacent(p, row + 1, col);
		// openAdjacent(p, row, col - 1);
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		validate(row, col);
		return open[row - 1][col - 1];
	}

	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		validate(row, col);
		return isOpen(row, col) && unionFind.connected(top, getSingleIndex(row, col));
		// return full[row - 1][col - 1];
	}

	// number of open sites
	public int numberOfOpenSites() {
		return openSites;
	}

	// does the system percolate?
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

	private void openAdjacent(int p, int row, int col) {
		// avoid processing for out of range row, col
		if (row - 1 < 0 || row - 1 >= count) {
			return;
		}
		if (col - 1 < 0 || col - 1 >= count) {
			return;
		}

		if (!isOpen(row - 1, col - 1)) {
			return;
		}
		if (isFull(row - 1, col - 1)) {
			return;
		}

		if (row - 1 == 0) {
			full[row - 1][col - 1] = true;
		}

		openAdjacent(p, row - 1, col); // open above
		openAdjacent(p, row, col + 1); // open right
		openAdjacent(p, row + 1, col); // open below
		openAdjacent(p, row, col - 1); // open left
	}

	private void validate(int row, int col) {
		if (row - 1 < 0 || row - 1 >= count)
			throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
		if (col - 1 < 0 || col - 1 >= count)
			throw new IndexOutOfBoundsException("column index " + col + " out of bounds");
	}

	private int getSingleIndex(int row, int col) {
		return (row - 1) * count + (col - 1);
	}

	// test client (optional)
	public static void main(String[] args) {
		new Percolation(5);
	}
}
