package com.aws.service.s3.model.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonSuccessResponse <T>{
    private String timestamp;
    private String status;
    private int statusCode;
    private String message;
    private T payload;
}
