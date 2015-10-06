/*------------------------------------------------------------------ 
 *  Author:       Ziting Shen
 *  Written:      08/10/2015
 * 
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation
 *  Dependencies: StdRandom.java   WeightedQuickUnionUF.java
 *
 *  Simulation of a percolation system with an N-by-N grid of sites. 
 *----------------------------------------------------------------*/
public class Percolation {
    private int N;   // the side length of the percolation system
    private WeightedQuickUnionUF system;  // the percolation system
    private boolean[] opened; // if the site is opened
    private boolean[] bottomConnected; // if the site is bottom connected
    
    /**
     *  Create N-by-N grid, with all sites blocked.
     * 
     *  Throws an IllegalArgumentException if N is not positive.
     */
    public Percolation(int N) {
        if (N <= 0) 
            throw new IllegalArgumentException("Please give a positive integer!");
        system = new WeightedQuickUnionUF(N*N + 1);
        this.N = N;
        opened = new boolean[N*N + 1];
        bottomConnected = new boolean[N*N + 1];
    }
    
    /** 
     *  Open site (row i, column j) if it is not open already.
     */
    public void open(int i, int j) {
        validate(i, j);
        int index = (i - 1)*N + j; // the index of site (i, j) in the system
        boolean isBottomConnected = false; // if the component including site (i, j)
                                           // is connected to the bottom
        if (!isOpen(i, j)) {
            opened[index] = true;
            if (i == N) isBottomConnected = true;
            
            if (i > 1 && isOpen(i - 1, j)) {
                isBottomConnected = isBottomConnected 
                    || bottomConnected[system.find(index - N)];
                system.union(index - N, index);
            }
            if (i < N && isOpen(i + 1, j)) {
                isBottomConnected = isBottomConnected 
                    || bottomConnected[system.find(index + N)];
                system.union(index + N, index);
            }
            if (j > 1 && isOpen(i, j - 1)) {
                isBottomConnected = isBottomConnected 
                    || bottomConnected[system.find(index - 1)];
                system.union(index - 1, index);
            }
            if (j < N && isOpen(i, j + 1)) {
                isBottomConnected = isBottomConnected
                    || bottomConnected[system.find(index + 1)];
                system.union(index + 1, index);
            }
            if (i == 1) {
                isBottomConnected = isBottomConnected
                    || bottomConnected[system.find(0)];
                system.union(0, index);
            }
            
            bottomConnected[system.find(index)] = isBottomConnected;
        }
    }
    
    /**
     *  Is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        int index = (i - 1)*N + j; // the index of side(i, j) in the system
        return opened[index];
    }
    
    /**
     *  Is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        validate(i, j);
        int index = (i - 1)*N + j; // the index of side(i, j) in the system
        return system.connected(0, index);
    }
    
    /**
     *  Does the system percolate?
     */
    public boolean percolates() {
        return bottomConnected[system.find(0)];
    }
    
    /**
     *  Check if site (row i, column j) is out of bound.
     * 
     *  Throws an IndexOutOfBoundsException if it is out of bound.
     */
    private void validate(int i, int j) {
        if (((i < 1) || (i > N)) || ((j < 1) || (j > N)))
            throw new IndexOutOfBoundsException("Please give a valid site!");
    }
    
   /**
    *  Test client: Monte Carlo Simulation of the percolation system.
    *  Keep randomly opening a blocked site, until the system percolates. The 
    *  fraction of sites that are opened when the system percolates helps estimate 
    *  the percolation threhold. Repeat the simulation for T times, then calculate 
    *  the average fraction as the estimated percolation threhold.
    * 
    *  Catches IllegalArgumentException and IndexOutOfBoundsException and print out 
    *  the exception message.
    */
   public static void main(String[] args) {
       try {
           int average = 0;
           int T = 1000;
           int N = 100;
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
               average += times;
           }
           System.out.println(average/(double) T/(N*N));
           
       } catch (IllegalArgumentException e) {
           System.out.println(e);
       } catch (IndexOutOfBoundsException e) {
           System.out.println(e);
       }
   }
}