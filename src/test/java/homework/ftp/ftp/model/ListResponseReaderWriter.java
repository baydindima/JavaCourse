package homework.ftp.ftp.model;

import homework.torrent.model.reader.ObjectReader;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests of asynchronous read/write for list response
 */
public class ListResponseReaderWriter {
    private AbstractReaderWriterTest<ListFtpResponse> getReaderWriterTest() {
        return new AbstractReaderWriterTest<>(ListFtpResponse.ListFtpResponseReader::new, t -> t.new ListFtpResponseWriter());
    }

    private AbstractReaderWriterTest<ListFtpResponse> getMaybeErrorReaderWriterTest() {
        return new AbstractReaderWriterTest<>(() -> new MaybeExceptionResponseReader<ListFtpResponse>() {
            @NotNull
            @Override
            protected ObjectReader<ListFtpResponse> getReader() {
                return new ListFtpResponse.ListFtpResponseReader();
            }
        }, t -> t.new ListFtpResponseWriter());
    }

    private AbstractReaderWriterTest<ListFtpResponse.FileInfo> getFileInfoReaderWriterTest() {
        return new AbstractReaderWriterTest<>(ListFtpResponse.FileInfo.FileInfoReader::new, t -> t.new FileInfoWriter());
    }

    @Test
    public void simpleTest() {
        ListFtpResponse.FileInfo file1 = new ListFtpResponse.FileInfo(false, "File1");
        ListFtpResponse.FileInfo file2 = new ListFtpResponse.FileInfo(false, "File2");
        ListFtpResponse.FileInfo file3 = new ListFtpResponse.FileInfo(false, "File3");
        ListFtpResponse.FileInfo file4 = new ListFtpResponse.FileInfo(false, "File4");
        ListFtpResponse.FileInfo dir1 = new ListFtpResponse.FileInfo(true, "Dir1");
        ListFtpResponse.FileInfo dir2 = new ListFtpResponse.FileInfo(true, "Dir2");
        ListFtpResponse.FileInfo dir3 = new ListFtpResponse.FileInfo(true, "Dir3");
        ListFtpResponse.FileInfo dir4 = new ListFtpResponse.FileInfo(true, "Dir4");
        List<ListFtpResponse.FileInfo> fileInfoList = new ArrayList<>();
        fileInfoList.add(file1);
        fileInfoList.add(file2);
        fileInfoList.add(file3);
        fileInfoList.add(file4);
        fileInfoList.add(dir1);
        fileInfoList.add(dir2);
        fileInfoList.add(dir3);
        fileInfoList.add(dir4);
        ListFtpResponse ftpResponse = new ListFtpResponse(8, fileInfoList);
        getReaderWriterTest().test(ftpResponse);
    }

    @Test
    public void maybeErrorTest() {
        ListFtpResponse.FileInfo file1 = new ListFtpResponse.FileInfo(false, "File1");
        ListFtpResponse.FileInfo file2 = new ListFtpResponse.FileInfo(false, "File2");
        ListFtpResponse.FileInfo file3 = new ListFtpResponse.FileInfo(false, "File3");
        ListFtpResponse.FileInfo file4 = new ListFtpResponse.FileInfo(false, "File4");
        ListFtpResponse.FileInfo dir1 = new ListFtpResponse.FileInfo(true, "Dir1");
        ListFtpResponse.FileInfo dir2 = new ListFtpResponse.FileInfo(true, "Dir2");
        ListFtpResponse.FileInfo dir3 = new ListFtpResponse.FileInfo(true, "Dir3");
        ListFtpResponse.FileInfo dir4 = new ListFtpResponse.FileInfo(true, "Dir4");
        List<ListFtpResponse.FileInfo> fileInfoList = new ArrayList<>();
        fileInfoList.add(file1);
        fileInfoList.add(file2);
        fileInfoList.add(file3);
        fileInfoList.add(file4);
        fileInfoList.add(dir1);
        fileInfoList.add(dir2);
        fileInfoList.add(dir3);
        fileInfoList.add(dir4);
        ListFtpResponse ftpResponse = new ListFtpResponse(8, fileInfoList);
        getMaybeErrorReaderWriterTest().test(ftpResponse);
    }

    @Test
    public void fileInfoTest() {
        ListFtpResponse.FileInfo fileInfo = new ListFtpResponse.FileInfo(false, "file1");
        getFileInfoReaderWriterTest().test(fileInfo);
        ListFtpResponse.FileInfo dir2 = new ListFtpResponse.FileInfo(true, "dir2");
        getFileInfoReaderWriterTest().test(dir2);
    }

}
