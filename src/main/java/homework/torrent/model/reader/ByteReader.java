package homework.torrent.model.reader;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Dmitriy Baidin.
 */
public class ByteReader extends AbstractObjectReader<Byte> {

    public ByteReader() {
        super(1);
    }

    @Override
    protected Byte getResultFromArray(@NotNull byte[] array) {
        return array[0];
    }
}
