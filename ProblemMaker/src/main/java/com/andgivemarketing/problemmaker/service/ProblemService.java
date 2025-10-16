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
    public void save(ProblemEntity problemEntity) {
        problemRepository.save(problemEntity);
    }

    // 문제 삭제
    public void delete(Long id) {
        problemRepository.deleteById(id);
    }

    // 문제 id로 문제 조회
    public ProblemEntity findById(Long id) {
        return problemRepository.findById(id).orElse(null);
    }

    // 랜덤 문제들 조회
    public List<ProblemDTO> findRandomProblems(Long unitId, int count) {

        // DTO는 클라이언트와 주고 받는 객체 (외부와 소통)
        // Entity는 DB 조회를 위한 객체 (내부에서만)

        // 출력해야 하는 데이터는 Entity에서 Dto로 변환해야 하는 이유
        // 1. Entity는 DB와 대응되기 때문에 Entity를 그대로 반환하면 DB 내부 구조가 노출된다. ★
        // 2. 우리가 반환할 때 Entity의 모든값이 다 필요 없을 수도 있다. -> 필요한 값만 DTO에 담아서 반환한다. ★
        // 3. JPA Entity의 지연로딩을 통한 N+1 문제를 해결하기 위해 DTO로 변환해서 반환 ★★★★★
        // JPA의 Entity는 OneToMany, OneToOne 같은 연관 관계 설정이 가능하다.
        // JPA에서 DB 조회를 할때 즉시 로딩과 지연 로딩을 설정할 수 있다.
        //  즉시 로딩 : 연관 관계에 있는 모든 Entity를 함께 조회한다.
        //      user{userId:"aa123", name: "홍길동", userImage: {imageId:"bb22", url:"/user/image/aa.jpg"}}
        //  지연 로딩 : 최초에는 연관 관계에 있는 Entity를 조회하지 않고, 필요할 때 추가 조회를 자동으로 진행한다.
        //      user{userId:"aa123", name: "홍길동", userImage: null}

        // ResponseEntity를 통해 클라이언트단(프론트)으로 데이터를 반환하는데 표준 양식은 JSON이다.
        // 내부적으로 Entity 객체를 자동으로 JSON 형태로 변형시켜주는데 이 과정을 직렬화라고 부른다.
        // 이 직렬화는 내부적으로 @Getter를 사용 (getId(), getName(), getAge(), getImage() ...)
        // 이 직렬화 과정에서 지연 로딩을 통해 아직 조회되지 않은 연관 관계에 있는 Entity를 get하게 되는 순간
        // (Entity를 get하게 되는 순간) 아직 조회되지 않은 연관 관계의 Entity가 필요한 순간이 되버린다.
        // 즉, 조회하지 않았던 연관 관계에 있는 Entity가 JPA 내부적으로 필요하게 되어 추가 쿼리 조회가 발생한다.
        // 이게 바로 JPA의 N+1 문제다. (Table 단위)

        // 자 이 문제를 해결하기 위해서는 Entity를 DTO로 변환해서 반환하면 직렬화를 거치더라도 추가 쿼리가 발생 안한다!!

        // 1. repositroy에서 조회를 할때 DTO로 받는다. (JPQL을 쓸줄 알아야 함)
        //     - JPQL을 사용해야 하지만, 변환할 필요가 없음
        // 2. 외부 라이브러리를 사용한다.(기능이 한정적이여서 비선호)
        //     - 기능이 한정적이고, 마찬가지로 setter를 남발
        // 3. toDto 커스텀 메서드를 사용한다.
        //     - setter를 남발하여 코드가 더럽고 길다.

        // 단원 id에 해당하는 문제들 조회
        List<ProblemDTO> problemDTOS = findByUnitId(unitId).stream().map(this::parseDTO).collect(Collectors.toList());

        Collections.shuffle(problemDTOS);

        problemDTOS = problemDTOS.stream().limit(count).toList();

        return problemDTOS;
    }

    // 단원 id로 문제들 조회
    public List<ProblemEntity> findByUnitId(Long unitId) {
        return problemRepository.findByUnitId(unitId);
    }

    public ProblemDTO parseDTO(ProblemEntity problemEntity) {
        ProblemDTO problemDTO = new ProblemDTO();
        problemDTO.setId(problemEntity.getId());
        problemDTO.setTitle(problemEntity.getTitle());
        problemDTO.setAnswer(problemEntity.getAnswer());
        problemDTO.setUnitId(problemEntity.getUnitId());
        problemDTO.setCreateAt(problemEntity.getCreateAt());
        problemDTO.setUpdateAt(problemEntity.getUpdateAt());
        return problemDTO;
    }


}
