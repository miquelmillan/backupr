package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Qualifier("resourceComponent")
public class ResourceComponent {
    @Autowired
    private ResourceRequester requester;
    @Autowired
    private ResourceProcessor processor;

    public ResourceComponent(ResourceRequester requester, ResourceProcessor processor) {
        this.requester = requester;
        this.processor = processor;
    }

    public void storeLocation(Location location) throws IOException {
        ResourceResult resources = this.requester.listLocation(location);
        resources.getResources().entrySet().stream().map(resource -> {
            try {
                return this.storeResult(resource.getValue());
            } catch ( IOException | ResourceRepositoryException e ) {
                return new ResourceResult();
            }
        }).collect(Collectors.toList());

        /*
            TODO
            1.- Return Resources properly processed
            2.- Identify the resources wrongly processed and be able to return them
         */
    }

    public ResourceResult storeResult(Resource resource) throws IOException, ResourceRepositoryException {
        return this.processor.storeResource(resource);
    }
}
