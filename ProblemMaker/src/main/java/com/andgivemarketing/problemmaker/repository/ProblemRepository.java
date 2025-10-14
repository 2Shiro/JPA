package com.andgivemarketing.problemmaker.repository;


import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity, Long> {

    // 단원 id로 문제 조회
    List<ProblemEntity> findByUnitId(Long unitId);

}
