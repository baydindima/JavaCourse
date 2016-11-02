package homework.ftp.ftp.model;

import homework.ftp.ftp.exception.InvalidProcessorStateException;
import homework.ftp.ftp.model.reader.IntReader;
import homework.torrent.model.reader.ObjectReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Abstract class for reader where expected exception.
 *
 * @param <T> result type
 */
public abstract class MaybeExceptionResponseReader<T extends FtpResponse>
        implements ObjectReader<FtpResponse> {

    /**
     * Reader of type.
     */
    @NotNull
    private final IntReader typeReader = new IntReader();
    /**
     * Exception reader.
     */
    @Nullable
    private ExceptionFtpResponse.ExceptionFtpResponseReader exceptionReader;
    /**
     * Normal result reader.
     */
    @Nullable
    private ObjectReader<T> otherReader;

    /**
     * Exception result.
     */
    @Nullable
    private ExceptionFtpResponse exceptionResult;
    /**
     * Normal result.
     */
    @Nullable
    private T otherResult;

    /**
     * Factory of normal result reader.
     *
     * @return normal result reader
     */
    @NotNull
    protected abstract ObjectReader<T> getReader();

    /**
     * Add data to processing object.
     *
     * @param byteBuffer buffer which contains data
     * @return count of read bytes
     */
    @Override
    public final int read(@NotNull final ByteBuffer byteBuffer) {
        int result = 0;
        result += typeReader.read(byteBuffer);
        if (typeReader.isReady()) {
            if (typeReader.getResult()
                    != FtpResponse.FtpResponseType.ExceptionType.getValue()) {
                if (otherReader == null) {
                    otherReader = getReader();
                    ByteBuffer intBuffer = ByteBuffer
                            .allocate(Integer.BYTES)
                            .putInt(typeReader.getResult());
                    intBuffer.flip();
                    otherReader.read(intBuffer);
                }
                result += otherReader.read(byteBuffer);
            } else {
                if (exceptionReader == null) {
                    exceptionReader =
                            new ExceptionFtpResponse
                                    .ExceptionFtpResponseReader();
                    ByteBuffer intBuffer =
                            ByteBuffer
                                    .allocate(Integer.BYTES)
                                    .putInt(typeReader.getResult());
                    intBuffer.flip();
                    exceptionReader.read(intBuffer);
                }
                result += exceptionReader.read(byteBuffer);
            }
        }
        return result;
    }

    /**
     * Reader is ready to get result.
     *
     * @return true if ready, false otherwise
     */
    @Override
    public final boolean isReady() {
        return typeReader.isReady()
                && (
                (otherReader != null && otherReader.isReady())
                        || (exceptionReader != null && exceptionReader.isReady()
                )
        );
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
    public final FtpResponse getResult() {
        if (!isReady()) {
            throw new InvalidProcessorStateException("Response doesn't ready!");
        }
        if (typeReader.getResult()
                != FtpResponse.FtpResponseType.ExceptionType.getValue()) {
            if (otherResult == null) {
                assert otherReader != null;
                otherResult = otherReader.getResult();
            }
            return otherResult;
        } else {
            if (exceptionResult == null) {
                assert exceptionReader != null;
                exceptionResult = exceptionReader.getResult();
            }
            return exceptionResult;
        }
    }
}
