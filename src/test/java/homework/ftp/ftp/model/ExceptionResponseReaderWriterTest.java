package homework.ftp.ftp.model;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * Tests of asynchronous read/write for exception response
 */
public class ExceptionResponseReaderWriterTest {

    private AbstractReaderWriterTest<ExceptionFtpResponse> getMaybeErrorReaderWriterTest() {
        return new AbstractReaderWriterTest<>(() -> new MaybeExceptionResponseReader<ListFtpResponse>() {
            @NotNull
            @Override
            protected ObjectReader<ListFtpResponse> getReader() {
                return new ListFtpResponse.ListFtpResponseReader();
            }
        }, t -> t.new ExceptionFtpResponseWriter());
    }

    @Test
    public void simpleTest() {
        ExceptionFtpResponse response = new ExceptionFtpResponse("Test exception!");
        getMaybeErrorReaderWriterTest().test(response);
    }
}
