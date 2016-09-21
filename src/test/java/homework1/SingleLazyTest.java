package homework1;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Baidin Dima
 */
public class SingleLazyTest extends TestCase {
    public void testGet() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        Lazy<Integer> lazy = LazyFactory.getSingleLazy(atomicInteger::incrementAndGet);

        assertEquals(lazy.get(), lazy.get());
        assertEquals(1, atomicInteger.get());
    }

}