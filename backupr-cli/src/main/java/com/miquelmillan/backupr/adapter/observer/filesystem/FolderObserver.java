package com.miquelmillan.backupr.adapter.observer.filesystem;

import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class FolderObserver extends Observable {
    private static Logger LOG = LoggerFactory.getLogger(FolderObserver.class);

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private boolean trace;


    public FolderObserver(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap();

        LOG.debug("Scanning {} ...\n", dir);
        registerAll(dir);
        LOG.debug("Done.");

        // enable trace after initial registration
        this.trace = true;
    }

    public void processEvents() {
        do {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);

            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path name = ev.context();
                Path child = dir.resolve(name);

                // Tell the consumer of this watcher to do stuff
                LOG.debug("{}: {}\n", event.kind().name(), child);
                this.notifyEvent(event, child);

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readable
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        } while (true);
    }

    private void notifyEvent(WatchEvent<?> event, Path child) {
        WatchEvent.Kind kind = event.kind();
        ResourceEvent resEvent = new ResourceEvent();
        Resource resource = new Resource(new Location(child.toString()));

        resEvent.setResource(resource);

        if (kind == ENTRY_CREATE) {
            resEvent.setType(ResourceEvent.EventType.CREATED);
        } else if (kind == ENTRY_MODIFY) {
            resEvent.setType(ResourceEvent.EventType.UPDATED);
        } else if (kind == ENTRY_DELETE) {
            resEvent.setType(ResourceEvent.EventType.DELETED);
        }

        if (resEvent.getType() != null) {
            this.notifyObservers(resEvent);
            this.setChanged();
        } else {
            this.clearChanged();
        }
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                LOG.debug("register: {}\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    LOG.debug("update: {} -> {}\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }
}