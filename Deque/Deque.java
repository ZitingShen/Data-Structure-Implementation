/*************************************************************************
 * Name: Ziting Shen
 * Date: 08/12/2015
 *
 * Compilation:  javac-algs4 Deaue.java
 * Execution:  java-algs4 Deque 
 *
 * Description: A double-ended queue. Users can add or remove entries at 
 * both the front and the end.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;
    
    private class Node {
        private Item item;
        private Node prev, next;
    }
    
    // construct an empty deque
    public Deque() {
        first = new Node();
        last = new Node();
        first.next = last;
        last.prev = first;
        size = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return size;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) 
            throw new NullPointerException("Please add valid items!");
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = first.next;
        newNode.prev = first;
        first.next.prev = newNode;
        first.next = newNode;
        size++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) 
            throw new NullPointerException("Please add valid items!");
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = last;
        newNode.prev = last.prev;
        last.prev.next = newNode;
        last.prev = newNode;
        size++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) 
            throw new NoSuchElementException("No elements in the deque!");
        Item item = first.next.item;
        first.next.next.prev = first;
        first.next = first.next.next;
        size--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) 
            throw new NoSuchElementException("No elements in the deque!");
        Item item = last.prev.item;
        last.prev.prev.next = last;
        last.prev = last.prev.prev;
        size--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = first.next;
        
        public boolean hasNext() {
            return current.item != null;
        }
        
        public void remove() { 
            throw new UnsupportedOperationException("Unsupported Operation!");
        }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException("No more elements in the deque!");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        try {
            Deque<String> D = new Deque<String>();
            D.addFirst("hahaha");
            D.addLast("hohoho");
            System.out.println(D.removeLast());
            Iterator it = D.iterator();
            while (it.hasNext())
                System.out.println(it.next());
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