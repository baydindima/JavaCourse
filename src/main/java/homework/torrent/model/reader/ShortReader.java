package homework.torrent.model.reader;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
public class ShortReader extends AbstractObjectReader<Short> {
    public ShortReader() {
        super(Short.BYTES);
    }

    @Override
    protected Short getResultFromArray(@NotNull byte[] array) {
        return ByteBuffer.wrap(array).getShort();
    }
}
