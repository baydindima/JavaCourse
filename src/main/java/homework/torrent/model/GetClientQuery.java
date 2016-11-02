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
public class GetClientQuery implements SerializableObject, TorrentClientQuery {
    private final long fileId;
    private final int partId;

    @Override
    public @NotNull TorrentClientQuery.Type getType() {
        return Type.Get;
    }

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(new ByteWriter(getType().getId()),
                new LongWriter(fileId),
                new IntWriter(partId));
    }

    public final static class Reader implements ObjectReader<GetClientQuery> {
        @NotNull
        private final ByteReader typeReader = new ByteReader();
        @NotNull
        private final LongReader fileIdReader = new LongReader();
        @NotNull
        private final IntReader partIdReader = new IntReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(typeReader, fileIdReader, partIdReader);
        @Nullable
        private GetClientQuery result;


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
        public GetClientQuery getResult() {
            if (result == null) {
                if (typeReader.getResult() != TorrentServerQuery.Type.Source.getId()) {
                    throw new InvalidProcessorStateException(
                            String.format("Expected get type, but got %s", typeReader.getResult()));
                }
                result = new GetClientQuery(fileIdReader.getResult(), partIdReader.getResult());
            }
            return result;
        }
    }
}
