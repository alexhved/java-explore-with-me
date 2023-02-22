package ru.practicum.ewm.ratings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.users.UserEntity;
import ru.practicum.ewm.users.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RatingProducer ratingProducer;

    public void addRate(Long userId, Long eventId, Boolean isPositive) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id: %s not found", userId)));
        EventEntity eventEntity = eventRepository.findByIdAndStatePUBLISHED(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Event with id: %s not found", eventId)));

        ratingRepository.save(new RateEntity(userEntity, eventEntity, isPositive));
    }

    public void deleteRate(Long userId, Long eventId) {
        ratingRepository.deleteByInitiator_IdAndEvent_Id(userId, eventId);
    }

    public UserRatingDto findRatingForAuthor(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(String.format("User with id: %s not found", userId));
        }
        String authorName = userRepository.findUserName(userId);
        Integer likes = ratingRepository.findLikesForAuthor(userId);
        Integer dislikes = ratingRepository.findDislikesForAuthor(userId);
        Integer rating = ratingProducer.calculateRating(likes, dislikes);
        return new UserRatingDto(authorName, rating);
    }
}
