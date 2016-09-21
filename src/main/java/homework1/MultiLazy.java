package homework1;

import com.sun.istack.internal.NotNull;

import java.util.function.Supplier;

/**
 * @author Baidin Dima
 */
public class MultiLazy<T> implements Lazy<T> {
    private volatile Supplier<T> supplier;
    private volatile T value;

    public MultiLazy(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (supplier != null) {
            synchronized (this) {
                if (supplier != null) {
                    value = supplier.get();
                    supplier = null;
                }
            }
        }
        return value;
    }

}
