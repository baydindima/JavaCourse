package homework.ftp.ftp.model;

import org.junit.Test;

/**
 * Tests of asynchronous read/write for list query
 */
public class ListQueryReaderWriterTest {

    private AbstractReaderWriterTest<ListFtpQuery> getReaderWriterTest() {
        return new AbstractReaderWriterTest<>(FtpQueryReader::new, t -> t.new ListFtpQueryWriter());
    }

    @Test
    public void simpleTest() {
        ListFtpQuery ftpQuery = new ListFtpQuery("filepath");
        getReaderWriterTest().test(ftpQuery);
    }

}
