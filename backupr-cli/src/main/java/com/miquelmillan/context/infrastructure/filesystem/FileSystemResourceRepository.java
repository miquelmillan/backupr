package com.miquelmillan.context.infrastructure.filesystem;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
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
        Map<String, Resource> resources = new HashMap();

        if (item != null
                && item.getContents() != null
                && item.getContents().getInputStream() != null) {

            File f = new File(item.getLocation().getLocation());
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            f.createNewFile();

            try (OutputStream output = new BufferedOutputStream(new FileOutputStream(item.getLocation().getLocation()));
                InputStream input = item.getContents().getInputStream()) {
                byte[] read_buf = new byte[1024];
                int read_len;

                while ((read_len = input.read(read_buf)) > 0) {
                    output.write(read_buf, 0, read_len);
                }
            } catch (Exception e){
                throw e;
            }
            resources.put(item.getName(), item);
        } else {
            throw new ResourceRepositoryException("Item cannot be null");
        }

        return new ResourceResult(resources);
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


