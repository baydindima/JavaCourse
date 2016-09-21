package practice3;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Rule allows to register threads and check that all threads ends without exceptions
 * <p>
 * Created by John on 9/21/2016.
 */
public class ThreadRule implements TestRule {
    private final Map<Thread, Boolean> threads = new ConcurrentHashMap<>();

    /**
     * Add thread to observed. This function is idempotent.
     *
     * @param thread new observed thread
     */
    public void register(Thread thread) {
        threads.computeIfAbsent(thread, t -> {
            t.setUncaughtExceptionHandler((t1, e) -> threads.put(t1, false));
            return true;
        });
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } finally {
                    checkAllThreads();
                }
            }
        };
    }

    private void checkAllThreads() {
        List<Thread> aliveThreads = threads.keySet().stream()
                .filter(Thread::isAlive)
                .collect(Collectors.toList());
        if (!aliveThreads.isEmpty()) {
            throw new RuntimeException("This threads is alive " + aliveThreads.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ")));
        }

        List<Thread> threadsWithExceptions = threads.entrySet().stream()
                .filter(e -> !e.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!threadsWithExceptions.isEmpty()) {
            throw new RuntimeException("This threads is throw exception " + threadsWithExceptions.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ")));
        }
    }
}
