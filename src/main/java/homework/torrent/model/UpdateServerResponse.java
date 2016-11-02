package homework.torrent.model;

import homework.torrent.model.reader.BooleanReader;
import homework.torrent.model.reader.ObjectReader;
import homework.torrent.model.writer.BooleanWriter;
import homework.torrent.model.writer.ObjectWriter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Created by Dmitriy Baidin.
 */
@Data
public class UpdateServerResponse implements SerializableObject {
    private final boolean status;

    @NotNull
    @Override
    public ObjectWriter getWriter() {
        return new BooleanWriter(status);
    }


    public final static class Reader implements ObjectReader<UpdateServerResponse> {
        @NotNull
        private final BooleanReader statusReader = new BooleanReader();
        @Nullable
        private UpdateServerResponse result;


        @Override
        public int read(@NotNull final ByteBuffer byteBuffer) {
            return statusReader.read(byteBuffer);
        }

        @Override
        public boolean isReady() {
            return statusReader.isReady();
        }

        @NotNull
        @Override
        public UpdateServerResponse getResult() {
            if (result == null) {
                result = new UpdateServerResponse(statusReader.getResult());
            }
            return result;
        }
    }


}
