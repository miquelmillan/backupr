package com.miquelmillan.context.infrastructure.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public void store(Resource item) throws IOException, ResourceRepositoryException {

        // Check if item is properly set
        if (item != null){
            // open an output stream
            HashMap<String, Object> props = new HashMap<>();


            //write the outputstream with the info coming from the item.content.stream
            PutObjectResult response =
                    this.s3client.putObject(    this.bucketName,
                                                item.getLocation().getLocation(),
                                                item.getContents().getInputStream(),
                                                new ObjectMetadata());
            if (response == null){
                throw new ResourceRepositoryException();
            }

            props.put(Resource.Properties.MD5.toString(), response.getContentMd5());
            item.setProperties(props);
        }
    }

    @Override
    public Resource query(String path) throws IOException, AmazonServiceException {
        Resource result;
        S3Object o = this.s3client.getObject(this.bucketName, path);

        try (S3ObjectInputStream s3is = o.getObjectContent();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] read_buf = new byte[1024];
            int read_len;

            while ((read_len = s3is.read(read_buf)) > 0) {
                buffer.write(read_buf, 0, read_len);
            }

            result = new Resource(path,
                                new Location(path),
                                new Contents(new ByteArrayInputStream(buffer.toByteArray())));

        }


        return result;
    }
}
