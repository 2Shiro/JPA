package com.andgivemarketing.problemmaker.controllers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.andgivemarketing.problemmaker.dto.ErrorResponse;
import com.andgivemarketing.problemmaker.dto.ProblemDTO;
import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import com.andgivemarketing.problemmaker.service.ProblemService;
import com.andgivemarketing.problemmaker.service.UnitService;
import com.andgivemarketing.problemmaker.utils.GsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController {

    private final ProblemService problemService;
    private final UnitService unitService;

    // 문제 생성
    @PostMapping("/problem/create")
    public ResponseEntity<?> createProblem(@RequestBody(required = false) ProblemDTO problemDTO) {

        // 입력받은 문제 빈 값 체크
        if (problemDTO == null
                || problemDTO.getUnitId() == null
                || problemDTO.getTitle() == null
                || problemDTO.getTitle().isBlank()
                || problemDTO.getAnswer() == null || problemDTO.getAnswer().isBlank()) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다.")));
        }

        ProblemEntity problemEntity = ProblemEntity.builder()
                .unitId(problemDTO.getUnitId())
                .title(problemDTO.getTitle())
                .answer(problemDTO.getAnswer())
                .createAt(LocalDateTime.now())
                .build();

        problemService.save(problemEntity);

        return ResponseEntity.ok().build();
    }

    // 입력받은 문제 수정
    @PutMapping("/problem/update")
    public ResponseEntity<?> updateProblem(@RequestBody(required = false) ProblemDTO problemDTO) {

        // 입력받은 문제 빈 값 체크
        if (problemDTO == null
                || problemDTO.getId() == null
                || problemDTO.getUnitId() == null
                || problemDTO.getTitle() == null  || problemDTO.getTitle().isBlank()
                || problemDTO.getAnswer() == null || problemDTO.getAnswer().isBlank()) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다.")));
        }

        // 수정할 문제 조회
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
    @DeleteMapping("/problem/delete")
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
    @GetMapping("/problems/random")
    public ResponseEntity<?> getRandomProblems(@RequestParam(name = "unitId", required = false) Long unitId
            , @RequestParam(name = "count", required = false) int count
            , @RequestParam(name = "format", required = false) String format) throws Exception {

        // count 파라미터 확인
        if (count <= 0) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("문제 수는 양수로 기입해주세요")));
        }

        // count 만큼 문제 조회
        List<ProblemDTO> randomProblems = problemService.findRandomProblems(unitId, count);

        // 엑셀 요청 처리
        // 1. Excel 파일 생성은 Workbook → Sheet → Row → Cell 형태로 채운다.
        // 2. 파일을 HTTP로 돌려줄 때는 Content-Type + Content-Disposition 헤더가 필요하다.
        // 3. 큰 데이터엔 메모리 고려(SXSSFWorkbook)가 필요하다.
        if ("excel".equalsIgnoreCase(format)) {
            // 워크북을 바이트 배열로 만들어 ResponseEntity로 반환 (스프링이 스트림 처리)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Problems");

                // 헤더 스타일
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // 헤더 행 생성
                Row headerRow = sheet.createRow(0);
                String[] headers = {"문제 아이디", "문제", "문제 정답"};
                for (int i = 0; i < headers.length; i++) {
                    Cell c = headerRow.createCell(i);
                    c.setCellValue(headers[i]);
                    c.setCellStyle(headerStyle);
                }

                // 데이터 채우기
                int rowIdx = 1;
                for (ProblemDTO dto : randomProblems) {
                    Row row = sheet.createRow(rowIdx++);
                    if (dto.getId() != null) row.createCell(0).setCellValue(dto.getId());
                    else row.createCell(0).setCellValue("");
                    row.createCell(1).setCellValue(dto.getTitle() == null ? "" : dto.getTitle());
                    row.createCell(2).setCellValue(dto.getAnswer() == null ? "" : dto.getAnswer());
                }

                // 열 너비 자동조정
                for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

                workbook.write(baos);
            }

            byte[] excelBytes = baos.toByteArray();
            String filename = "problems_unit_" + (unitId == null ? "all" : unitId) + ".xlsx";
            String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelBytes.length)
                    .body(excelBytes);
        }

        return ResponseEntity.ok().body(GsonUtils.gson.toJson(randomProblems));
    }

}
