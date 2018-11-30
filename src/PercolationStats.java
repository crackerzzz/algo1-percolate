import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private final double[] pThreshold;
  private final int trials;

  // perform trials independent experiments on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    this.trials = trials;

    this.pThreshold = new double[trials];
    for (int i = 0; i < trials; i++) {
      final int openSite = performTrial(n);
      this.pThreshold[i] = (openSite * 1.0) / n / n;
      System.out.println(pThreshold[i]);
    }
  }

  private int performTrial(int n) {
    final Percolation perc = new Percolation(n);
    while (!perc.percolates()) {
      final int row = StdRandom.uniform(1, n + 1);
      final int col = StdRandom.uniform(1, n + 1);
      perc.open(row, col);
    }
    return perc.numberOfOpenSites();
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(pThreshold);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(pThreshold);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - confidencePart();
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + confidencePart();
  }

  private double confidencePart() {
    // i.96s/sqrt(T);
    return 1.96 * stddev() / Math.sqrt(trials);
  }

  // test client (described below)
  public static void main(String[] args) {
    final int n = Integer.parseInt(args[0]);
    final int T = Integer.parseInt(args[1]);

    System.out.println("n = " + n + ", T: " + T);

    final PercolationStats pStats = new PercolationStats(n, T);
    System.out.println("mean                    = " + pStats.mean());
    System.out.println("stddev                  = " + pStats.stddev());
    System.out.println("95% confidence interval = [" + pStats.confidenceLo() + "," + pStats.confidenceHi() + "]");
  }
}