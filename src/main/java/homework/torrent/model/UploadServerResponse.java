package homework.torrent.model;

import homework.torrent.model.reader.LongReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.writer.LongWriter;
import homework.torrent.model.writer.ObjectWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class UploadServerResponse implements SerializableObject {
    private final long fileId;


    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new LongWriter(fileId);
    }

    public static final class Reader implements ObjectReader<UploadServerResponse> {
        @NotNull
        private final LongReader fileIdReader = new LongReader();
        @Nullable
        private UploadServerResponse result;

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return fileIdReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return fileIdReader.isReady();
        }

        @NotNull
        @Override
        public UploadServerResponse getResult() {
            if (result == null) {
                result = new UploadServerResponse(fileIdReader.getResult());
            }
            return result;
        }
    }
}
