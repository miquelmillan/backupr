package com.miquelmillan.context.infrastructure.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.domain.resource.ResourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Repository
@Qualifier("s3ResourceRepository")
public class S3ResourceRepository implements ResourceRepository {
    private AmazonS3 s3client;
    private String bucketName;

    @Autowired
    public S3ResourceRepository(@Value("${aws.credentials.bucketname}") String bucketName) {
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("eu-north-1")
                .build();

        this.bucketName = bucketName;
    }

    @Override
    public ResourceResult store(Resource item) throws IOException, ResourceRepositoryException {
        ResourceResult result = new ResourceResult();
        HashMap<String, Resource> resources = new HashMap<>();
        Resource res = new Resource(item.getName(), item.getLocation());

        if (item != null){
            HashMap<String, Object> props = new HashMap<>();

            PutObjectResult response = this.s3client.putObject(this.bucketName,
                                    item.getLocation().getLocation(),
                                    new File(item.getLocation().getLocation()));

            if (response == null){
                throw new ResourceRepositoryException();
            }

            props.put(Resource.Properties.MD5.toString(), response.getContentMd5());
            res.setProperties(props);
            resources.put(res.getName(), res);

            result.setResources(resources);
        }

        return result;
    }

    @Override
    public ResourceResult query(String path) throws IOException, AmazonServiceException {
        ResourceResult result = new ResourceResult();
        HashMap<String, Resource> resources = new HashMap<>();

        S3Object o = this.s3client.getObject(this.bucketName, path);

        try (S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(path))) {
            byte[] read_buf = new byte[1024];
            int read_len;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
        }

        resources.put(path, new Resource(path, new Location(path)));
        result.setResources(resources);

        return result;
    }
}
