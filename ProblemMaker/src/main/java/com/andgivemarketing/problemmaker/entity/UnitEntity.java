package com.andgivemarketing.problemmaker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
// JPA 규약을 지키면서 외부에서 무분별한 인스턴스 생성 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "unit")
public class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    public void changeName(String name) {
        this.name = name;
    }

}
