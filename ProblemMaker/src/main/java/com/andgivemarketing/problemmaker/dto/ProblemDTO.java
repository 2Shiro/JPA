package com.andgivemarketing.problemmaker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProblemDTO {

    // Primitive type인 long을 사용하지 않고 wrapper type인 Long 쓰는 이유는 Null을 허용해주기 위함인데
    // Null을 허용해 준다는 것은 큰 장점이다.

    // 테이블 설계할 때 Null 허용을 설정할지 말지 결정하는데 DB에서 조회한 값을 Entity 객체에 맵핑을 시키는데
    // 객체의 필드 중에 원시타입을 사용한 필드에 Null값이 들어가게 되면 런타임 에러가 뜨면서 서버가 터진다.

    // 따라서, 안전하게 wrapper type을 사용해 주는 것이 좋다.
    private Long id;

    private Long unitId;

    private String title;

    private String answer;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


}
