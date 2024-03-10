package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public @Getter @Setter class Address {
    private String city;
    private String street;
    private String zipCode;
}
