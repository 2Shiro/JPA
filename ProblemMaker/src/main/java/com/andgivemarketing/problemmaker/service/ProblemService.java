package com.andgivemarketing.problemmaker.service;

import com.andgivemarketing.problemmaker.dto.ProblemDTO;
import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import com.andgivemarketing.problemmaker.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    // 문제 생성 또는 수정
    public void save(ProblemEntity problemEntity){
        problemRepository.save(problemEntity);
    }

    // 문제 삭제
    public void delete(Long id){
        problemRepository.deleteById(id);
    }

    // 문제 id로 문제 조회
    public ProblemEntity findById(Long id) {
        return problemRepository.findById(id).orElse(null);
    }

    // 랜덤 문제들 조회
    public List<ProblemDTO> findRandomProblems(Long unitId, int count) {

        // 단원 id에 해당하는 문제들 조회
        List<ProblemEntity> problemEntities = findByUnitId(unitId);

        // 랜덤 순서
        Collections.shuffle(problemEntities);
        
        // 랜덤 순서로 된 리스트에서 원하는 만큼 잘라내기
        // count가 조회된 문제 수보다 높으면 안되기 때문에 Math.min 사용
        problemEntities = problemEntities.subList(0, Math.min(count, problemEntities.size()));

        // Entity를 Dto로 변환
        //
        return problemEntities.stream()
                // Entity를 Dto로 변환하는 메서드 호출
                .map(this::toDto)
                // 변환된 요소들을 List<ProblemDTO>로 모아서 반환
                .collect(Collectors.toList());
    }

    // 단원 id로 문제들 조회
    public List<ProblemEntity> findByUnitId(Long unitId) {
        return problemRepository.findByUnitId(unitId);
    }

    // Entity를 Dto로 변환
    private ProblemDTO toDto(ProblemEntity problemEntity) {

        ProblemDTO problemDTO = new ProblemDTO();;
        problemDTO.setUnitId(problemEntity.getUnitId());
        problemDTO.setTitle(problemEntity.getTitle());
        problemDTO.setAnswer(problemEntity.getAnswer());

        return problemDTO;
    }

}
