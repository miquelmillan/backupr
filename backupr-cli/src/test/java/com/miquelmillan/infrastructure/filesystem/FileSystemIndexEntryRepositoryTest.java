package com.miquelmillan.infrastructure.filesystem;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.infrastructure.filesystem.index.FileSystemIndexEntryRepository;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;


public class FileSystemIndexEntryRepositoryTest {
    IndexEntryRepository repo;
    String basePath;

    @Before
    public void setUp() throws IOException {
        this.basePath = ResourceRepository.class.getClassLoader().getResource("").getFile();
        this.repo = new FileSystemIndexEntryRepository(this.basePath + "index.json");

    }

    @After
    public void tearDown() {
        // Cleanup created files
        File f = new File(
                ResourceRepository.class.getClassLoader().getResource("").getPath() +
                        "index.json");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void pathWithFiles_IndexPath_IndexOk() throws IOException {
        assertTrue(this.repo.listAll().size() == 0);
    }

    @Test
    public void pathWithFiles_IndexPath_AddItemOk() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(this.basePath), null, true);

        for (File file : files) {
            Resource r = new Resource(
                    UUID.randomUUID(),
                    file.getName(),
                    new Location(file.getPath()),
                    new Contents(file.getPath()));
            IndexEntry<Resource> entry = new IndexEntry(r);
            repo.addOrUpdate(entry);
        }

        validateIndexContents(files);

    }

    @Test
    public void pathWithFiles_IndexPath_AddItemsOk() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(this.basePath), null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(
                    new Resource(UUID.randomUUID(),
                                file.getName(),
                                new Location(file.getPath()),
                                new Contents(file.getPath()))
                    )
            );
        }

        repo.addOrUpdate(entries);
        validateIndexContents(files);
    }

    @Test
    public void pathWithFiles_IndexPath_RemoveItemOk() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(this.basePath), null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(
                            new Resource(UUID.randomUUID(),
                                    file.getName(),
                                    new Location(file.getPath()),
                                    new Contents(file.getPath()))
                    )
            );
        }

        repo.addOrUpdate(entries);
        IndexEntry removedItem = new ArrayList<IndexEntry>(repo.listAll()).get(0);
        this.repo.remove(removedItem);

        //Validate removal of entry
        List<IndexEntry> repoEntries = new ArrayList(repo.listAll());
        Set<String> repoLocations = new HashSet<>();

        repoEntries.forEach(
                elem -> repoLocations.add(
                        ((Resource) elem.getElement()).getLocation().getLocation()
                )
        );


        assertEquals("Repo entries and files length are different", repoEntries.size(), files.size() - 1);
        assertTrue(repoEntries.get(0).getElement() instanceof Resource);

        files.stream().forEach(
                file -> {
                    if (file.getPath() == ((Resource)removedItem.getElement()).getLocation().getLocation()) {
                        assertFalse("Entry has been removed without being asked",
                                repoLocations.contains(file.getPath())
                        );
                    } else {
                        assertTrue("Removed entry is still in repo",
                                repoLocations.contains(file.getPath())
                        );
                    }
                }
        );

    }

    @Test
    public void pathWithFiles_IndexPath_SearchByPathOk() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(this.basePath), null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(
                            new Resource(UUID.randomUUID(),
                                    file.getName(),
                                    new Location(file.getPath()),
                                    new Contents(file.getPath()))
                    )
            );
        }

        repo.addOrUpdate(entries);

        List<Resource> search = repo.get(new Resource(UUID.randomUUID(), "dummy", new Location(this.basePath), null));

        assertEquals(search.size(), files.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pathWithFiles_IndexPath_ConstructorKo() throws IOException {
        this.repo = new FileSystemIndexEntryRepository(null);
    }

    private void validateIndexContents(Collection<File> files) {
        List<IndexEntry> repoEntries = new ArrayList(repo.listAll());
        Set<String> repoLocations = new HashSet<>();

        repoEntries.forEach(
                elem -> repoLocations.add(
                        ((Resource) elem.getElement()).getLocation().getLocation()
                )
        );

        assertEquals("Repo entries and files length are different", repoEntries.size(), files.size());
        assertTrue(repoEntries.get(0).getElement() instanceof Resource);

        files.stream().forEach(
                file -> assertTrue("First entry and file are different",
                        repoLocations.contains(file.getPath())
                )
        );
    }
}
