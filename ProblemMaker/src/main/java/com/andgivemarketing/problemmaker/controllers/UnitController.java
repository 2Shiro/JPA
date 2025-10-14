package com.andgivemarketing.problemmaker.controllers;

import com.andgivemarketing.problemmaker.dto.ErrorResponse;
import com.andgivemarketing.problemmaker.dto.UnitDTO;
import com.andgivemarketing.problemmaker.entity.UnitEntity;
import com.andgivemarketing.problemmaker.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UnitController {

    private final UnitService unitService;

    //정말 단순하게 가져올떄
    //파라미터로 page 번호 정도만 받는다면 get 이 괜찮음.

    //필터 같은게 들어간다면은, get에다가 body를 담는건 이상함.
    //필터가 많은 경우에는 그렇다고 다 get으로 하고 파라미터로 넣기에는 너무 주소가 길어지고 가독성도 떨어짐.
    //dto 로 받는거랑 하나하나 파라미터 로 받는거랑도 코드에 차이가 있음.

    //이 경우에는 post로.

    //클라이언트(사용자)의 요청 오류 nll 등은 400
    //요청은 문제 없으나 중복 등 문제, 거의 대부분의 모든 문제 404
    //런타임 오류나 예상치 못한 오류 500
    //인증 권한 문제는 407

    // 단원 생성
    @PostMapping("/unit")
    public ResponseEntity<?> createUnit(@RequestBody(required = false)UnitDTO unitDTO) {

        // 입력받은 단원 빈 값 체크
        if (unitDTO == null
                || unitDTO.getName() == null || unitDTO.getName().isBlank()){
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 이름입니다")));
        }
        String name = unitDTO.getName();

        // 이름 중복 체크
        UnitEntity unitEntity = unitService.findByName(name);

        if (unitEntity != null){
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("이미 존재하는 이름입니다")));
        }

        unitEntity = new UnitEntity();
        unitEntity.setName(unitDTO.getName());
        unitEntity.setCreateAt(LocalDateTime.now());

        unitService.save(unitEntity);

        return ResponseEntity.ok().build();
    }

    // 단원 수정
    @PutMapping("/unit")
    public ResponseEntity<?> updateUnitName(@RequestBody(required = false)UnitDTO unitDTO) {

        // 입력받은 단원 빈 값 체크
        if (unitDTO == null
                || unitDTO.getId() == null
                || unitDTO.getName() == null || unitDTO.getName().isBlank()){
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다")));
        }

        Long id = unitDTO.getId();
        String name = unitDTO.getName();

        // 수정할 단원 조회
        UnitEntity unitEntity = unitService.findById(id);
        if (unitEntity == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("해당 단원을 찾을 수 없습니다")));
        }

        // 이름 중복 체크
        UnitEntity duplicateCheckEntity = unitService.findByName(name);
        if (duplicateCheckEntity != null && !duplicateCheckEntity.getId().equals(id)){ // 본인 id에 해당하는 단원과 같은 이름으로 입력할 시 허용
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("이미 존재하는 이름입니다")));
        }

        unitEntity.setName(name);
        unitService.save(unitEntity);

        return ResponseEntity.ok().build();
    }

    // 단원 삭제
    @DeleteMapping("/unit")
    public ResponseEntity<?> deleteUnit(@RequestParam(name = "id") Long id) {

        if (id == null) {
            return ResponseEntity.status(400).body(new ErrorResponse(new ErrorResponse.ErrorDetails("올바르지 않은 요청입니다")));
        }

        // 문제 존재 여부 확인
        if (unitService.findById(id) == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(new ErrorResponse.ErrorDetails("존재하지 않는 단원입니다")));
        }

        unitService.delete(id);

        return ResponseEntity.ok().build();
    }


}
