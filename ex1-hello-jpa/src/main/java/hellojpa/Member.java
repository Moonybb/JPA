package hellojpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // 기간 Period
    @Embedded
    private Period workPeriod;
//    private LocalDateTime startDate;
//    private LocalDateTime endDate;

    // 주소 Adress
    @Embedded
    private Adress homeAdress;
//    private String city;
//    private String street;
//    private String zipCode;

}
