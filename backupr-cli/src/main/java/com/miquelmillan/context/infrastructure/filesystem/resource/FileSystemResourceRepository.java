package com.miquelmillan.context.infrastructure.filesystem.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


@Repository
@Qualifier("fsResourceRepository")
public class FileSystemResourceRepository implements ResourceRepository {
    @Override
    public void store(Resource item) throws IOException, ResourceRepositoryException {
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
        } else {
            throw new ResourceRepositoryException("Item cannot be null");
        }
    }

    @Override
    public Resource query(String path) throws IOException {
        Path _path = Paths.get(path);

        if (Files.exists(_path)) {
            return new Resource(_path.getFileName().toString(),
                    new Location(_path.toString()),
                    new Contents(_path.toString())
            );
        }

        return null;
    }
}


