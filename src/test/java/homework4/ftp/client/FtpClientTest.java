package homework4.ftp.client;

import homework4.ftp.model.GetResponse;
import homework4.ftp.model.ListResponse;
import homework4.ftp.server.FtpServer;
import homework4.ftp.server.Server;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.net.InetSocketAddress;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Dmitriy Baidin.
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

        ListResponse listResponse = ftpClient.executeList(".");
        assertEquals(2, listResponse.getSize());
        assertThat(listResponse.getFileInfoList(), contains(
                new ListResponse.FileInfo(true, "folder1"),
                new ListResponse.FileInfo(false, "file1")
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

        GetResponse getResponse = ftpClient.executeGet("file1");
        assertEquals("Hello!", new String(getResponse.getContent()));

        Thread.sleep(3000);
        server.close();
    }

}