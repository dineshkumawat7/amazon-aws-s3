package com.aws.service.s3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AwsS3Config {

    @Autowired
    private EnvironmentConfig env;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(env.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(env.getAccessKey(), env.getSecretKey())
                        )
                )
                // LocalStack endpoint
                .endpointOverride(URI.create(env.getEndpoint()))
                // Force path-style access (required for LocalStack)
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }
}
