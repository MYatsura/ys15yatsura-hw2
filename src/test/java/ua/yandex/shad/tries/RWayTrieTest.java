/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.yandex.shad.tries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maksym Yatsura
 */
public class RWayTrieTest {

    @Test
    public void testAdd_AddFromFirstSymbol() {
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(tigerTuple);
        
        assertTrue(trie.contains("tiger")); 
    }
    
    @Test
    public void testAdd_AlreadyInTheTrie() {
        Tuple tigerTuple = new Tuple("tiger", 5);
        Tuple hiTuple = new Tuple("hi", 2);
        Tuple hellTuple = new Tuple("hell", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(tigerTuple);
        trie.add(hiTuple);
        trie.add(hellTuple);
        trie.add(hellTuple);
        int expResult = 3;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult); 
    }
    
    @Test
    public void testAdd_ContinueExsistingChain() {
        Tuple tigerTuple = new Tuple("tiger", 5);
        Tuple youTuple = new Tuple("you", 3);
        Tuple youthTuple = new Tuple("youth", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(tigerTuple);
        trie.add(youTuple);
        trie.add(youthTuple);
        
        assertTrue(trie.contains("youth")); 
    }

    @Test
    public void testAdd_AddOnlyValueNotNodes() {
        Tuple hellTuple = new Tuple("hell", 4);
        Tuple heTuple = new Tuple("he", 2);
        RWayTrie trie = new RWayTrie();
        trie.add(hellTuple);
        trie.add(heTuple);
        
        assertTrue(trie.contains("he")); 
    }
    
    @Test
    public void testAdd_Branching() {
        Tuple hellTuple = new Tuple("hell", 4);
        Tuple heTuple = new Tuple("he", 2);
        Tuple headTuple = new Tuple("head", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(hellTuple);
        trie.add(heTuple);
        trie.add(headTuple);
        
        assertTrue(trie.contains("head")); 
    }    
    
    @Test
    public void testAdd_LongWord() {
        Tuple longTuple = new Tuple("antananarivu", 12);
        RWayTrie trie = new RWayTrie();
        trie.add(longTuple);
        
        assertTrue(trie.contains("antananarivu"));
    }
    
    @Test
    public void testAdd_Empty() {
        Tuple emptyTuple = new Tuple("", 0);
        RWayTrie trie = new RWayTrie();
        trie.add(emptyTuple);
        
        assertTrue(trie.contains(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_IllegalWord_Before_a() {
        Tuple illegalTuple = new Tuple("abc!", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(illegalTuple);
    }    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_IllegalWord_After_z() {
        Tuple illegalTuple = new Tuple("abc|", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(illegalTuple);
    }  
    
    @Test
    public void testContains_ExistingWord() {
        Tuple youTuple = new Tuple("you", 3);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        
        assertTrue(trie.contains("you"));
    }

    @Test
    public void testContains_AbsentWord() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        
        assertFalse(trie.contains("java"));
    }
    
    @Test
    public void testContains_PrefixOfWord() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        
        assertFalse(trie.contains("tig"));
    }
    
    @Test
    public void testContains_EmptyTrie() {
        RWayTrie trie = new RWayTrie();
        
        assertFalse(trie.contains("hello"));
    }
    
    @Test
    public void testDelete_ExsistingWord() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        trie.delete("you");
        
        assertFalse(trie.contains("you"));
    }
    
    @Test
    public void testDelete_NotExsistingWord() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        trie.delete("java");
        int expResult = 2;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult);        
    }

    @Test
    public void testDelete_FromEmptyTrie() {
        RWayTrie trie = new RWayTrie();
        trie.delete("pascal");
        int expResult = 0;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult);
    }
    
    @Test
    public void testDelete_TwoItemsSizeChange() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        Tuple youthTuple = new Tuple("youth", 5);
        Tuple hellTuple = new Tuple("hell", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        trie.add(youthTuple);
        trie.add(hellTuple);
        trie.delete("you");
        trie.delete("tiger");
        int expResult = 2;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult);
    }

    @Test
    public void testDelete_AllItems() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple tigerTuple = new Tuple("tiger", 5);
        Tuple youthTuple = new Tuple("youth", 5);
        Tuple hellTuple = new Tuple("hell", 4);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(tigerTuple);
        trie.add(youthTuple);
        trie.add(hellTuple);
        trie.delete("you");
        trie.delete("tiger");
        trie.delete("youth");
        trie.delete("hell");
        int expResult = 0;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult);       
    }
    
    @Test
    public void testDelete_LongDeleteShortRemains() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple youthTuple = new Tuple("youth", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(youthTuple);
        trie.delete("youth");
        
        assertTrue(trie.contains("you"));
    }

    @Test
    public void testWords_FiveWords() {
        Tuple dogTuple = new Tuple("dog", 3);
        Tuple spongeTuple = new Tuple("sponge", 6);
        Tuple tableTuple = new Tuple("table", 5);
        Tuple tabTuple = new Tuple("tab", 6);
        Tuple queenTuple = new Tuple("queen", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(dogTuple);
        trie.add(spongeTuple);
        trie.add(tableTuple);
        trie.add(tabTuple);
        trie.add(queenTuple);
        Iterable<String> words = trie.words();
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "dog", "tab", "queen", "table", "sponge"};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 5);
    }   
    
    public void testWords_EmptyTrie() {
        RWayTrie trie = new RWayTrie();

        Iterable<String> words = trie.words();
        Iterator<String> iterator = words.iterator();
        String[] expArray = {};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }      
        assertTrue(equalArrays && index == 0);
    }

    @Test
    public void testWordsWithPrefix_EmptyTrie() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple youthTuple = new Tuple("youth", 5);
        Tuple yourTuple = new Tuple("your", 4);
        Tuple yourthTuple = new Tuple("yourth", 6);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(youthTuple);
        trie.add(youthTuple);
        trie.add(yourTuple);
        trie.add(yourthTuple);
        Iterable<String> words = trie.wordsWithPrefix("yo");
        Iterator<String> iterator = words.iterator();
        String[] expArray = { "you", "your", "youth", "yourth"};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 4);
    }
    
    @Test
    public void testWordsWithPrefix_NoWords() {
        Tuple dogTuple = new Tuple("dog", 3);
        Tuple spongeTuple = new Tuple("sponge", 6);
        Tuple tableTuple = new Tuple("table", 5);
        Tuple tabTuple = new Tuple("tab", 6);
        Tuple queenTuple = new Tuple("queen", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(dogTuple);
        trie.add(spongeTuple);
        trie.add(tableTuple);
        trie.add(tabTuple);
        trie.add(queenTuple);
        Iterable<String> words = trie.wordsWithPrefix("tabu");
        Iterator<String> iterator = words.iterator();
        String[] expArray = {};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 0);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testWordsWithPrefix_NoElement() {
        Tuple dogTuple = new Tuple("dog", 3);
        Tuple spongeTuple = new Tuple("sponge", 6);
        RWayTrie trie = new RWayTrie();
        trie.add(dogTuple);
        trie.add(spongeTuple);
        Iterable<String> words = trie.wordsWithPrefix("tabu");
        Iterator<String> iterator = words.iterator();
        String word = iterator.next();
    }
    
    @Test
    public void testWordsWithPrefix_OneWord() {
        Tuple dogTuple = new Tuple("dog", 3);
        Tuple spongeTuple = new Tuple("sponge", 6);
        Tuple tableTuple = new Tuple("table", 5);
        Tuple tabTuple = new Tuple("tab", 6);
        Tuple queenTuple = new Tuple("queen", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(dogTuple);
        trie.add(spongeTuple);
        trie.add(tableTuple);
        trie.add(tabTuple);
        trie.add(queenTuple);
        Iterable<String> words = trie.wordsWithPrefix("tabl");
        Iterator<String> iterator = words.iterator();
        String[] expArray = {"table"};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 1);
    }
    
    @Test
    public void testWordsWithPrefix_AllWords() {
        Tuple heyTuple = new Tuple("hey", 3);
        Tuple headTuple = new Tuple("head", 4);
        Tuple helloTuple = new Tuple("hello", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(heyTuple);
        trie.add(headTuple);
        trie.add(helloTuple);
        Iterable<String> words = trie.wordsWithPrefix("he");
        Iterator<String> iterator = words.iterator();
        String[] expArray = {"hey", "head", "hello"};
        int index = 0;
        boolean equalArrays = true;
        while(iterator.hasNext()) {
            if (!expArray[index++].equals(iterator.next())) {
                equalArrays = false;
            }
        }        
        assertTrue(equalArrays && index == 3);
    }

    @Test
    public void testSize_FourItems() {
        Tuple youTuple = new Tuple("you", 3);
        Tuple youthTuple = new Tuple("youth", 5);
        Tuple tigerTuple = new Tuple("tiger", 5);
        RWayTrie trie = new RWayTrie();
        trie.add(youTuple);
        trie.add(youthTuple);
        trie.add(tigerTuple);
        int expResult = 3;
        int actualResult = trie.size();
        
        assertEquals(expResult, actualResult);
    }
    
    @Test
    public void testSize_EmptyTrie() {
        int expResult = 0;
        RWayTrie trie = new RWayTrie();
        int actualResult= trie.size();
        assertEquals(expResult, actualResult);
    }
    
}
