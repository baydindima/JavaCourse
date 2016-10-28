package homework.ftp.ftp.model.writer;

import java.nio.ByteBuffer;

/**
 * ObjectWriter for string.
 */
public class IntWriter extends AbstractObjectWriter {
    /**
     * Create new instance of int writer.
     *
     * @param value value to write
     */
    public IntWriter(final int value) {
        super(ByteBuffer.allocate(Integer.BYTES).putInt(value).array());
    }
}
