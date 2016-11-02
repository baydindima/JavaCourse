package homework.torrent.model;

import homework.torrent.model.writer.ObjectWriter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Dmitriy Baidin.
 */
public interface SerializableObject {
    @NotNull ObjectWriter getWriter();
}
