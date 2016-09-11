package homework1;

import java.util.function.Supplier;

/**
 * @author Baidin Dima
 */
public class LazyFactory {

    private LazyFactory() {
    }

    public static <T> Lazy<T> getSingleLazy(Supplier<T> supplier) {
        return new SingleLazy<>(supplier);
    }

    public static <T> Lazy<T> getLockFreeLazy(Supplier<T> supplier) {
        return new LockFreeLazy<>(supplier);
    }

    public static <T> Lazy<T> getMultiLazy(Supplier<T> supplier) {
        return new MultiLazy<>(supplier);
    }

}
