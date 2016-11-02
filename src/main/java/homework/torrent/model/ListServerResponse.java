package homework.torrent.model;

import homework.torrent.model.reader.ListObjectReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.writer.ListObjectWriter;
import homework.torrent.model.writer.ObjectWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class ListServerResponse implements SerializableObject {
    @NotNull
    private final List<FileInfo> files;


    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new ListObjectWriter<>(files, FileInfo::getWriter);
    }

    public final static class Reader implements ObjectReader<ListServerResponse> {
        @NotNull
        private final ListObjectReader<FileInfo> filesReader = new ListObjectReader<>(FileInfo.Reader::new);
        @Nullable
        private ListServerResponse result;

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return filesReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return filesReader.isReady();
        }

        @NotNull
        @Override
        public ListServerResponse getResult() {
            if (result == null) {
                result = new ListServerResponse(filesReader.getResult());
            }
            return result;
        }
    }
}
