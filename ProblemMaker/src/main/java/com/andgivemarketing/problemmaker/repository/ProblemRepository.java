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
    // 1. 커스텀 생성자(플랫 생성자)를 만들어서 받는 방법(현재 사용 중이며, 제일 선호)
    // 2. DTO를 인터페이스로 만들어서 받는 방법
    // 3. recode 객체 만들어서 받는 방법
    // 4. DTO 내부에 객체를 안 쓰고 그냥 필드만 쭉 나열시켜는 방법
    @Query("""
        SELECT new com.andgivemarketing.problemmaker.dto.ProblemWithUnitDTO(
            p.id,
            p.title,
            p.answer,
            u.id,
            u.name,
            u.createAt
        )
        FROM ProblemEntity p
        JOIN p.unit u
        WHERE u.id = :unitId
    """)
    List<ProblemWithUnitDTO> findByUnitId(@Param("unitId") Long unitId);

}
