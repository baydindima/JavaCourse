package homework.torrent.app;

import homework.torrent.model.ClientInfo;
import homework.torrent.model.FileInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dmitriy Baidin.
 */
public class TrackerTracker {
    @NotNull
    private final AtomicLong lastId = new AtomicLong(0);

    @NotNull
    private final ConcurrentMap<Long, Queue<ClientInfo>> clients = new ConcurrentHashMap<>();
    @NotNull
    private final ConcurrentMap<Long, FileInfo> files = new ConcurrentHashMap<>();


}
