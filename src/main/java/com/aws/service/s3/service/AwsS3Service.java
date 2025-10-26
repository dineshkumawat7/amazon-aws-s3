package com.aws.service.s3.service;

import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;

public interface AwsS3Service {
    Bucket createBucket(String bucketName);
    List<Bucket> listBucket();
}
