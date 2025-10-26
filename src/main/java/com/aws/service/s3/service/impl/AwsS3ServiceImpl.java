package com.aws.service.s3.service.impl;

import com.aws.service.s3.service.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class AwsS3ServiceImpl implements AwsS3Service {

    @Autowired
    private S3Client s3Client;

    @Override
    public Bucket createBucket(String bucketName) {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            CreateBucketResponse createBucketResponse = s3Client.createBucket(createBucketRequest);
            Bucket createdBucket = Bucket.builder()
                    .name(bucketName)
                    .bucketRegion(createBucketResponse.bucketArn())
                    .bucketArn(createBucketResponse.bucketArn())
                    .creationDate(Instant.now())
                    .build();
            log.info("Bucket created successfully: {}", createdBucket);
            return createdBucket;
        } catch (S3Exception e) {
            log.error("An unexpected error occurred while creation of bucket: {}", e.getMessage(), e);
            throw e;
        } catch (SdkClientException e) {
            log.error("An unexpected error occurred while creation of bucket: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Bucket> listBucket() {
        try {
            ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
                    .build();
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
            return listBucketsResponse.buckets();
        } catch (S3Exception e) {
            log.error("An unexpected error occurred while creation of bucket: {}", e.getMessage(), e);
            throw e;
        } catch (SdkClientException e) {
            log.error("An unexpected error occurred while creation of bucket: {}", e.getMessage(), e);
            throw e;
        }
    }
}
