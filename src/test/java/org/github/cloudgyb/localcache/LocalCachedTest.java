package org.github.cloudgyb.localcache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for LocalCached.
 */
public class LocalCachedTest {

    @Test
    public void getLocalCache() {
        LocalCached localCached = LocalCached.getLocalCached();
        LocalCached localCached1 = LocalCached.getLocalCached();
        assertTrue(localCached==localCached1);
    }
}
