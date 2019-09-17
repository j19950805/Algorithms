import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Estimate the value of the percolation threshold via Monte Carlo simulation.
 * @author Jennifer Chen
 */
public class PercolationStats {
    /** Z value for 95% confidence. */
    private static final double Z = 1.96;
    /** Result of percolation experiments. */
    private double[] results;
    private double mean;
    private double stddev;

    /**
     * Perform independent trials on an n-by-n grid.
     * @param n the side length of the grid area
     * @param trials the number of trial
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                      "input n & trials should bigger than 0");
        }
        results = new double[trials];
        Percolation p;
        for (int t = 0; t < trials; t++) {
            p = new Percolation(n);
            results[t] = percolationExperiment(p, n);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
    }

    /**
     * Perform a single experiment on estimating the percolation threshold.
     * @param p the Percolation instance for the experiment
     * @param n the side Length of the Percolation grid area
     * @return the experiment result (value of percolation threshold)
     */
    private double percolationExperiment(Percolation p, int n) {
        while (!p.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (n * n);
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() { return mean;
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return stddev;
    }

    /**
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean - Z * stddev / Math.sqrt(results.length);
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean + Z * stddev / Math.sqrt(results.length);
    }

    /**
     * Use for unit testing.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please enter command-line arguments:");
            System.out.println("n, trials");
        }
        try {
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(n, trials);
            System.out.printf("mean                    = %f\n", ps.mean());
            System.out.printf("stddiv                  = %f\n", ps.stddev());
            System.out.print("95% confidence interval = [");
            System.out.printf("%f, %f]", ps.confidenceLo(), ps.confidenceHi());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }
}
