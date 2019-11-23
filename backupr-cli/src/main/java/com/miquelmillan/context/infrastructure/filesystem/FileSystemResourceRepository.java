package com.miquelmillan.context.infrastructure.filesystem;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    @Override
    public ResourceResult store(Resource item) throws IOException, ResourceRepositoryException {
        throw new NotImplementedException();
    }

    @Override
    public ResourceResult query(String path) throws IOException {
        Map<String, Resource> files = new HashMap();
        Path _path = Paths.get(path);

        try (Stream<Path> paths = Files.walk(_path)) {
            paths.filter(Files::isRegularFile)
                    .forEach((elem) -> {
                        try {
                            files.put(
                                    elem.toString(),
                                    new Resource(UUID.randomUUID().toString(),
                                            new Location(elem.toString()),
                                            new Contents(elem.toString())
                                    ));
                        } catch (FileNotFoundException e) {
                            // TODO: Define a proper exception handling
                            files.put(elem.toString(), null);
                        }
                    });

        }

        return new ResourceResult(files);
    }
}


