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
public class StatClientQuery implements TorrentClientQuery, SerializableObject {
    private final long fileId;

    @Override
    public @NotNull Type getType() {
        return Type.Stat;
    }

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new ByteWriter(getType().getId()),
                new LongWriter(fileId)
        );
    }

    public final static class Reader implements ObjectReader<StatClientQuery> {
        @NotNull
        private final ByteReader typeReader = new ByteReader();
        @NotNull
        private final LongReader idReader = new LongReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(typeReader, idReader);
        @Nullable
        private StatClientQuery result;

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
        public StatClientQuery getResult() {
            if (result == null) {
                if (typeReader.getResult() != Type.Stat.getId()) {
                    throw new InvalidProcessorStateException(
                            String.format("Expected stat type, but got %s", typeReader.getResult()));
                }
                result = new StatClientQuery(idReader.getResult());
            }
            return result;
        }
    }
}
