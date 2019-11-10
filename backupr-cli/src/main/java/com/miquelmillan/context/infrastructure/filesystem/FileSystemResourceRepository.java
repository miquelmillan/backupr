package com.miquelmillan.context.infrastructure.filesystem;

import com.miquelmillan.context.domain.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


@Repository
@Qualifier("fsResourceRepository")
public class FileSystemResourceRepository implements ResourceRepository {
    public FileSystemResourceRepository() {
    }

    @Override
    public ResourceResult store(Resource item) throws IOException, ResourceRepositoryException {
        return null;
    }

    @Override
    public ResourceResult query(String path) throws IOException {
        Map<String, Resource> files = new HashMap();
        Path _path = Paths.get(path);

        try (Stream<Path> paths = Files.walk(_path)) {
            paths.filter(Files::isRegularFile)
                    .forEach((elem) -> files.put(
                                                elem.toString(),
                                                new Resource(UUID.randomUUID().toString(),
                                                new Location(elem.toString()))));
        }

        return new ResourceResult(files);
    }
}


