package com.fluffy.SharingCalendar.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Repository {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public URL uploadFile(ObjectMetadata objectMetadata, InputStream inputStream, String keyName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, keyName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, keyName);
    }

    public void deleteFile(String fileName) {
        boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, fileName);

        if (!isObjectExist) {
            log.debug("Fail Delete Failed _ file not found");
        }

        amazonS3Client.deleteObject(bucket, fileName);
    }
}
