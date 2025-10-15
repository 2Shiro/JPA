package com.andgivemarketing.problemmaker.repository;

import com.andgivemarketing.problemmaker.dto.ProblemWithUnitDTO;
import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity, Long> {

    // 단원 id로 문제 조회
    @Query("""
        SELECT new com.andgivemarketing.problemmaker.dto.ProblemWithUnitDTO(
            p.id, p.unit, p.title, p.answer
        )
        FROM ProblemEntity p
        WHERE p.unit.id = :unitId
    """)
    List<ProblemWithUnitDTO> findByUnitId(@Param("unitId") Long unitId);

}
