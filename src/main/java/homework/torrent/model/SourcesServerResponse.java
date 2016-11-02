package homework.torrent.model;

import homework.torrent.model.reader.ListObjectReader;
import homework.torrent.model.reader.ObjectReader;
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
public class SourcesServerResponse implements SerializableObject {
    @NotNull
    private final List<ClientInfo> clients;

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new ListObjectWriter<>(clients, ClientInfo::getWriter);
    }

    public final static class Reader implements ObjectReader<SourcesServerResponse> {
        @NotNull
        private final ListObjectReader<ClientInfo> clientsReader =
                new ListObjectReader<>(ClientInfo.Reader::new);
        @Nullable
        private SourcesServerResponse result;

        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return clientsReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return clientsReader.isReady();
        }

        @NotNull
        @Override
        public SourcesServerResponse getResult() {
            if (result == null) {
                result = new SourcesServerResponse(clientsReader.getResult());
            }
            return result;
        }
    }
}
