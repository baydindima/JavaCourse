package homework.torrent.model;

import homework.torrent.model.reader.IntReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.reader.SequenceObjectReader;
import homework.torrent.model.reader.ShortReader;
import homework.torrent.model.writer.IntWriter;
import homework.torrent.model.writer.ObjectWriter;
import homework.torrent.model.writer.SeqObjectWriter;
import homework.torrent.model.writer.ShortWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class ClientInfo implements SerializableObject {
    @NotNull
    private final byte[] address;
    private final short port;
    private final long lastUpdate;


    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new SeqObjectWriter(
                new IntWriter(ByteBuffer.wrap(address).getInt()),
                new ShortWriter(port)
        );
    }

    public final static class Reader implements ObjectReader<ClientInfo> {
        @NotNull
        private final IntReader ipReader = new IntReader();
        @NotNull
        private final ShortReader portReader = new ShortReader();
        @NotNull
        private final SequenceObjectReader seqReader = new SequenceObjectReader(ipReader, portReader);
        @Nullable
        private ClientInfo result;

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
        public ClientInfo getResult() {
            if (result == null) {
                result = new ClientInfo(
                        ByteBuffer.allocate(Integer.BYTES).putInt(ipReader.getResult()).array(),
                        portReader.getResult(),
                        System.currentTimeMillis());
            }
            return result;
        }
    }
}
