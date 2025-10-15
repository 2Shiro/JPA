package com.andgivemarketing.problemmaker.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UnitDTO {

    private Long id;

    private String name;

    private LocalDateTime createAt;

}
