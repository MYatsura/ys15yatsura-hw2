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
    public Iterable<String> wordsWithPrefix(String s) {
        
        WordsIter iterator = bfs(new NodeWithWord(get(root, s, 0), s));
        WordsIterable wi = new WordsIterable();
        wi.setIterator(iterator);
        
        return wi;
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
    
    private WordsIter bfs(NodeWithWord x) {
        
        WordsIter result = new WordsIter();
        
        if (x.node == null) {
            return result;
        }
        
        Queue<NodeWithWord> queue = new Queue<NodeWithWord>();
        queue.enqueue(x);
        WordsIter iterator = new WordsIter();
        result.next = iterator;
        
        while (!queue.isEmpty()) {
            NodeWithWord v = new NodeWithWord(queue.dequeue());
            if (v.node.getValue() != EMPTY_VAL) {
                iterator.setWord(v.word);
                iterator.setNext(new WordsIter());
                iterator = iterator.nextIter();
            }
            for (char c = 'a'; c < 'a'+R; c++) {
                if (v.node.next[indexOf(c)] != null) {
                    queue.enqueue(new NodeWithWord(v.node.next[indexOf(c)], 
                                               v.word + c));
                }
            }
        }
        
        return result;
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
    
    private static class WordsIterable implements Iterable<String> {

        private Iterator<String> iterator;
        
        @Override
        public Iterator<String> iterator() {
            return new WordsIter(iterator);
        }
        
        public void setIterator(Iterator<String> iter) {
            iterator = iter;
        }
    }
    
    private static class WordsIter implements Iterator<String> {

        private String word;
        private WordsIter next;

        public WordsIter() {
            word = null;
            next = null;
        }
        
        public WordsIter(Iterator<String> iter) {
            
            if (iter instanceof WordsIter) {
                WordsIter iterator = (WordsIter) iter;
                this.next = iterator.next;
                this.word = iterator.word;
            }    
        }
        
        @Override
        public boolean hasNext() {
            return next != null && next.word != null;
        }

        @Override
        public String next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            word = next.word;
            next = next.next;
            return word;
        }
        
        public void setWord(String w) {
            word = w;
        }
        
        public WordsIter nextIter() {
            return next;
  
        }
        
        public void setNext(WordsIter iterator) {
            next = iterator;
        }
        
    }
    
}
