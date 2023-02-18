package ru.practicum.ewm.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.dto.EventIdVsConfirmedParticipants;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    Optional<ParticipationEntity> findFirstByRequester_IdAndEvent_Id(Long requester, Long event);

    @Query("select e.id, count(p.id) from ParticipationEntity p " +
            "join EventEntity e " +
            "on p.event.id = e.id " +
            "where p.status = 'CONFIRMED' and e.id in :eventIdList " +
            "group by e.id")
    Map<Long, Long> eventId_vs_ConfirmedParticipants(List<Long> eventIdList);

    @Query("select new ru.practicum.ewm.event.dto.EventIdVsConfirmedParticipants(e.id, count(p.id)) " +
            "from ParticipationEntity p " +
            "join EventEntity e " +
            "on p.event.id = e.id " +
            "where p.status = 'CONFIRMED' and e.id in :eventIdList " +
            "group by e.id " +
            "order by e.id asc ")
    List<EventIdVsConfirmedParticipants> eventIds_vs_ConfirmedParticipants(List<Long> eventIdList);

    @Query("select e.event.id from ParticipationEntity e " +
            "where e.event.participantLimit > " +
            "(select count(p.id) from ParticipationEntity p where p.status = 'CONFIRMED' and p.id = e.event.id)")
    List<Long> findAvailableEventIds();

    Integer countParticipationEntitiesByEventIdAndStatus(Long eventId, Status status);

    @Query("select p from ParticipationEntity p " +
            "where p.event.initiator.id = :initiatorId and p.event.id = :eventId")
    List<ParticipationEntity> findAllParticipationRequestsForEventInitiator(Long initiatorId, Long eventId);

    List<ParticipationEntity> findAllByRequester_Id(Long requesterId);

    List<ParticipationEntity> findAllByEvent_Initiator_IdAndEvent_IdAndStatus(Long initiatorId, Long eventId, Status status);

    @Query("select p from ParticipationEntity p " +
            "where p.event.initiator.id = :userId " +
            "and p.event.id = :eventId and p.id in :requestIds " +
            "and p.status = 'PENDING'")
    Optional<ParticipationEntity> findAllByUserAndEventAndRequestsAndStatusPENDING(Long userId, Long eventId, List<Long> requestIds);

    @Query("select p from ParticipationEntity p where p.id in :requestIds")
    List<ParticipationEntity> findAllByRequestIds(List<Long> requestIds);

    Optional<ParticipationEntity> findFirstByRequester_IdAndId(Long requesterId, Long requestId);

}
