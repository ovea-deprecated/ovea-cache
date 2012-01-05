package com.ovea.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum RemovalCause {
    /**
     * Entry reclamed by GC (i.e. weak and soft references)
     */
    COLLECTED,

    /**
     * If an entry expired
     */
    EXPIRED,


}
