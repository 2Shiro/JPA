package com.andgivemarketing.problemmaker.dto;

import lombok.Data;

@Data
public class ProblemWithUnitDTO {

    private Long id;

    private UnitDTO unitDTO;

    private String title;

    private String answer;

}
