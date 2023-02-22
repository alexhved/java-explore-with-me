package ru.practicum.ewm.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.users.UserEntity;
import ru.practicum.ewm.users.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationMapper participationMapper;

    public ParticipationRequestDto addParticipation(Long userId, Long eventId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Optional<ParticipationEntity> optionalParticipationEntity = participationRepository.findFirstByRequester_IdAndEvent_Id(userId, eventId);
        if (optionalParticipationEntity.isPresent()) {
            throw new ResourceAccessException("Resource already exist");
        }

        if (eventEntity.getInitiator().getId() == userId) {
            throw new ResourceAccessException(String.format("No access for user id: %s", userId));
        }
        if (eventEntity.getState().equals(State.PENDING)) {
            throw new ResourceAccessException("No access for unpublished event");
        }

        Integer participationCount = participationRepository.countParticipationEntitiesByEventIdAndStatus(eventId, Status.CONFIRMED);
        if (participationCount >= eventEntity.getParticipantLimit()) {
            throw new ResourceAccessException("No access because participation limit has been reached");
        }

        Status participationStatus;
        if (!eventEntity.isRequestModeration()) {
            participationStatus = Status.CONFIRMED;
        } else {
            participationStatus = Status.PENDING;
        }

        ParticipationEntity participationEntity = participationMapper.buildEntity(userEntity, eventEntity, participationStatus);
        participationRepository.save(participationEntity);

        return participationMapper.buildParticipationRequestDto(participationEntity, userId);
    }

    public List<ParticipationRequestDto> findByUserId(Long userId) {
        List<ParticipationEntity> participationEntityList = participationRepository.findAllByRequester_Id(userId);
        return participationEntityList.stream()
                .map(entity -> participationMapper.buildParticipationRequestDto(entity, userId))
                .collect(Collectors.toList());
    }

    public List<ParticipationRequestDto> findRequestsOnParticipationForUser(Long userId, Long eventId) {
        List<ParticipationEntity> participationEntities = participationRepository.findAllParticipationRequestsForEventInitiator(userId, eventId);

        return participationEntities.stream()
                .map(participationMapper::buildParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult changeStatusForParticipation(Long userId,
                                                                       Long eventId,
                                                                       EventRequestStatusUpdateRequest statusUpdateRequest) {
        List<Long> requestIds = statusUpdateRequest.getRequestIds();
        String status = statusUpdateRequest.getStatus();
        List<ParticipationEntity> participationEntityList = participationRepository.findAllByRequestIds(requestIds);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        EventEntity eventEntity = eventRepository.findFirstByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Event with id: %s not found", eventId)));
        int participantLimit = eventEntity.getParticipantLimit();
        Integer confirmedParticipants = participationRepository.countParticipationEntitiesByEventIdAndStatus(eventId, Status.CONFIRMED);

        if (Status.CONFIRMED.name().equals(status)) {
            for (ParticipationEntity entity : participationEntityList) {
                if (participantLimit > confirmedParticipants && entity.getStatus().equals(Status.PENDING)) {
                    entity.setStatus(Status.CONFIRMED);
                    ParticipationRequestDto participationRequestDto =
                            participationMapper.buildParticipationRequestDto(entity, entity.getRequester().getId());
                    confirmedRequests.add(participationRequestDto);
                } else {
                    participationEntityList.forEach(participationEntity -> {
                        if (participationEntity.getStatus().equals(Status.PENDING)) {
                            participationEntity.setStatus(Status.REJECTED);
                            ParticipationRequestDto participationRequestDto =
                                    participationMapper.buildParticipationRequestDto(entity, entity.getRequester().getId());
                            rejectedRequests.add(participationRequestDto);
                        }
                    });
                    throw new ResourceAccessException(String.format("Participation limit has been reached for event with id: %s", eventId));
                }
                participantLimit--;
            }
        } else if (Status.REJECTED.name().equals(status)) {
            for (ParticipationEntity entity : participationEntityList) {
                if (entity.getStatus().equals(Status.PENDING)) {
                    entity.setStatus(Status.REJECTED);
                    ParticipationRequestDto participationRequestDto =
                            participationMapper.buildParticipationRequestDto(entity, entity.getRequester().getId());
                    rejectedRequests.add(participationRequestDto);
                } else {
                    throw new ResourceAccessException(" No access for entity with status: " + entity.getStatus());
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("Status: %s not supported", status));
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    public ParticipationRequestDto cancelParticipation(Long userId, Long requestId) {
        ParticipationEntity participationEntity = participationRepository.findFirstByRequester_IdAndId(userId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Participation with id: %s not found", requestId)));
        participationEntity.setStatus(Status.CANCELED);
        participationRepository.save(participationEntity);

        return participationMapper.buildParticipationRequestDto(participationEntity, userId);
    }
}
