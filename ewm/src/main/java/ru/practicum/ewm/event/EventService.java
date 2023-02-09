package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.CategoryEntity;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.error.DateTimeException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.user.UserEntity;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.utils.Const;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventService {
    private final static DateTimeFormatter DTF = Const.DTF;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventFullDto addEntity(Long userId, NewEventDto newEventDto) {

        LocalDateTime eventDate = parseDAteTimeForEventDate(newEventDto.getEventDate());

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CategoryEntity categoryEntity = categoryRepository.findById(newEventDto.getCategory().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        EventEntity eventEntity = buildEntity(newEventDto, userEntity, categoryEntity, eventDate);
        eventRepository.save(eventEntity);

        return buildEventFullDto(eventEntity);
    }

    private EventFullDto buildEventFullDto(EventEntity eventEntity) {
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

    private LocalDateTime parseDAteTimeForEventDate(String timestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DateTimeException("Event date must be early 2 hours");
        }
        return localDateTime;
    }

    private EventEntity buildEntity(NewEventDto newEventDto, UserEntity userEntity,
                                    CategoryEntity categoryEntity, LocalDateTime eventDate) {

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
                .state(State.PENDING)
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .paid(newEventDto.isPaid())
                .build();
    }
}
