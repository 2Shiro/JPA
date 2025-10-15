package com.andgivemarketing.problemmaker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "problem")
public class ProblemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "answer", length = 256, nullable = false)
    private String answer;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
