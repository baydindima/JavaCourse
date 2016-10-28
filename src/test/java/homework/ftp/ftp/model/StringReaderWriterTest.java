package homework.ftp.ftp.model;

import homework.ftp.ftp.model.reader.StringReader;
import homework.ftp.ftp.model.writer.StringWriter;
import org.junit.Test;

/**
 * Tests of asynchronous read/write for strings
 */
public class StringReaderWriterTest {

    private AbstractReaderWriterTest<String> getReaderWriterTest() {
        return new AbstractReaderWriterTest<>(StringReader::new, StringWriter::new);
    }

    @Test
    public void emptyStringReadWrite() {
        final String text = "";
        getReaderWriterTest().test(text);
    }

    @Test
    public void nonEmptyStringReadWrite() {
        final String text = "abcd123/zzz";
        getReaderWriterTest().test(text);
    }

    @Test
    public void stringWithRussianLettersReadWrite() {
        final String text = "тест123тест";
        getReaderWriterTest().test(text);
    }

}
