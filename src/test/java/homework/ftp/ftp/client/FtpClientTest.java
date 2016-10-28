package homework.ftp.ftp.client;

import homework.ftp.ftp.model.GetFtpResponse;
import homework.ftp.ftp.model.ListFtpResponse;
import homework.ftp.ftp.server.FtpServer;
import homework.ftp.ftp.server.Server;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Integrate tests.
 */
public class FtpClientTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void executeList() throws Exception {
        temporaryFolder.newFolder("folder1");
        temporaryFolder.newFile("file1");
        Server server = new FtpServer(14765, temporaryFolder.getRoot().toPath());
        server.start();
        Thread.sleep(3000);
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 14765);
        FtpClient ftpClient = new FtpClient(hostAddress);


        ListFtpResponse listResponse = ftpClient.executeList(".");
        assertEquals(2, listResponse.getSize());
        assertThat(listResponse.getFileInfoList(), contains(
                new ListFtpResponse.FileInfo(true, "folder1"),
                new ListFtpResponse.FileInfo(false, "file1")
        ));

        Thread.sleep(3000);
        server.close();
    }

    @Test
    public void executeGet() throws Exception {
        File file1 = temporaryFolder.newFile("file1");

        FileWriter fileWriter = new FileWriter(file1);
        fileWriter.write("Hello!");
        fileWriter.close();

        Server server = new FtpServer(14766, temporaryFolder.getRoot().toPath());
        server.start();

        Thread.sleep(3000);

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 14766);
        FtpClient ftpClient = new FtpClient(hostAddress);

        File file2 = temporaryFolder.newFile("file2");
        GetFtpResponse getResponse = ftpClient.executeGet("file1", file2);
        assertEquals("Hello!", new String(Files.readAllBytes(file2.toPath())));
        assertEquals(getResponse.getSize(), file1.length());
        assertEquals(getResponse.getSize(), file2.length());
        Thread.sleep(3000);
        server.close();
    }

}