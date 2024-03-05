package hellojpa;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
public @Getter @Setter abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createDate;
    private String updatedBy;
    private LocalDateTime updateDate;

}
