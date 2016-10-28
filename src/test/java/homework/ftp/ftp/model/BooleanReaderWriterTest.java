package homework.ftp.ftp.model;

import homework.ftp.ftp.model.reader.BooleanReader;
import homework.ftp.ftp.model.writer.BooleanWriter;
import org.junit.Test;

/**
 * Tests of asynchronous read/write for boolean
 */
public class BooleanReaderWriterTest {

    private AbstractReaderWriterTest<Boolean> getReaderWriterTest() {
        return new AbstractReaderWriterTest<>(BooleanReader::new, BooleanWriter::new);
    }

    @Test
    public void trueReadWrite() {
        getReaderWriterTest().test(true);
    }

    @Test
    public void falseReadWrite() {
        getReaderWriterTest().test(false);
    }
}
