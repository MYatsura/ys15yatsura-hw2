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

    public Iterable<String> wordsWithPrefix(final String pref, final int k) {
 
        if (pref.length() < MIN_PREF || k < 0) {
            throw new IllegalArgumentException();
        }
        
        return new Iterable<String>() {
            
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private Iterator<String> trieIter = trie
                            .wordsWithPrefix(pref).iterator();
                    private int lens = k;
                    private int prevLength = -1;
                    @Override
                    public boolean hasNext() {
                        return trieIter.hasNext() && lens > 0; 
                    }
                    @Override
                    public String next() throws NoSuchElementException {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }      
                        String curWord = trieIter.next();
                        int len = curWord.length();
                        if (len > prevLength) {
                            lens--;
                            prevLength = len;
                        }
                        return curWord;
                    }
                }; 
            }
        };
    }

    public int size() {
        return trie.size();
    }
    
}
