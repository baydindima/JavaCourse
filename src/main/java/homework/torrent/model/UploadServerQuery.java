package homework.torrent.model;

import homework.torrent.exception.InvalidProcessorStateException;
import homework.torrent.model.reader.*;
import homework.torrent.model.writer.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class UploadServerQuery implements SerializableObject, TorrentServerQuery {
    @NotNull
    private final String fileName;
    private final long fileSize;

    @Override
    public @NotNull TorrentServerQuery.Type getType() {
        return Type.Upload;
    }

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new ByteWriter(Type.Upload.getId()),
                new StringWriter(fileName),
                new LongWriter(fileSize));
    }

    public static class Reader implements ObjectReader<UploadServerQuery> {
        @NotNull
        private final ByteReader typeReader = new ByteReader();
        @NotNull
        private final StringReader fileNameReader = new StringReader();
        @NotNull
        private final LongReader sizeReader = new LongReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(typeReader, fileNameReader, sizeReader);
        @Nullable
        private UploadServerQuery result;

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return seqReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return seqReader.isReady();
        }

        @NotNull
        @Override
        public UploadServerQuery getResult() {
            if (result == null) {
                if (typeReader.getResult() != Type.Upload.getId()) {
                    throw new InvalidProcessorStateException(
                            String.format("Expected upload type, but got %s", typeReader.getResult()));
                }
                result = new UploadServerQuery(
                        fileNameReader.getResult(),
                        sizeReader.getResult());
            }
            return result;
        }
    }
}
