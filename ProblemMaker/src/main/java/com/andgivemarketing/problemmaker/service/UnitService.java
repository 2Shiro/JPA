package com.andgivemarketing.problemmaker.service;

import com.andgivemarketing.problemmaker.entity.UnitEntity;
import com.andgivemarketing.problemmaker.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    // 단원 생성 또는 수정
    public void save(UnitEntity unitEntity){
        unitRepository.save(unitEntity);
    }

    // 단원 삭제
    public void delete(Long id){
        unitRepository.deleteById(id);
    }

    // 단원 id로 단원 조회
    public UnitEntity findById(Long id){
        return unitRepository.findById(id).orElse(null);
    }

    // 단원 이름으로 단원 조회
    public UnitEntity findByName(String name){
        return unitRepository.findByName(name).orElse(null);
    }

}
