package ru.practicum.ewm.ratings;

import lombok.*;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.users.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "rates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RateEntity {
    public RateEntity(UserEntity initiator, EventEntity event, boolean isPositive) {
        this.initiator = initiator;
        this.event = event;
        this.isPositive = isPositive;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
    private UserEntity initiator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private EventEntity event;

    @Column(name = "is_positive", nullable = false)
    private boolean isPositive;

}
