package homework.torrent.model.writer;

/**
 * Created by Dmitriy Baidin.
 */
public class ByteWriter extends AbstractObjectWriter {
    /**
     * Create new instance of abstract object writer.
     *
     * @param value inner array for writing
     */
    public ByteWriter(final byte value) {
        super(new byte[]{value});
    }
}
