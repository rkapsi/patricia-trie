package org.ardverk.collection;

class Objects {

    private Objects() {}

    /**
     * Returns true if both values are either null or equal
     */
    public static boolean areEqual(Object a, Object b) {
        return (a == null ? b == null : a.equals(b));
    }
    
    /**
     * Throws a {@link NullPointerException} with the given message if 
     * the argument is null.
     */
    public static <T> T notNull(T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }
}
