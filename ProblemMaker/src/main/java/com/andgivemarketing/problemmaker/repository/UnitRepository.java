package com.andgivemarketing.problemmaker.repository;


import com.andgivemarketing.problemmaker.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Long> {

    // 이름으로 단원 조회
    Optional<UnitEntity> findByName(String name);

}