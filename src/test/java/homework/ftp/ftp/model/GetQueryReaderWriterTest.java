package homework.ftp.ftp.model;

import org.junit.Test;

/**
 * Tests of asynchronous read/write for get query
 */
public class GetQueryReaderWriterTest {

    private AbstractReaderWriterTest<GetFtpQuery> getReaderWriterTest() {
        return new AbstractReaderWriterTest<>(FtpQueryReader::new, t -> t.new GetFtpQueryWriter());
    }

    @Test
    public void simpleTest() {
        GetFtpQuery ftpQuery = new GetFtpQuery("filepath");
        getReaderWriterTest().test(ftpQuery);
    }

}
