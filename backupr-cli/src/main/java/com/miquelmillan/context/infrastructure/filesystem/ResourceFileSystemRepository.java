package com.miquelmillan.context.infrastructure.filesystem;

import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class ResourceFileSystemRepository implements ResourceRepository {
    public ResourceFileSystemRepository() {
    }

    @Override
    public ResourceResult query(String path) throws IOException {
        Map<String, Object> files = new HashMap<String, Object>();
        Path _path = Paths.get(path);

        try (Stream<Path> paths = Files.walk(_path)) {
            paths.filter(Files::isRegularFile)
                    .forEach((elem) -> files.put(elem.toString(), elem));
        }

        return new ResourceResult(files);
    }
}


