package ua.yandex.shad.collections;

import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maksym Yatsura
 */
public class QueueTest {

    @Test
    public void testPeek_OneElementInQueue() {
        Queue<String> queue = new Queue<String>(); 
        queue.enqueue("hi!");
        String expResult = "hi!";
        String actualResult = queue.peek();
        assertEquals(expResult, actualResult);
    }
    
    @Test
    public void testPeek_TwoElementsInQueue() {
        Queue<String> queue = new Queue<String>(); 
        queue.enqueue("hi!");
        queue.enqueue("rabbit");
        String expResult = "hi!";
        String actualResult = queue.peek();
        assertEquals(expResult, actualResult);
    }
 
    @Test(expected = NoSuchElementException.class)
    public void testPeek_Empty() {
        Queue<String> queue = new Queue<String>(); 
        String actualResult = queue.peek();
    }

    @Test
    public void testEnqueue_EmptyQueue() {
        Queue<String> queue = new Queue<String>();
        queue.enqueue("word");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue_NotEmptyQueue() {
        Queue<String> queue = new Queue<String>();
        queue.enqueue("word");
        queue.enqueue("another");
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDequeue_EmptyQueue() {
        Queue<String> queue = new Queue<String>(); 
        String actualResult = queue.dequeue();        
    }
    
    @Test
    public void testDequeue_NotEmptyQueue() {
        Queue<String> queue = new Queue<String>(); 
        queue.enqueue("hello");
        String expResult = "hello";
        String actualResult = queue.dequeue();    
        assertEquals(expResult, actualResult);
    }

    @Test
    public void testIsEmpty_Empty() {
        Queue<String> queue = new Queue<String>(); 
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testIsEmpty_NotEmpty() {
        Queue<String> queue = new Queue<String>(); 
        queue.enqueue("hello");
        assertFalse(queue.isEmpty());
    }
    
}
