package com.andgivemarketing.problemmaker.controllers;

import com.andgivemarketing.problemmaker.dto.ErrorResponse;
import com.andgivemarketing.problemmaker.dto.ProblemDTO;
import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import com.andgivemarketing.problemmaker.entity.UnitEntity;
import com.andgivemarketing.problemmaker.service.ProblemService;
import com.andgivemarketing.problemmaker.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController {

    private final ProblemService problemService;
    private final UnitService unitService;

    // 문제 생성
    @PostMapping("/problem")
    public ResponseEntity<?> createProblem(@RequestBody(required = false) ProblemDTO problemDTO) {

        // 입력받은 문제 빈 값 체크
        if (problemDTO == null
                || problemDTO.getUnitId() == null
                || problemDTO.getTitle() == null  || problemDTO.getTitle().isBlank()
                || problemDTO.getAnswer() == null || problemDTO.getAnswer().isBlank()) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다.")));
        }

        Long unitId = problemDTO.getUnitId();

        UnitEntity unitEntity = unitService.findById(unitId);
        if (unitEntity == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("존재하지 않는 단원입니다")));
        }

        ProblemEntity problemEntity = new ProblemEntity();
        problemEntity.setUnitId(unitId);
        problemEntity.setTitle(problemDTO.getTitle());
        problemEntity.setAnswer(problemDTO.getAnswer());
        problemEntity.setCreateAt(LocalDateTime.now());

        problemService.save(problemEntity);

        return ResponseEntity.ok().build();
    }

    // 입력받은 문제 수정
    @PutMapping("/problem")
    public ResponseEntity<?> updateProblem(@RequestBody(required = false) ProblemDTO problemDTO) {

        // 입력받은 문제 빈 값 체크
        if (problemDTO == null
                || problemDTO.getId() == null
                || problemDTO.getUnitId() == null
                || problemDTO.getTitle() == null  || problemDTO.getTitle().isBlank()
                || problemDTO.getAnswer() == null || problemDTO.getAnswer().isBlank()) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다.")));
        }

        // 수정할 단원 조회
        ProblemEntity problemEntity = problemService.findById(problemDTO.getId());
        if (problemEntity == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("해당 문제를 찾을 수 없습니다")));
        }

        problemEntity = new ProblemEntity();
        problemEntity.setUnitId(problemDTO.getUnitId());
        problemEntity.setTitle(problemDTO.getTitle());
        problemEntity.setAnswer(problemDTO.getAnswer());
        problemEntity.setUpdateAt(LocalDateTime.now());

        problemService.save(problemEntity);

        return ResponseEntity.ok().build();
    }

    // 문제 삭제
    @DeleteMapping("/problem")
    public ResponseEntity<?> deleteProblem(@RequestParam(name = "id") Long id) {

        // 삭제할 문제 요청 id 확인
        if (id == null) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다")));
        }

        // 문제 존재 여부 확인
        if (problemService.findById(id) == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("존재하지 않는 문제입니다")));
        }

        problemService.delete(id);

        return ResponseEntity.ok().build();
    }

    // 랜덤 문제들 조회
    @PostMapping("/problems/random")
    public ResponseEntity<?> getRandomProblems(@RequestParam(name = "unitId") Long unitId, @RequestParam(name = "count") int count) {

        // count 파라미터 확인
        if (count <= 0) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("문제 수는 양수로 기입해주세요")));
        }

        // count 만큼 문제 조회
        List<ProblemDTO> list = problemService.findRandomProblems(unitId, count);

        return ResponseEntity.ok().body(list);
    }

}
