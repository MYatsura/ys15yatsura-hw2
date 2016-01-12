package ua.yandex.shad.tries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ua.yandex.shad.collections.Queue;

public class RWayTrie implements Trie {

    public static final int R = 26;
    public static final int EMPTY_VAL = -1;
    public static final int LETTER_SHIFT = 'a';
    private Node root = new Node();
    
    private static  class Node {
        private int val = EMPTY_VAL;
        private Node[] next = new Node[R];
        
        public int getValue() {
            return val;
        }
    }  
    
    private int indexOf(char c) {
        int index = c-LETTER_SHIFT;
        if (index < 0 || index >= R) {
            throw new IllegalArgumentException();
        }
        return index;
    }
    
    @Override
    public void add(Tuple t) {
        root = add(root, t, 0);
    }

    @Override
    public boolean contains(String word) {
        return !(getVal(word) == EMPTY_VAL);
    }

    @Override
    public boolean delete(String word) {
        int prevSize = size();
        root = delete(root, word, 0);
        return size() == prevSize-1;
    }

    @Override
    public Iterable<String> words() {
        return wordsWithPrefix("");
    }

    @Override
    public Iterable<String> wordsWithPrefix(final String pref) {

        return new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                return new BFSIterator(pref);
            }
        };
    }

    @Override
    public int size() {
        return size(root);
    }

    private int getVal(String s) {
        Node x = get(root, s, 0);
        if (x == null) {
            return EMPTY_VAL;
        }
        return (int) x.val;
    }
    
    private Node get(Node x, String s, int d) {
        if (x == null) {
            return null;
        }
        if (d == s.length()) {
            return x;
        }
        char c = s.charAt(d);
        return get(x.next[indexOf(c)], s, d+1);
    }
    
    private Node add(Node y, Tuple t, int d) {
        Node x = y;
        if (x == null) {
            x = new Node();
        }
        if (d == t.getTerm().length()) {
            x.val = t.getWeight();
            return x;
        }
        char c = t.getTerm().charAt(d);
        x.next[indexOf(c)] = add(x.next[indexOf(c)], t, d+1);
        return x;
    }
    
    private Node delete(Node y, String s, int d) {
        if (y == null) {
            return null;
        }
        
        if (d == s.length()) {
            y.val = EMPTY_VAL;
        } else {
            char c = s.charAt(d);
            y.next[indexOf(c)] = delete(y.next[indexOf(c)], s, d+1);
        }
        
        if (y.val != EMPTY_VAL) {
            return y;
        }
        for (char c = 'a'; c < 'a'+R; c++) {
            if (y.next[indexOf(c)] != null) {
                return y;
            }
        }
        return null;
    }
    
    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        int cnt = 0;
        if (x.val != EMPTY_VAL) {
            cnt++;
        }
        for (char c = 'a'; c < 'a'+R; c++) {
            cnt += size(x.next[indexOf(c)]);
        }
        return cnt;
    }      
    
    private static class NodeWithWord {
        private final Node node;
        private final String word;
 
        public NodeWithWord(Node n, String w) {
            node = n;
            word = w;
        }
        
        public NodeWithWord(NodeWithWord nw) {
            node = nw.node;
            word = nw.word;
        }
    }    
    
    private class BFSIterator implements Iterator<String> {

        private Queue<NodeWithWord> queue = new Queue<>();

        public BFSIterator(String pref) {
            Node node = get(root, pref, 0);
            if (node != null) {
                queue.enqueue(new NodeWithWord(node, pref));
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public String next() throws NoSuchElementException {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }  
            
            while (true) {
                NodeWithWord currentNode = new NodeWithWord(queue.dequeue());

                for (char c = 'a'; c < 'a' + R; c++) {
                    if (currentNode.node.next[indexOf(c)] != null) {
                        queue.enqueue(new NodeWithWord(currentNode.node
                                                       .next[indexOf(c)],
                                currentNode.word + c));
                    }
                }

                if (currentNode.node.getValue() != EMPTY_VAL) {
                    return currentNode.word;
                }
            }
        }
    }
    
}
