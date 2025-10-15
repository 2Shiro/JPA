package com.andgivemarketing.problemmaker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UnitDTO {

    private Long id;

    private String name;

    private LocalDateTime createAt;

}
