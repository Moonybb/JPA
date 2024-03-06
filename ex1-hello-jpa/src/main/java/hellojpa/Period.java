package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public @Getter @Setter class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

//    public boolean isWork() {
//
//    }
}
