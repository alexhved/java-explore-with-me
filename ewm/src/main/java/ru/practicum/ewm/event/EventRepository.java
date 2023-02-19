package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


    @Query("select e from EventEntity e where e.id = :id and e.state = 'PUBLISHED'")
    Optional<EventEntity> findByIdAndStatePUBLISHED(Long id);

    Page<EventEntity> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<EventEntity> findFirstByInitiator_IdAndId(Long initiatorId, Long eventId);

    @Query("select e from EventEntity e " +
            "where e.initiator.id = :initiatorId and e.id = :eventId and (e.state ='PENDING' or e.state = 'CANCELED')")
    Optional<EventEntity> findFirstByInitiator_IdAndIdAndStatePendingOrStateCanceled(Long initiatorId, Long eventId);

    @Query("select e from EventEntity e " +
            "where e.id = :eventId and e.eventDate > :nowPlusOneHour")
    Optional<EventEntity> findByIdAndEventDateAfterOneHour(Long eventId, LocalDateTime nowPlusOneHour);

    @Query("select e.id, e.participantLimit from EventEntity e where e.id in :eventList and e.state = 'PENDING'")
    Map<Long, Integer> countParticipationEntitiesByEventListAndStatusPENDING(Long eventList);

    @Query("select e from EventEntity e where e.id in :eventIdList")
    List<EventEntity> findByEventIdList(List<Long> eventIdList);

    @Modifying
    @Query("update EventEntity e set e.views = (e.views + 1) where e.id in :eventIdList")
    void incrementViewsForEventIdList(List<Long> eventIdList);
}