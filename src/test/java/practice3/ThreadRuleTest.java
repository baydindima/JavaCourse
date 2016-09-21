package practice3;

import org.junit.Rule;
import org.junit.Test;


public class ThreadRuleTest {

    @Rule
    public ThreadRule threadRule = new ThreadRule();

    @Test
    // should failed
    public void withExceptionThread() throws Exception {
        Thread failThread = new Thread(() -> {
            throw new RuntimeException();
        }, "failThread");
        threadRule.register(failThread);
        failThread.start();
        failThread.join();
    }

    @Test
    // should failed
    public void withInfiniteThread() throws Exception {
        Thread infiniteThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "infiniteThread");
        threadRule.register(infiniteThread);
        infiniteThread.start();
        Thread.sleep(100);
    }

    @Test
    // should failed
    public void testIdempotent() throws Exception {
        Thread failThread = new Thread(() -> {
            throw new RuntimeException();
        }, "failThread");
        threadRule.register(failThread);
        failThread.start();
        failThread.join();
        threadRule.register(failThread);
    }

    @Test
    public void testNotStartedThreads() throws Exception {
        Thread thread = new Thread("i will never start");
        threadRule.register(thread);
    }

    @Test
    public void testWithGoodThreads() throws Exception {
        Thread nonInfiniteThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "nonInfiniteThread");
        threadRule.register(nonInfiniteThread);
        nonInfiniteThread.start();
        nonInfiniteThread.join();
    }
}
