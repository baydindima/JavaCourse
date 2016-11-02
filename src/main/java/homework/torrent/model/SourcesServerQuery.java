package homework.torrent.model;

import homework.torrent.exception.InvalidProcessorStateException;
import homework.torrent.model.reader.ByteReader;
import homework.torrent.model.reader.LongReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.reader.SequenceObjectReader;
import homework.torrent.model.writer.ByteWriter;
import homework.torrent.model.writer.LongWriter;
import homework.torrent.model.writer.ObjectWriter;
import homework.torrent.model.writer.SeqObjectWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class SourcesServerQuery implements SerializableObject, TorrentServerQuery {
    private final long fileId;

    @Override
    public @NotNull TorrentServerQuery.Type getType() {
        return Type.Source;
    }

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new ByteWriter(Type.Source.getId()),
                new LongWriter(fileId)
        );
    }

    public final static class Reader implements ObjectReader<SourcesServerQuery> {
        @NotNull
        private final ByteReader typeReader = new ByteReader();
        @NotNull
        private final LongReader fileIdReader = new LongReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(typeReader, fileIdReader);
        @Nullable
        private SourcesServerQuery result;

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
        public SourcesServerQuery getResult() {
            if (result == null) {
                if (typeReader.getResult() != Type.Source.getId()) {
                    throw new InvalidProcessorStateException(
                            String.format("Expected sources type, but got %s", typeReader.getResult()));
                }
                result = new SourcesServerQuery(fileIdReader.getResult());
            }
            return result;
        }
    }
}
