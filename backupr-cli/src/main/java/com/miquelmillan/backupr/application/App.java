package com.miquelmillan.backupr.application;

import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.uc.port.Resource;
import com.miquelmillan.backupr.uc.port.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.file.BackupFile;
import com.miquelmillan.backupr.uc.port.file.RestoreFile;
import com.miquelmillan.backupr.uc.port.folder.BackupFolder;
import com.miquelmillan.backupr.uc.port.folder.IndexFolder;
import com.miquelmillan.backupr.uc.port.folder.ListIndexedFolder;
import com.miquelmillan.backupr.uc.port.folder.RestoreFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@ComponentScan("com.miquelmillan.context")
public class App implements CommandLineRunner {

    public static int ERROR_PARAMETERS_WRONG_NUMBER = 1;
    public static int ERROR_RUNTIME_EXCEPTION = 2;

    private static Logger LOG = LoggerFactory.getLogger(App.class);

    @Autowired
    @Qualifier("backupFolderUC")
    private BackupFolder backupFolder;

    @Autowired
    @Qualifier("restoreFolderUC")
    private RestoreFolder restoreFolder;

    @Autowired
    @Qualifier("indexFolderUC")
    private IndexFolder indexFolder;

    @Autowired
    @Qualifier("listIndexedFolderUC")
    private ListIndexedFolder listFolder;

    @Autowired
    @Qualifier("backupFileUC")
    private BackupFile backupFile;

    @Autowired
    @Qualifier("restoreFileUC")
    private RestoreFile restoreFile;


    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(App.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }

        if (args.length % 2 != 0) {
            System.out.println("Wrong argument number");
            System.exit(this.ERROR_PARAMETERS_WRONG_NUMBER);
        } else {
            try {
                this.parseParameters(args);
            } catch (Exception e) {
                LOG.error("Error parsing parameters", e);
                System.exit(this.ERROR_RUNTIME_EXCEPTION);
            }
        }

        System.out.println("Finishing execution, all good :-)");
        System.exit(0);
    }

    private boolean parseParameters(String... args) throws ResourceUnavailableException,
            ResourceRepositoryException,
            ResourceUnknownException, IOException {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-u":
                    try {
                        if (isUUID(args[i+1])) {
                            LOG.info("Processing resource: {}", args[i+1]);
                            backupFile.backupFile(UUID.fromString(args[i+1]));
                            LOG.info("Resource processed");
                        } else {
                            LOG.info("Processing location: {}", args[i+1]);
                            backupFolder.backupFolder(new File(args[i+1]));
                            LOG.info("Location processed");
                        }
                    } catch (IOException e) {
                        LOG.error("Error while uploading resource", e);
                        return false;
                    }
                    break;
                case "-d":
                    try {
                        if (isUUID(args[i+1])) {
                            LOG.info("Processing resource: {}", args[i+1]);
                            restoreFile.restoreFile(UUID.fromString(args[i+1]));
                            LOG.info("Resource processed");
                        } else {
                            LOG.info("Processing location: {}", args[i+1]);
                            restoreFolder.restoreFolder(new File(args[i+1]));
                            LOG.info("Location processed");
                        }
                    } catch (IOException e) {
                        LOG.error("Error while downloading resource", e);
                        return false;
                    }
                    break;
                case "-l":
                    LOG.info("Listing current location");
                    List<IndexEntry<Resource>> entries = listFolder.listFolder();
                    entries.forEach( indexEntry -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("uid: '");
                        sb.append(indexEntry.getElement().getId());
                        sb.append("', name: '");
                        sb.append(indexEntry.getElement().getName());
                        sb.append("', location: '");
                        sb.append(indexEntry.getElement().getLocation().getLocation());
                        sb.append("'");

                        LOG.info(sb.toString());
                        LOG.info("Location listed");
                    });
                    break;
                case "-i":
                    LOG.info("Indexing current location");
                    indexFolder.indexFolder(new File(args[i + 1]));
                    LOG.info("Current location indexed");
                    break;
            }

        }
        return true;
    }

    private boolean isUUID(String s){
        try {
            UUID.fromString(s);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

}