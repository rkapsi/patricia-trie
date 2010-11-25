package org.ardverk.collection;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.junit.Test;

public class ByteArrayKeyAnalyzerTest {

    private static final int SIZE = 20000;
    
    @Test
    public void bitSet() {
        byte[] key = toByteArray("10100110", 2);
        ByteArrayKeyAnalyzer ka = new ByteArrayKeyAnalyzer(key.length * 8);
        int length = ka.lengthInBits(key);
        
        TestCase.assertTrue(ka.isBitSet(key, 0, length));
        TestCase.assertFalse(ka.isBitSet(key, 1, length));
        TestCase.assertTrue(ka.isBitSet(key, 2, length));
        TestCase.assertFalse(ka.isBitSet(key, 3, length));
        TestCase.assertFalse(ka.isBitSet(key, 4, length));
        TestCase.assertTrue(ka.isBitSet(key, 5, length));
        TestCase.assertTrue(ka.isBitSet(key, 6, length));
        TestCase.assertFalse(ka.isBitSet(key, 7, length));
    }
    
    @Test
    public void keys() {
        PatriciaTrie<byte[], BigInteger> trie
            = new PatriciaTrie<byte[], BigInteger>(
                    ByteArrayKeyAnalyzer.INSTANCE);
        
        Map<byte[], BigInteger> map 
            = new TreeMap<byte[], BigInteger>(
                    ByteArrayKeyAnalyzer.INSTANCE);
        
        for (int i = 0; i < SIZE; i++) {
            BigInteger value = BigInteger.valueOf(i);
            byte[] key = toByteArray(value);
            
            BigInteger existing = trie.put(key, value);
            TestCase.assertNull(existing);
            
            map.put(key, value);
        }
        
        TestCase.assertEquals(map.size(), trie.size());
        
        for (byte[] key : map.keySet()) {
            BigInteger expected = new BigInteger(1, key);
            BigInteger value = trie.get(key);
            
            TestCase.assertEquals(expected, value);
        }
    }
    
    @Test
    public void prefix() {
        byte[] prefix   = toByteArray("00001010", 2);
        byte[] key1     = toByteArray("11001010", 2);
        byte[] key2     = toByteArray("10101100", 2);
        
        ByteArrayKeyAnalyzer keyAnalyzer 
            = new ByteArrayKeyAnalyzer(key1.length * 8);
        
        int prefixLength = keyAnalyzer.lengthInBits(prefix);
            
        TestCase.assertFalse(keyAnalyzer.isPrefix(
                prefix, 4, prefixLength, key1));
        
        TestCase.assertTrue(keyAnalyzer.isPrefix(
                prefix, 4, prefixLength, key2));
    }
    
    private static byte[] toByteArray(String value, int radix) {
        return toByteArray(Long.parseLong(value, radix));
    }
    
    private static byte[] toByteArray(long value) {
        return toByteArray(BigInteger.valueOf(value));
    }
    
    private static byte[] toByteArray(BigInteger value) {
        byte[] src = value.toByteArray();
        if (src.length <= 1) {
            return src;
        }
        
        if (src[0] != 0) {
            return src;
        }
        
        byte[] dst = new byte[src.length-1];
        System.arraycopy(src, 1, dst, 0, dst.length);
        return dst;
    }
}
