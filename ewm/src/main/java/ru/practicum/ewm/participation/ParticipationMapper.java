package ru.practicum.ewm.participation;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.DateTimeConst;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.users.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ParticipationMapper {
    private static final DateTimeFormatter DTF = DateTimeConst.DTF;

    public ParticipationEntity buildEntity(UserEntity userEntity, EventEntity eventEntity, Status status) {
        return ParticipationEntity.builder()
                .requester(userEntity)
                .event(eventEntity)
                .status(status)
                .created(LocalDateTime.now())
                .build();
    }

    public ParticipationRequestDto buildParticipationRequestDto(ParticipationEntity participationEntity, Long userId) {
        return ParticipationRequestDto.builder()
                .id(participationEntity.getId())
                .requester(userId)
                .status(participationEntity.getStatus().name())
                .created(participationEntity.getCreated().format(DTF))
                .event(participationEntity.getEvent().getId())
                .build();
    }

    public ParticipationRequestDto buildParticipationRequestDto(ParticipationEntity participationEntity) {
        return ParticipationRequestDto.builder()
                .id(participationEntity.getId())
                .requester(participationEntity.getRequester().getId())
                .status(participationEntity.getStatus().name())
                .created(participationEntity.getCreated().format(DTF))
                .event(participationEntity.getEvent().getId())
                .build();
    }
}
