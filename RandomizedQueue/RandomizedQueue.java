/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/12/2015
 *
 * Compilation:  javac-algs4 RandomizedQueue.java
 * Execution: java-algs4 RandomizedQueue
 * Dependencies: StdRandom.java
 *
 * Description: A data structure similar to queue, except dequeue()
 * randomly removes and returns one entry in the queue. Its iterator will
 * also randomly loop through the randomized queue.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq;
    private int N = 0;
    private int size = 0;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        rq = (Item[]) new Object[2];
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return size;
    }
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) 
            throw new NullPointerException("Please add valid items!");
        if (N == rq.length - 1) resize(2*rq.length);
        rq[N++] = item;
        size++;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) 
            throw new NoSuchElementException("No elements in the queue!");
        int indexRemove = N;
        while (rq[indexRemove] == null)
            indexRemove = StdRandom.uniform(N);
        Item item = rq[indexRemove];
        rq[indexRemove] = null;
        size--;
        if (size == rq.length/4) resize(rq.length/2);
        return item;
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) 
            throw new NoSuchElementException("No elements in the queue!");
        Item item = null;
        while (item == null)
            item = rq[StdRandom.uniform(N)];
        return item;
    }
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int counter = 0;
        for (int i = 0; i < N; i++) {
            if (rq[i] != null)
                copy[counter++] = rq[i];
        }
        N = counter;
        rq = copy;
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RqIterator();
    }
    
    private class RqIterator implements Iterator<Item> {
        private Item[] copy = (Item[]) new Object[size];
        private int current = 0;
        
        public RqIterator() {
            for (int i = 0; i < N; i++) {
               if (rq[i] != null)
                copy[current++] = rq[i];
            }
            StdRandom.shuffle(copy);
            current = 0;
        }
        
        public boolean hasNext() {
            return current < size;
        }
        
        public void remove() { 
            throw new UnsupportedOperationException("Unsupported Operation!");
        }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException("No more elements in the deque!");
            return copy[current++];
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        try {
            RandomizedQueue<String> D = new RandomizedQueue<String>();
            //D.enqueue(null);
            D.enqueue("hohoho");
            D.enqueue("ahkhkj");
            D.enqueue("hkjhkjhkj");
            System.out.println("Remove: " + D.dequeue());
            Iterator it = D.iterator();
            while (it.hasNext()) System.out.println(it.next());
            System.out.println("Size: " + D.size());
        } catch (NullPointerException e) {
            System.out.println(e);
        } catch (NoSuchElementException e) {
            System.out.println(e);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }
}