package ua.yandex.shad.autocomplete;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ua.yandex.shad.tries.Tuple;
import ua.yandex.shad.tries.Trie;
import ua.yandex.shad.tries.RWayTrie;

/**
 *
 * @author Maksym Yatsura
 */
public class PrefixMatches {

    public static final int MIN_LENGTH = 3;
    public static final int MIN_PREF = 2;
    public static final int DEFAULT_K = 3;
    
    private Trie trie = new RWayTrie();

    public int load(String... strings) {
         for (int i = 0; i < strings.length; i++) {
            String[] words = strings[i].split(" ");
            for (int j = 0; j < words.length; j++) {
                if (words[j].length() >= MIN_LENGTH) {
                    trie.add(new Tuple(words[j], words[j].length()));
                }
            }
        }
        return trie.size();
    }

    public boolean contains(String word) {
        return trie.contains(word);
    }

    public boolean delete(String word) {
        return trie.delete(word);
    }

    public Iterable<String> wordsWithPrefix(String pref) {
        return wordsWithPrefix(pref, DEFAULT_K);
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) {
        int param = k;
        
        if (pref.length() < MIN_PREF || k < 0) {
            throw new IllegalArgumentException();
        }
        
        Iterable<String> words = trie.wordsWithPrefix(pref);
        Iterator<String> iterator = words.iterator();
        String curWord = null;
        String prevWord = null;
        int count = 0;
        
        while (iterator.hasNext() && param > 0) {
                curWord = iterator.next();
                if (prevWord == null || curWord.length() > prevWord.length()) {
                    param--;
                }
                
                prevWord = curWord;
                count++;
        }
        
        if (param == 0) {
            while (iterator.hasNext()) {
                curWord = iterator.next();
                if (curWord.length() != prevWord.length()) {
                    break;
                }
                prevWord = curWord;
                count++;            
            }
        }
        
        WordsIterable cutWords = new WordsIterable();
        cutWords.setIterator(new WordsIter(words.iterator(), count));
        return cutWords;
        
    }

    public int size() {
        return trie.size();
    }
    
    private static class WordsIterable implements Iterable<String> {

        private Iterator<String> iterator;
        
        @Override
        public Iterator<String> iterator() {
            return iterator;
        }
        
        public void setIterator(Iterator<String> iter) {
            iterator = iter;
        }
    }
    
    private static class WordsIter implements Iterator<String> {

        private Iterator<String> baseIterator;
        private int numWords;
        private int number;

        public WordsIter(Iterator<String> iterator, int num) {
            baseIterator = iterator;
            numWords = num;
            number = 0;
        }
        
        @Override
        public boolean hasNext() {
            
            return number < numWords;
        }

        @Override
        public String next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            number++;
            return baseIterator.next();
        }
    }
    
}
