package homework.torrent.model;

import homework.torrent.model.writer.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class UpdateServerQuery implements SerializableObject, TorrentServerQuery {
    private final short port;
    private final int fileCount;
    @NotNull
    private final List<Long> fileIds;

    @Override
    public @NotNull TorrentServerQuery.Type getType() {
        return Type.Update;
    }

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new ByteWriter(Type.Update.getId()),
                new ShortWriter(port),
                new IntWriter(fileCount),
                new ListObjectWriter<>(fileIds, LongWriter::new)
        );
    }
}
