package ua.yandex.shad.collections;

import java.util.NoSuchElementException;

/**
 *
 * @author Maksym Yatsura
 */
public class Queue<Item> {

    private Node<Item> first;   
    private Node<Item> last; 
    
    private static class Node<Item> {

        private Item item;
        private Node<Item> next;
    }
    
    public Queue() {
        first = null;
        last = null;
    }
    
    public Item peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first.item;
    }
    
    public void enqueue(Item item) {
        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }
    }
    
    public Item dequeue() {

        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }  
        return item;
    }
    
    public boolean isEmpty() {
        return first == null;
    }

}
