package homework.torrent.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Dmitriy Baidin.
 */
public interface TorrentServerQuery {

    @NotNull Type getType();

    enum Type {
        List((byte) 1),
        Upload((byte) 2),
        Source((byte) 3),
        Update((byte) 4);
        @Getter
        private final byte id;

        Type(final byte value) {
            this.id = value;
        }
    }

}
