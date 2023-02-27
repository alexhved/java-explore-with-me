package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.DateTimeConst;
import ru.practicum.ewm.category.CategoryEntity;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.ratings.RatingDto;
import ru.practicum.ewm.users.UserEntity;
import ru.practicum.ewm.users.UserMapper;
import ru.practicum.ewm.users.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventMapper {
    private static final DateTimeFormatter DTF = DateTimeConst.DTF;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventShortDto buildEventShortDto(EventEntity eventEntity, UserShortDto userShortDto,
                                            Integer confirmedReq, Long views) {
        return EventShortDto.builder()
                .id(eventEntity.getId())
                .annotation(eventEntity.getAnnotation())
                .category(categoryMapper.toDto(eventEntity.getCategory()))
                .confirmedRequests(confirmedReq)
                .eventDate(eventEntity.getEventDate().format(DTF))
                .initiator(userShortDto)
                .paid(eventEntity.isPaid())
                .title(eventEntity.getTitle())
                .views(views)
                .build();
    }

    public EventShortDto buildEventShortDto(EventEntity eventEntity, Integer confirmedReq) {
        return EventShortDto.builder()
                .id(eventEntity.getId())
                .annotation(eventEntity.getAnnotation())
                .category(categoryMapper.toDto(eventEntity.getCategory()))
                .confirmedRequests(confirmedReq)
                .eventDate(eventEntity.getEventDate().format(DTF))
                .initiator(userMapper.toUserShortDto(eventEntity.getInitiator()))
                .paid(eventEntity.isPaid())
                .title(eventEntity.getTitle())
                .views(eventEntity.getViews())
                .build();
    }

    public EventShortDto buildEventShortDto(EventEntity eventEntity, Integer confirmedReq, RatingDto ratingDto) {
        return EventShortDto.builder()
                .id(eventEntity.getId())
                .annotation(eventEntity.getAnnotation())
                .category(categoryMapper.toDto(eventEntity.getCategory()))
                .confirmedRequests(confirmedReq)
                .eventDate(eventEntity.getEventDate().format(DTF))
                .initiator(userMapper.toUserShortDto(eventEntity.getInitiator()))
                .paid(eventEntity.isPaid())
                .title(eventEntity.getTitle())
                .views(eventEntity.getViews())
                .rating(ratingDto)
                .build();
    }

    public EventFullDto buildEventFullDto(EventEntity eventEntity) {
        return EventFullDto.builder()
                .id(eventEntity.getId())
                .category(categoryMapper.toDto(eventEntity.getCategory()))
                .initiator(userMapper.toUserShortDto(eventEntity.getInitiator()))
                .location(new Location(eventEntity.getLatitude(), eventEntity.getLongitude()))
                .eventDate(eventEntity.getEventDate().format(DTF))
                .annotation(eventEntity.getAnnotation())
                .requestModeration(eventEntity.isRequestModeration())
                .description(eventEntity.getDescription())
                .state(eventEntity.getState())
                .title(eventEntity.getTitle())
                .paid(eventEntity.isPaid())
                .createdOn(eventEntity.getCreatedOn().format(DTF))
                .participantLimit(eventEntity.getParticipantLimit())
                .build();
    }

    public EventFullDto buildEventFullDto(EventEntity eventEntity, RatingDto ratingDto) {
        return EventFullDto.builder()
                .id(eventEntity.getId())
                .category(categoryMapper.toDto(eventEntity.getCategory()))
                .initiator(userMapper.toUserShortDto(eventEntity.getInitiator()))
                .location(new Location(eventEntity.getLatitude(), eventEntity.getLongitude()))
                .eventDate(eventEntity.getEventDate().format(DTF))
                .annotation(eventEntity.getAnnotation())
                .requestModeration(eventEntity.isRequestModeration())
                .description(eventEntity.getDescription())
                .state(eventEntity.getState())
                .title(eventEntity.getTitle())
                .paid(eventEntity.isPaid())
                .createdOn(eventEntity.getCreatedOn().format(DTF))
                .participantLimit(eventEntity.getParticipantLimit())
                .rating(ratingDto)
                .build();
    }

    public EventEntity buildEntity(NewEventDto newEventDto, UserEntity userEntity,
                                   CategoryEntity categoryEntity, LocalDateTime eventDate, State state) {

        return EventEntity.builder()
                .title(newEventDto.getTitle())
                .initiator(userEntity)
                .annotation(newEventDto.getAnnotation())
                .category(categoryEntity)
                .description(newEventDto.getDescription())
                .eventDate(eventDate)
                .createdOn(LocalDateTime.now())
                .latitude(newEventDto.getLocation().getLat())
                .longitude(newEventDto.getLocation().getLon())
                .state(state)
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .paid(newEventDto.isPaid())
                .build();
    }

    public EventEntity updateEventEntity(UpdateEventUserRequest updateEventUserRequest,
                                         CategoryEntity category, EventEntity eventEntity,
                                         State state, LocalDateTime updatedEventDate) {
        float lat = 0f;
        float lon = 0f;

        if (updateEventUserRequest.getLocation() != null) {
            lat = updateEventUserRequest.getLocation().getLat();
            lon = updateEventUserRequest.getLocation().getLon();
        }

        return EventEntity.builder()
                .id(eventEntity.getId())
                .title(updateEventUserRequest.getTitle() != null ?
                        updateEventUserRequest.getTitle() : eventEntity.getTitle())
                .initiator(eventEntity.getInitiator())
                .annotation(updateEventUserRequest.getAnnotation() != null ?
                        updateEventUserRequest.getAnnotation() : eventEntity.getAnnotation())
                .category(category != null ? category : eventEntity.getCategory())
                .description(updateEventUserRequest.getDescription() != null ?
                        updateEventUserRequest.getDescription() : eventEntity.getDescription())
                .eventDate(updatedEventDate)
                .createdOn(eventEntity.getCreatedOn())
                .latitude(lat != 0f ? lat : eventEntity.getLatitude())
                .longitude(lon != 0f ? lon : eventEntity.getLongitude())
                .state(state)
                .publishedOn(state.equals(State.PUBLISHED) ? LocalDateTime.now() : null)
                .participantLimit(updateEventUserRequest.getParticipantLimit() != null ?
                        updateEventUserRequest.getParticipantLimit() : eventEntity.getParticipantLimit())
                .requestModeration(updateEventUserRequest.getRequestModeration() != null ?
                        updateEventUserRequest.getRequestModeration() : eventEntity.isRequestModeration())
                .paid(updateEventUserRequest.getPaid() != null ?
                        updateEventUserRequest.getPaid() : eventEntity.isPaid())
                .build();
    }
}
