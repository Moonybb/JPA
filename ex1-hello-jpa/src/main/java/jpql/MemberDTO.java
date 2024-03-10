package jpql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public @Getter @Setter class MemberDTO {

    private String username;
    private int age;
}
