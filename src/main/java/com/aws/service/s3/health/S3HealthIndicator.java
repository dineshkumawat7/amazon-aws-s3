package com.aws.service.s3.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class S3HealthIndicator implements HealthIndicator {

    @Autowired
    private S3Client s3Client;

    @Override
    public Health health() {
        try {
            s3Client.listBuckets();
            return Health.up().withDetail("S3", "Connected successfully").build();
        } catch (Exception e) {
            return Health.down().withDetail("S3 Error", e.getMessage()).build();
        }
    }
}
