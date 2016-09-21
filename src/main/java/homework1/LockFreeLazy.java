package homework1;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/**
 * @author Baidin Dima
 */
public class LockFreeLazy<T> implements Lazy<T> {
    private static final AtomicReferenceFieldUpdater<LockFreeLazy, Wrapper> referenceFieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(LockFreeLazy.class, Wrapper.class, "wrapper");

    private final Supplier<T> supplier;
    private volatile Wrapper<T> wrapper;

    public LockFreeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        while (wrapper == null) {
            referenceFieldUpdater.compareAndSet(this, null, new Wrapper<>(supplier));
        }
        return wrapper.value;
    }

    private static class Wrapper<T> {
        private final T value;

        private Wrapper(Supplier<T> supplier) {
            this.value = supplier.get();
        }
    }

}
