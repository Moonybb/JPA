package hellojpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public @Getter @Setter class Member {
    @Id @GeneratedValue
    @Column(name = "memberId")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

}
