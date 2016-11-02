package homework.torrent.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Dmitriy Baidin.
 */
public interface TorrentClientQuery {

    @NotNull Type getType();

    enum Type {
        Stat((byte) 1),
        Get((byte) 2);
        @Getter
        private final byte id;

        Type(final byte value) {
            this.id = value;
        }
    }

}
