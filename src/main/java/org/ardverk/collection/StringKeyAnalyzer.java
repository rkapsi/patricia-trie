/*
 * Copyright 2010 Roger Kapsi
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

import java.io.Serializable;

/**
 * A {@link KeyAnalyzer} for {@link String}s.
 */
public class StringKeyAnalyzer extends AbstractKeyAnalyzer<String> 
        implements Serializable {
    
    private static final long serialVersionUID = -4927553200563548034L;

    public static final StringKeyAnalyzer INSTANCE = new StringKeyAnalyzer();

    /**
     * A 16-bit mask where the MSB bit is 1 and the others are zero
     */
    private static final int MSB = 1 << Character.SIZE-1;
    
    /**
     * Returns a bit mask where the given bit is set
     */
    private static int mask(int bit) {
        return MSB >>> bit;
    }
    
    @Override
    public int lengthInBits(String key) {
        return key.length() * Character.SIZE;
    }

    @Override
    public boolean isBitSet(String key, int bitIndex) {
        if (bitIndex >= lengthInBits(key)) {
            return false;
        }
        
        int index = (int)(bitIndex / Character.SIZE);
        int bit = (int)(bitIndex % Character.SIZE);
        
        return (key.charAt(index) & mask(bit)) != 0;
    }

    @Override
    public int bitIndex(String key, String otherKey) {
        
        boolean allNull = true;
        
        int length1 = key.length();
        int length2 = otherKey.length();
        
        int length = Math.max(length1, length2);
        
        char ch1, ch2 = 0;
        for (int i = 0; i < length; i++) {
            if (i < length1) {
                ch1 = key.charAt(i);
            } else {
                ch1 = 0;                
            }
            
            if (i < length2) {
                ch2 = otherKey.charAt(i);
            } else {
                ch2 = 0;
            }
            
            if (ch1 != ch2) {
                int x = ch1 ^ ch2;
                return i * Character.SIZE + (Integer.numberOfLeadingZeros(x) - Character.SIZE);
            }
            
            if (ch1 != 0) {
                allNull = false;
            }
        }
        
        // All bits are 0
        if (allNull) {
            return KeyAnalyzer.NULL_BIT_KEY;
        }
        
        // Both keys are equal
        return KeyAnalyzer.EQUAL_BIT_KEY;
    }

    @Override
    public boolean isPrefix(String key, String prefix) {
        return key.startsWith(prefix);
    }
}
