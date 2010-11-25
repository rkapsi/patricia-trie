/*
 * Copyright 2005-2009 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.collection;

import java.math.BigInteger;

/**
 * A {@link KeyAnalyzer} for {@link BigInteger}s
 * 
 * NOTE: THIS DOES NOT WORK YET!
 */
class BigIntegerKeyAnalyzer extends AbstractKeyAnalyzer<BigInteger> {
    
    private static final long serialVersionUID = 7123669849156062477L;

    /**
     * A singleton instance of {@link ByteArrayKeyAnalyzer}
     */
    public static final BigIntegerKeyAnalyzer INSTANCE 
        = new BigIntegerKeyAnalyzer(Integer.MAX_VALUE);
    
    /**
     * The maximum length of a key in bits
     */
    private final int maxLengthInBits;
    
    public BigIntegerKeyAnalyzer(int maxLengthInBits) {
        if (maxLengthInBits < 0) {
            throw new IllegalArgumentException(
                    "maxLengthInBits=" + maxLengthInBits);
        }
        
        this.maxLengthInBits = maxLengthInBits;
    }
    
    /**
     * Returns the maximum length of a key in bits
     */
    public int getMaxLengthInBits() {
        return maxLengthInBits;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int bitsPerElement() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lengthInBits(BigInteger key) {
        return (key != null ? key.bitLength() : 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBitSet(BigInteger key, int bitIndex, int lengthInBits) {
        if (key == null) {     
            return false;
        }
        
        int prefix = maxLengthInBits - lengthInBits;
        int keyBitIndex = bitIndex - prefix;
        
        if (keyBitIndex >= lengthInBits || keyBitIndex < 0) {
            return false;
        }
        
        int idx = lengthInBits - keyBitIndex - 1;
        return key.testBit(idx);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int bitIndex(BigInteger key, int offsetInBits, int lengthInBits, 
            BigInteger other, int otherOffsetInBits, int otherLengthInBits) {
        
        if (other == null) {
            other = BigInteger.ZERO;
        }
        
        boolean allNull = true;
        int length = Math.max(lengthInBits, otherLengthInBits);
        int prefix = maxLengthInBits - length;
        
        if (prefix < 0) {
            return KeyAnalyzer.OUT_OF_BOUNDS_BIT_KEY;
        }
        
        for (int i = 0; i < length; i++) {
            int index = prefix + (offsetInBits + i);
            boolean value = isBitSet(key, index, lengthInBits);
                
            if (value) {
                allNull = false;
            }
            
            int otherIndex = prefix + (otherOffsetInBits + i);
            boolean otherValue = isBitSet(other, otherIndex, otherLengthInBits);
            
            if (value != otherValue) {
                return index;
            }
        }
        
        if (allNull) {
            return KeyAnalyzer.NULL_BIT_KEY;
        }
        
        return KeyAnalyzer.EQUAL_BIT_KEY;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPrefix(BigInteger prefix, int offsetInBits, 
            int lengthInBits, BigInteger key) {
        
        int keyLength = lengthInBits(key);
        if (lengthInBits > keyLength) {
            return false;
        }
        
        int elements = lengthInBits - offsetInBits;
        for (int i = 0; i < elements; i++) {
            if (isBitSet(prefix, i+offsetInBits, lengthInBits) 
                    != isBitSet(key, i, keyLength)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        BigInteger value = new BigInteger("101", 2);
        
        System.out.println(value.bitCount());
        System.out.println(value.bitLength());
        
        System.out.println(value.testBit(2-0));
        System.out.println(value.testBit(2-1));
        System.out.println(value.testBit(2-2));
    }
}
