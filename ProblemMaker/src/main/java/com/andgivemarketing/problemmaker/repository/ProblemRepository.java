package com.andgivemarketing.problemmaker.repository;

import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity, Long> {

    // 단원 id로 문제 조회
    // 1. 커스텀 생성자(플랫 생성자)를 만들어서 받는 방법
    // 2. DTO를 인터페이스로 만들어서 받는 방법
    // 3. recode 객체 만들어서 받는 방법
    // 4. DTO 내부에 객체를 안 쓰고 그냥 필드만 쭉 나열시켜는 방법
    List<ProblemEntity> findByUnitId(Long unitId);

}
