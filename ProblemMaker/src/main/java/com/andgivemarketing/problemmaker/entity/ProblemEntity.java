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
@Table(name = "problem")
public class ProblemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 외래키 매핑: DB 컬럼명 unit_id 는 그대로 사용
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitEntity unit;

    @Column(name = "title", length = 256, nullable = false)
    private String title;

    @Column(name = "answer", length = 256, nullable = false)
    private String answer;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    // 수정할 때 영속성 컨텍스트로 인해
    // 기존 영속 엔티티를 수정하는 메서드 사용
    public void changeUnit(UnitEntity unit) {
        this.unit = unit;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeAnswer(String answer) {
        this.answer = answer;
    }

    public void touchUpdatedAt(LocalDateTime updatedAt) {
        this.updateAt = updatedAt;
    }

}
