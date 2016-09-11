package homework1;

import java.util.function.Supplier;

/**
 * @author Baidin Dima
 */
public class SingleLazy<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private T value;

    public SingleLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (supplier != null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }
}
