package hellojpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
@NoArgsConstructor
@AllArgsConstructor
public @Getter
@Setter class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "memberId")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "ADRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Adress> adressHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();



}
