package com.miquelmillan;

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

    private void parseParameters(String... args) {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-d":
                    try {
                        resourceComponent.storeLocation(new Location(args[i + 1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }


}