package jpql;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Embeddable
public class Address {

    private String city;

    private String street;

    private String zipCode;
}
