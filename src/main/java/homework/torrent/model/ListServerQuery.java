package homework.torrent.model;


import homework.torrent.exception.InvalidProcessorStateException;
import homework.torrent.model.reader.ByteReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.writer.ByteWriter;
import homework.torrent.model.writer.ObjectWriter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
public class ListServerQuery implements TorrentServerQuery, SerializableObject {
    @Override
    public @NotNull TorrentServerQuery.Type getType() {
        return Type.List;
    }


    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new ByteWriter(Type.List.getId());
    }

    public static class Reader implements ObjectReader<ListServerQuery> {
        @NotNull
        private final ByteReader typeReader = new ByteReader();

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return typeReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return typeReader.isReady();
        }

        @NotNull
        @Override
        public ListServerQuery getResult() {
            if (typeReader.getResult() == Type.List.getId()) {
                return new ListServerQuery();
            } else {
                throw new InvalidProcessorStateException(
                        String.format("Expected list type, but got %s", typeReader.getResult()));
            }
        }
    }

}
