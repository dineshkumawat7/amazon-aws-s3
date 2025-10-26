package com.aws.service.s3.controller;

import com.aws.service.s3.exception.ServiceException;
import com.aws.service.s3.model.common.CommonSuccessResponse;
import com.aws.service.s3.service.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/aws/s3")
public class AwsS3Controller {

    @Autowired
    private HealthEndpoint healthEndpoint;

    @Autowired
    private AwsS3Service awsS3Service;

    @GetMapping("/health")
    public ResponseEntity<CommonSuccessResponse<Object>> getHealth() {
        String status = healthEndpoint.health().getStatus().getCode();
        String message = switch (status) {
            case "UP" -> "Service is up and running. All systems are healthy.";
            case "DOWN" -> "Service is currently down. Please contact support.";
            case "OUT_OF_SERVICE" -> "Service is out of service. Maintenance may be in progress.";
            case "UNKNOWN" -> "Service health is unknown. Please try again later.";
            default -> "Service status: " + status;
        };
        return getSpecificResponse(message, HttpStatus.OK.value(), healthEndpoint.health());
    }

    @PostMapping(value = "/bucket/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonSuccessResponse<Object>> createBucket(@RequestParam(name = "bucket-name", required = true) String bucketName) {
        log.info("Incoming request for bucket creation: {}", bucketName);
        Bucket createdBucket = awsS3Service.createBucket(bucketName);
        return getSpecificResponse(String.format("Bucket created successfully: %s", bucketName), HttpStatus.CREATED.value(), createdBucket);
    }

    @GetMapping(value = "/bucket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonSuccessResponse<Object>> listBucket() {
        log.info("Incoming request for list bucket");
        List<Bucket> buckets = awsS3Service.listBucket();
        return getSpecificResponse("Bucket fetched successfully", HttpStatus.OK.value(), buckets);
    }

    /**
     * Constructs a standardized API response for successful operations.
     * <p>
     * Builds a CommonSuccessResponse object with timestamp, status, status code, message, and payload.
     * </p>
     *
     * @param msg        User-friendly message to include in the response
     * @param statusCode HTTP status code for the response
     * @param payload    Data payload to include in the response
     * @param <T>        Type of the payload
     * @return ResponseEntity containing the CommonSuccessResponse
     * @throws ServiceException if an error occurs while building the response
     */
    private <T> ResponseEntity<CommonSuccessResponse<T>> getSpecificResponse(String msg, int statusCode, T payload) {
        try {
            CommonSuccessResponse<T> response = CommonSuccessResponse.<T>builder()
                    .timestamp((Instant.now().toString()))
                    .status("Success")
                    .statusCode(statusCode)
                    .message(msg)
                    .payload(payload)
                    .build();
            return ResponseEntity.status(statusCode).body(response);
        } catch (Exception e) {
            throw new ServiceException("Something wrong on server.", e);
        }
    }
}
