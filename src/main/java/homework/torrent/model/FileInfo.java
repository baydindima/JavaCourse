package homework.torrent.model;

import homework.torrent.model.reader.LongReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.reader.SequenceObjectReader;
import homework.torrent.model.reader.StringReader;
import homework.torrent.model.writer.LongWriter;
import homework.torrent.model.writer.ObjectWriter;
import homework.torrent.model.writer.SeqObjectWriter;
import homework.torrent.model.writer.StringWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;


/**
 * Created by Dmitriy Baidin.
 */
@Data
public class FileInfo implements SerializableObject {
    private final long id;
    @NotNull
    private final String name;
    private final long size;
    private final long lastUpdate;

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new LongWriter(id),
                new StringWriter(name),
                new LongWriter(size)
        );
    }

    public final static class Reader implements ObjectReader<FileInfo> {
        @NotNull
        private final LongReader idReader = new LongReader();
        @NotNull
        private final StringReader nameReader = new StringReader();
        @NotNull
        private final LongReader sizeReader = new LongReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(idReader, nameReader, sizeReader);
        @Nullable
        private FileInfo result;

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
        public FileInfo getResult() {
            if (result == null) {
                result = new FileInfo(
                        idReader.getResult(),
                        nameReader.getResult(),
                        sizeReader.getResult(),
                        System.currentTimeMillis());
            }
            return result;
        }
    }
}
