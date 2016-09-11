package homework1;

import junit.framework.TestCase;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Baidin Dima
 */
public class LockFreeLazyTest extends TestCase {

    public void testGet() throws Exception {
        final int threadCount = 10;
        AtomicInteger atomicInteger = new AtomicInteger();

        Lazy<Integer> lazy = LazyFactory.getLockFreeLazy(atomicInteger::incrementAndGet);
        CyclicBarrier countDownLatch = new CyclicBarrier(threadCount);
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                    return;
                }
                assertEquals(lazy.get(), lazy.get());
            });
        }
    }

}