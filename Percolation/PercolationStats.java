/*----------------------------------------------------------- 
 *  Author:       Ziting Shen
 *  Written:      08/10/2015
 * 
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats N T
 *  Dependencies: StdRandom.java StdStats.java Percolation.java
 *
 *  Monte Carlo simulation of a percolation system with an 
 *  N-by-N grid of sites for T times.
 *---------------------------------------------------------*/
public class PercolationStats {
    private int T; // times for Monte Carlo simulation
    private double[] fractions; // the fractions of opened sites in each simulation
    
    /**
     *  Perform T independent experiments on an N-by-N grid.
     *  Prints out the mean, standard deviation, and the 95% confidence interval of 
     *  the fractions.
     * 
     *  Throws an IllegalArgumentException when N or T is not positive.
     */
    public PercolationStats(int N, int T) {
        if (T <= 0) 
            throw new IllegalArgumentException("Please give two positive integers!");
    
        this.T = T;
        fractions = new double[T];

        for (int i = 0; i < T; i++) {
           Percolation system = new Percolation(N);
           int times = 0;
           while (!system.percolates()) {
               int row = StdRandom.uniform(1, N + 1);
               int column = StdRandom.uniform(1, N + 1);
               if (!system.isOpen(row, column)) {
                   system.open(row, column);
                   times++;
               }
           }
           fractions[i] = times/(double) (N*N);
        }
    }
    
    /** 
     * Sample mean of percolation threshold.
     */
    public double mean() {
        return StdStats.mean(fractions);
    }
    
    /** 
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return StdStats.stddev(fractions);
    }
    
    /**
     *  Low endpoint of 95% confidence interval.
     */
    public double confidenceLo() {
        return mean() - 1.96*stddev()/Math.sqrt(T);
    }
    
    /**
     *  High endpoint of 95% confidence interval.
     */
    public double confidenceHi() {
        return mean() + 1.96*stddev()/Math.sqrt(T);
    }

    /**
     *  Takes two command-line arguments N and T, performs T independent 
     *  computational experiments on an N-by-N grid, and prints out the mean, 
     *  standard deviation, and the 95% confidence interval for the percolation 
     *  threshold.
     * 
     *  Catches IllegalArgumentException and IndexOutOfBoundsException.
     */
    public static void main(String[] args) {
        try {
            PercolationStats stat = new PercolationStats(Integer.parseInt(args[0]), 
                                                         Integer.parseInt(args[1]));
            System.out.println("mean\t\t\t= " + stat.mean());
            System.out.println("stddev\t\t\t= " + stat.stddev());
            System.out.println("95% confidence interval\t= " + stat.confidenceLo() 
                               + ", " + stat.confidenceHi());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }
}