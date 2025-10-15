package com.andgivemarketing.problemmaker.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProblemWithUnitDTO {

    private Long id;

    private UnitDTO unitDTO;

    private String title;

    private String answer;

    public ProblemWithUnitDTO(Long problemId, String title, String answer,
                              Long unitId, String name, LocalDateTime createAt) {
        this.id = problemId;
        this.title = title;
        this.answer = answer;
        this.unitDTO = new UnitDTO(unitId, name, createAt);
    }
}
