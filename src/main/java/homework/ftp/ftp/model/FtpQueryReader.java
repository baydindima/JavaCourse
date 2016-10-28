package homework.ftp.ftp.model;

import homework.ftp.ftp.exception.InvalidQueryFormat;
import homework.ftp.ftp.model.reader.IntReader;
import homework.ftp.ftp.model.reader.StringReader;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Common reader of queries.
 */
public class FtpQueryReader implements ObjectReader<FtpQuery> {
    /**
     * Reader of type.
     */
    @NotNull
    private IntReader typeReader = new IntReader();
    /**
     * Message reader.
     */
    @NotNull
    private StringReader stringReader = new StringReader();

    /**
     * Add data to processing object.
     *
     * @param byteBuffer buffer which contains data
     * @return count of read bytes
     */
    @Override
    public final int read(@NotNull final ByteBuffer byteBuffer) {
        return typeReader.read(byteBuffer) + stringReader.read(byteBuffer);
    }

    /**
     * Reader is ready to get result.
     *
     * @return true if ready, false otherwise
     */
    @Override
    public final boolean isReady() {
        return typeReader.isReady() && stringReader.isReady();
    }

    /**
     * Return reader result.
     *
     * @return completed object
     * @throws InvalidProcessorStateException if processor not ready
     *                                        for creation of object
     */
    @NotNull
    @Override
    public final FtpQuery getResult() {
        switch (typeReader.getResult()) {
            case 1:
                return new GetFtpQuery(stringReader.getResult());
            case 2:
                return new ListFtpQuery(stringReader.getResult());
            default:
                throw new InvalidQueryFormat("No such type of query"
                        + typeReader.getResult());
        }
    }
}

