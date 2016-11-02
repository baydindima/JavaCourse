package homework.torrent.model;

import homework.ftp.ftp.model.reader.IntReader;
import homework.torrent.model.reader.ListObjectReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.writer.IntWriter;
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
public class StatClientResponse implements SerializableObject {
    @NotNull
    private final List<Integer> partIds;


    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new ListObjectWriter<>(partIds, IntWriter::new);
    }


    public final static class Reader implements ObjectReader<StatClientResponse> {
        @NotNull
        private final ListObjectReader<Integer> partsReader = new ListObjectReader<>(IntReader::new);
        @Nullable
        private StatClientResponse result;

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return partsReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return partsReader.isReady();
        }

        @NotNull
        @Override
        public StatClientResponse getResult() {
            if (result == null) {
                result = new StatClientResponse(partsReader.getResult());
            }
            return result;
        }
    }
}
