package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.ewm.category.CategoryEntity;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.error.DateTimeException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.participation.ParticipationRepository;
import ru.practicum.ewm.ratings.EventIdVsLikes;
import ru.practicum.ewm.ratings.RatingDto;
import ru.practicum.ewm.ratings.RatingProducer;
import ru.practicum.ewm.ratings.RatingRepository;
import ru.practicum.ewm.users.UserEntity;
import ru.practicum.ewm.users.UserRepository;
import ru.practicum.ewm.users.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final CriteriaEventRepository criteriaEventRepository;
    private final RatingRepository ratingRepository;
    private final EventMapper eventMapper;
    private final RatingProducer ratingProducer;

    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {

        LocalDateTime eventDate = parseDateTimeForEventDate(newEventDto.getEventDate());

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", userId)));
        CategoryEntity categoryEntity = categoryRepository.findById(newEventDto.getCategory().longValue())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Category with id %s not found", newEventDto.getCategory())));

        State state = State.PENDING;


        EventEntity eventEntity = eventMapper.buildEntity(newEventDto, userEntity, categoryEntity, eventDate, state);
        eventRepository.save(eventEntity);

        return eventMapper.buildEventFullDto(eventEntity);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {

        EventEntity eventEntity = eventRepository.findFirstByInitiator_IdAndIdAndStatePendingOrStateCanceled(userId, eventId)
                .orElseThrow(() -> new ResourceAccessException(String
                        .format("Event with user id: %s and event id: %s not found", userId, eventId)));

        Integer categoryId = updateEventUserRequest.getCategory();
        CategoryEntity categoryEntity = null;
        if (categoryId != null) {
            categoryEntity = categoryRepository.findById(categoryId.longValue())
                    .orElseThrow(() -> new ResourceAccessException(String.format("Category with id: %s not found", categoryId)));
        }
        State state = null;
        if (updateEventUserRequest.getStateAction() != null) {
            if (StateAction.CANCEL_REVIEW.name().equals(updateEventUserRequest.getStateAction())) {
                state = State.CANCELED;
            } else if (StateAction.SEND_TO_REVIEW.name().equals(updateEventUserRequest.getStateAction())) {
                state = State.PENDING;
            } else {
                throw new IllegalArgumentException("Wrong state action");
            }
        }
        String strUpdatedEventDate = updateEventUserRequest.getEventDate();
        LocalDateTime updatedEventDate;

        if (strUpdatedEventDate != null) {
            updatedEventDate = parseDateTimeForEventDate(strUpdatedEventDate);
        } else {
            updatedEventDate = eventEntity.getEventDate();
        }
        EventEntity updatedEventEntity =
                eventMapper.updateEventEntity(updateEventUserRequest, categoryEntity, eventEntity, state, updatedEventDate);
        eventRepository.save(updatedEventEntity);

        return eventMapper.buildEventFullDto(updatedEventEntity);
    }

    public EventFullDto findOneByUserAndEvent(Long userId, Long eventId) {
        EventEntity eventEntity = eventRepository.findFirstByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Event with user id: %s and event id: %s not found", userId, eventId)));

        return eventMapper.buildEventFullDto(eventEntity);
    }

    public List<EventShortDto> findEventsByUser(Long userId, Integer from, Integer size) {
        UserShortDto userShortDto = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found", userId)));

        Page<EventEntity> eventEntityPage = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size));

        List<Long> eventIdList = eventEntityPage.getContent().stream()
                .map(EventEntity::getId)
                .collect(Collectors.toList());

        List<EventIdVsConfirmedParticipants> confirmedListMap = participationRepository.eventIds_vs_ConfirmedParticipants(eventIdList);
        Map<Long, Long> confirmedMap = new HashMap<>();
        confirmedListMap.forEach(value -> confirmedMap.put(value.getEventId(), value.getConfParticipantCount()));

        return eventEntityPage.getContent().stream()
                .map(eventEntity -> {
                    Integer confirmedReq = confirmedMap.getOrDefault(eventEntity.getId(), 0L).intValue();
                    return eventMapper.buildEventShortDto(eventEntity, userShortDto, confirmedReq, eventEntity.getViews());
                })
                .collect(Collectors.toList());
    }


    public EventFullDto moderateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        EventEntity eventEntity = eventRepository.findByIdAndEventDateAfterOneHour(eventId, LocalDateTime.now().plusHours(1))
                .orElseThrow(() -> new ResourceAccessException(String
                        .format("Event with id: %s no access, is too late", eventId)));

        State state = null;
        String strStateAction = updateEventAdminRequest.getStateAction();
        if (strStateAction != null) {
            if (eventEntity.getState().equals(State.PENDING)) {
                if (StateAction.PUBLISH_EVENT.name().equals(strStateAction)) {
                    eventEntity.setPublishedOn(LocalDateTime.now());
                    state = State.PUBLISHED;
                } else if (StateAction.REJECT_EVENT.name().equals(strStateAction)) {
                    state = State.CANCELED;
                } else {
                    throw new IllegalArgumentException("Illegal state action: " + strStateAction);
                }
            } else {
                throw new ResourceAccessException("No access for events with state: " + eventEntity.getState());
            }
        }

        Integer categoryId = updateEventAdminRequest.getCategory();
        CategoryEntity categoryEntity = null;
        if (categoryId != null) {
            categoryEntity = categoryRepository.findById(categoryId.longValue())
                    .orElseThrow(() -> new ResourceAccessException(String.format("Category with id: %s not found", categoryId)));
        }

        String strUpdatedEventDate = updateEventAdminRequest.getEventDate();
        LocalDateTime updatedEventDate;
        if (strUpdatedEventDate != null) {
            updatedEventDate = parseDateTimeForModerateEventDate(strUpdatedEventDate);
        } else {
            updatedEventDate = eventEntity.getEventDate();
        }

        EventEntity updatedEventEntity =
                eventMapper.updateEventEntity(updateEventAdminRequest, categoryEntity, eventEntity, state, updatedEventDate);
        eventRepository.save(updatedEventEntity);
        return eventMapper.buildEventFullDto(eventEntity);
    }

    @Transactional
    public List<EventFullDto> findBySearchParamsForAdmin(AdminSearchParamsDto adminSearchParamsDto) {
        Page<EventEntity> page = criteriaEventRepository.searchWithParams(adminSearchParamsDto);
        return page.getContent().stream()
                .map(eventMapper::buildEventFullDto)
                .collect(Collectors.toList());
    }

    public List<EventShortDto> findBySearchParamsForPublic(EventSearchParamsDto eventSearchParamsDto) {
        Page<EventEntity> page = criteriaEventRepository.searchWithParams(eventSearchParamsDto);

        List<Long> eventIdList = page.getContent().stream()
                .map(EventEntity::getId)
                .collect(Collectors.toList());

        List<EventIdVsConfirmedParticipants> confirmedListMap = participationRepository.eventIds_vs_ConfirmedParticipants(eventIdList);
        Map<Long, Long> confirmedMap = new HashMap<>();
        confirmedListMap.forEach(value -> confirmedMap.put(value.getEventId(), value.getConfParticipantCount()));

        page.getContent().forEach(this::incrementViews);

        List<EventEntity> eventEntities = eventRepository.saveAllAndFlush(page.getContent());

        Map<Long, Long> eventIdVsLikesMap = ratingRepository.findLikesByEventIdList(eventIdList).stream()
                .collect(Collectors.toMap(EventIdVsLikes::getEventId, EventIdVsLikes::getLikes));

        Map<Long, Long> eventIdDislikesMap = ratingRepository.findDislikesByEventIdList(eventIdList).stream()
                .collect(Collectors.toMap(EventIdVsLikes::getEventId, EventIdVsLikes::getLikes));

        List<EventShortDto> eventShortDtoList = eventEntities.stream()
                .map(eventEntity -> {
                    int confirmedReq = confirmedMap.getOrDefault(eventEntity.getId(), 0L).intValue();
                    int likes = eventIdVsLikesMap.getOrDefault(eventEntity.getId(), 0L).intValue();
                    int dislikes = eventIdDislikesMap.getOrDefault(eventEntity.getId(), 0L).intValue();
                    RatingDto ratingDto = ratingProducer.calculateDto(likes, dislikes);
                    return eventMapper.buildEventShortDto(eventEntity, confirmedReq, ratingDto);
                })
                .collect(Collectors.toList());

        if (eventSearchParamsDto.getSort() != null && eventSearchParamsDto.getSort().equals(SortingState.LIKES)) {
            eventShortDtoList.sort((o1, o2) -> o2.getRating().getRating() - o1.getRating().getRating());
        }

        return eventShortDtoList;
    }

    public EventFullDto findByIdAndStatePUBLISHED(Long id) {
        EventEntity eventEntity = eventRepository.findByIdAndStatePUBLISHED(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Event with id: %s not found", id)));

        Integer likes = ratingRepository.countLikesByEventId(id);
        Integer dislikes = ratingRepository.countDislikesByEventId(id);
        RatingDto ratingDto = ratingProducer.calculateDto(likes, dislikes);

        incrementViews(eventEntity);

        return eventMapper.buildEventFullDto(eventEntity, ratingDto);
    }

    private void incrementViews(EventEntity eventEntity) {
        long views = eventEntity.getViews();
        eventEntity.setViews(++views);
    }

    private LocalDateTime parseDateTimeForEventDate(String timestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateTimeException("Event date must be early 2 hours");
        }
        return localDateTime;
    }

    private LocalDateTime parseDateTimeForModerateEventDate(String timestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new DateTimeException("Event date must be early 1 hours");
        }
        return localDateTime;
    }
}
