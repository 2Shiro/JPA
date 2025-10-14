package com.andgivemarketing.problemmaker.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponse {


    final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private ErrorDetails error;

    public ErrorResponse(ErrorDetails error) {
        this.error = error;
    }

    @Data
    public static class ErrorDetails {

        private String requestAt;
        private String message;


        public ErrorDetails(String message) {
            this.requestAt = FORMATTER.format(LocalDateTime.now());
            this.message = message;
        }
    }
}
