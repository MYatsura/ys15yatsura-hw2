package ua.yandex.shad.autocomplete;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import ua.yandex.shad.tries.Trie;
import ua.yandex.shad.tries.Tuple;

/**
 *
 * @author Maksym Yatsura
 */
@RunWith(MockitoJUnitRunner.class)
public class PrefixMatchesTest {

    @Mock private Trie trie;
    
    @InjectMocks private final PrefixMatches prefMatches = new PrefixMatches();
    
    @Test
    public void testLoad_ThreeItems() {
        prefMatches.load("one", "two", "three");
        verify(trie, times(1)).add(eq(new Tuple("one", 3)));
        verify(trie, times(1)).add(eq(new Tuple("two", 3)));
        verify(trie, times(1)).add(eq(new Tuple("three", 5)));
    }
    
    @Test
    public void testLoad_ZeroItems() {
        prefMatches.load();
        verify(trie, never()).add(Matchers.any(Tuple.class));
    }
    
    @Test
    public void testLoad_AllShorterThanThreeSymbols() {
        prefMatches.load("", "a", "b", "ab");
        verify(trie, never()).add(Matchers.any(Tuple.class));
    }
    
    @Test
    public void testLoad_TwoShort() {
        
        prefMatches.load("opera", "chrome", "ie", "vk", "facebook");
        verify(trie, times(1)).add(eq(new Tuple("opera", 5)));
        verify(trie, times(1)).add(eq(new Tuple("chrome", 6)));
        verify(trie, times(1)).add(eq(new Tuple("facebook", 8)));
    }
    
    @Test
    public void testLoad_SplitSentence() {
        
        prefMatches.load("he was born long time ago");
        verify(trie, times(1)).add(eq(new Tuple("was", 3)));
        verify(trie, times(1)).add(eq(new Tuple("born", 4)));
        verify(trie, times(1)).add(eq(new Tuple("long", 4)));
        verify(trie, times(1)).add(eq(new Tuple("time", 4)));
        verify(trie, times(1)).add(eq(new Tuple("ago", 3)));
    }
    
    @Test
    public void testLoad_Mixed() {
        
        prefMatches.load("i like to", "eat", "ice cream");
        verify(trie, times(1)).add(eq(new Tuple("like", 4)));
        verify(trie, times(1)).add(eq(new Tuple("eat", 3)));
        verify(trie, times(1)).add(eq(new Tuple("ice", 3)));
        verify(trie, times(1)).add(eq(new Tuple("cream", 5)));
    }    
    
    @Test
    public void testContains_InTrie() {
        when(trie.contains("hello")).thenReturn(true);
        assertTrue(prefMatches.contains("hello"));
    }
    
    @Test
    public void testContains_NotInTrie() {
        when(trie.contains("bye")).thenReturn(false);
        assertFalse(prefMatches.contains("bye"));
    }

    @Test
    public void testDelete_InTrie() {
        when(trie.delete("hello")).thenReturn(true);
        assertTrue(prefMatches.delete("hello"));
    }
    
    @Test
    public void testDelete_NotInTrie() {
        when(trie.delete("bye")).thenReturn(false);
        assertFalse(prefMatches.delete("bye"));
    }

    @Test
    public void testWordsWithPrefix_String() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "tor", "tork", "tors", "torz", "torpo", "torps",
                             "torok", "tortoi", "tompson", "totrfgf"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("to");
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "tor", "tork", "tors", "torz", "torok", "torpo", 
                              "torps"};
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 7);         
    }
    
    @Test
    public void testWordsWithPrefix_AllWords() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "you", "your", "yourself"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("you");
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "you", "your", "yourself"};
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 3);     
    }

    @Test
    public void testWordsWithPrefix_String_int_threeWords() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "you", "your", "youth", "yourth", "yourself"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("yo", 3);
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "you", "your", "youth"};
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 3);        
    }
    
    @Test
    public void testWordsWithPrefix_String_int_EightWords() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "tor", "tork", "tors", "torz", "torpo", "torps",
                             "torok", "tortoi", "tompson", "totrfgf"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("to", 4);
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "tor", "tork", "tors", "torz", "torok", "torpo", 
                              "torps", "tortoi"};
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 8); 
    }
    
    @Test
    public void testWordsWithPrefix_String_int_NoWords() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "tor", "tork", "tors", "torz", "torpo", "torps",
                             "torok", "tortoi", "tompson", "totrfgf"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("torn", 2);
        Iterator<String> iterator = words.iterator();
        String[] expArray = { };
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 0); 
    }

    @Test
    public void testWordsWithPrefix_String_int_NotEnoughLengths() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "tor", "tork", "tors", "torz", "torpo", "torps",
                             "torok", "tortoi", "torpson", "torrfgf"};
        int len = prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("tor", 9);
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "tor", "tork", "tors", "torz", "torok", "torpo",
                             "torps", "tortoi", "torpson", "torrfgf"};
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 10); 
    }
    
   @Test
    public void testWordsWithPrefix_String_int_OneLength() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "bor", "bork", "bors", "borz"};
        int len = prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("bo", 1);
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "bor" };
        int index = 0;
        boolean equalArrays = true;
        String word;
        while(iterator.hasNext()) {
            word = iterator.next();
            if (!expArray[index++].equals(word)) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 1); 
    }
    
   @Test(expected = IllegalArgumentException.class)
    public void testWordsWithPrefix_String_int_ShortPrefix() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "bor", "bork", "bors", "borz"};
        int len = prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("b", 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWordsWithPrefix_String_int_Negative_k() {
        PrefixMatches prefMatches = new PrefixMatches();
        String[] strings = { "tor", "tork", "tors", "torz", "torpo", "torps"};
        prefMatches.load(strings);
        Iterable<String> words = prefMatches.wordsWithPrefix("torn", -1);
    }
    
    @Test
    public void testSize_NonZeroSize() {
        when(trie.size()).thenReturn(42);
        int expResult = 42;
        int actualResult = prefMatches.size();
        
        assertEquals(expResult, actualResult);
    }
    
    @Test
    public void testSize_ZeroSize() {
        when(trie.size()).thenReturn(0);
        int expResult = 0;
        int actualResult = prefMatches.size();
        
        assertEquals(expResult, actualResult);
    }
    
}
