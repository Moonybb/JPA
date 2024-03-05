package hellojpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
@NoArgsConstructor
@AllArgsConstructor
public @Getter @Setter class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "memberId")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;

}
