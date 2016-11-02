package homework.torrent.model;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Dmitriy Baidin.
 */
public class FileReadWriteTest {
    public static void main(String[] args) throws IOException {
        AsynchronousFileChannel open = AsynchronousFileChannel.open(Paths.get("."), StandardOpenOption.READ);

    }

}
