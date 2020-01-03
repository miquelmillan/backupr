package com.miquelmillan;

import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.*;
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
    @Qualifier("resourceComponent")
    private ResourceComponent resourceComponent;


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
                e.printStackTrace();
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
                            resourceComponent.outboundResource(UUID.fromString(args[i+1]));
                            LOG.info("Resource processed");
                        } else {
                            LOG.info("Processing location: {}", args[i+1]);
                            resourceComponent.outboundLocation(new Location(args[i+1]));
                            LOG.info("Location processed");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                         return false;
                    }
                    break;
                case "-d":
                    try {
                        if (isUUID(args[i+1])) {
                            LOG.info("Processing resource: {}", args[i+1]);
                            resourceComponent.inboundResource(UUID.fromString(args[i+1]));
                            LOG.info("Resource processed");
                        } else {
                            LOG.info("Processing location: {}", args[i+1]);
                            resourceComponent.inboundLocation(new Location(args[i+1]));
                            LOG.info("Location processed");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                case "-l":
                    LOG.info("Listing current location");
                    List<IndexEntry<Resource>> entries = resourceComponent.listLocation();
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
                    resourceComponent.indexLocation(new Location(args[i + 1]));
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