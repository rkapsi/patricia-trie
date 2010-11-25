package org.ardverk.collection;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.junit.Test;

public class BigIntegerKeyAnalyzerTest {

    private static final int SIZE = 20000;
    
    @Test
    public void keys() {
        PatriciaTrie<BigInteger, BigInteger> trie
            = new PatriciaTrie<BigInteger, BigInteger>(
                    BigIntegerKeyAnalyzer.INSTANCE);
        
        Map<BigInteger, BigInteger> map 
            = new TreeMap<BigInteger, BigInteger>(
                    BigIntegerKeyAnalyzer.INSTANCE);
        
        // Fill the Trie and the Map
        for (int i = 0; i < SIZE; i++) {
            BigInteger value = BigInteger.valueOf(i);
            
            BigInteger existing = trie.put(value, value);
            TestCase.assertNull(existing);
            
            map.put(value, value);
        }
        
        TestCase.assertEquals(map.size(), trie.size());
        
        // Check if all values are there *AND* if they are in
        // the same order!
        for (BigInteger expected : map.keySet()) {
            BigInteger value = trie.get(expected);
            
            TestCase.assertEquals(expected, value);
        }
    }
}
