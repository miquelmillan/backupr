package com.miquelmillan.backupr.adapter.requester;

import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
import com.miquelmillan.backupr.uc.port.requester.ResourceRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LocalResourceRequester implements ResourceRequester {
    private ResourceRepository localRepository;
    private ResourceRepository remoteRepository;

    @Autowired
    public LocalResourceRequester(
            @Qualifier("fsResourceRepository") ResourceRepository localRepository,
            @Qualifier("s3ResourceRepository") ResourceRepository remoteRepository
    ){
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public Resource requestOutputResource(Resource item) throws IOException {
        return this.localRepository.query(item);
    }

    public Resource requestInputResource(Resource item) throws IOException {
        return this.remoteRepository.query(item);
    }

}
