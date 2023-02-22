package ru.practicum.ewm.ratings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<RateEntity, Long> {

    @Query("select count(r.initiator.id) from RateEntity r where r.event.initiator.id = :authorId and r.isPositive = true ")
    Integer findLikesForAuthor(Long authorId);

    @Query("select count(r.initiator.id) from RateEntity r where r.event.initiator.id = :authorId and r.isPositive = false ")
    Integer findDislikesForAuthor(Long authorId);

    @Query("select new ru.practicum.ewm.ratings.EventIdVsLikes(r.event.id, count(r.initiator.id)) " +
            "from RateEntity r " +
            "where r.event.id in :eventIdList and r.isPositive = true " +
            "group by r.event.id")
    List<EventIdVsLikes> findLikesByEventIdList(List<Long> eventIdList);

    @Query("select new ru.practicum.ewm.ratings.EventIdVsLikes(r.event.id, count(r.initiator.id)) " +
            "from RateEntity r " +
            "where r.event.id in :eventIdList and r.isPositive = false " +
            "group by r.event.id")
    List<EventIdVsLikes> findDislikesByEventIdList(List<Long> eventIdList);

    @Query("select count(r.initiator.id) from RateEntity r where r.event.id =:eventId and r.isPositive = true")
    Integer countLikesByEventId(Long eventId);

    @Query("select count(r.initiator.id) from RateEntity r where r.event.id =:eventId and r.isPositive = false")
    Integer countDislikesByEventId(Long eventId);

    void deleteByInitiator_IdAndEvent_Id(Long initiatorId, Long eventId);

}
