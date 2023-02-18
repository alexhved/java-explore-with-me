package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.dto.AdminSearchParamsDto;
import ru.practicum.ewm.event.dto.EventSearchParamsDto;
import ru.practicum.ewm.participation.ParticipationRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CriteriaEventRepository {

    private final EntityManager entityManager;
    private final ParticipationRepository participationRepository;


    public CriteriaEventRepository(EntityManager entityManager, ParticipationRepository participationRepository) {
        this.entityManager = entityManager;
        this.participationRepository = participationRepository;
    }

    public Page<EventEntity> searchWithParams(AdminSearchParamsDto adminSearchParamsDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = cq.from(EventEntity.class);

        Predicate predicate = buildPredicate(cb, root, adminSearchParamsDto);
        cq.where(predicate);

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(adminSearchParamsDto.getFrom() * adminSearchParamsDto.getSize())
                .setMaxResults(adminSearchParamsDto.getSize());

        Pageable pageable = PageRequest.of(adminSearchParamsDto.getFrom(), adminSearchParamsDto.getSize());
        Long countEntities = findCount(cb, predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, countEntities);
    }

    public Page<EventEntity> searchWithParams(EventSearchParamsDto paramsDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = cq.from(EventEntity.class);

        Predicate predicate = buildPredicate(cb, root, paramsDto);
        cq.where(predicate);

        if (paramsDto.getSort() != null) {
            if (paramsDto.getSort().equals(SortingState.EVENT_DATE)) {
                cq.orderBy(cb.asc(root.get("eventDate")));
            } else {
                cq.orderBy(cb.asc(root.get("views")));
            }
        }

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(paramsDto.getFrom() * paramsDto.getSize())
                .setMaxResults(paramsDto.getSize());

        Pageable pageable = PageRequest.of(paramsDto.getFrom(), paramsDto.getSize());
        Long count = findCount(cb, predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private Long findCount(CriteriaBuilder cb, Predicate predicate) {
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<EventEntity> countRoot = countQ.from(EventEntity.class);
        countQ.select(cb.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQ).getSingleResult();
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<EventEntity> root, EventSearchParamsDto paramsDto) {
        List<Predicate> predicateList = new ArrayList<>();
        if (paramsDto.getText() != null) {
            Predicate predicateAnnotation = cb.like(root.get("annotation"), "%" + paramsDto.getText() + "%");
            Predicate predicateDescription = cb.like(root.get("description"), "%" + paramsDto.getText() + "%");
            predicateList.add(cb.or(predicateAnnotation, predicateDescription));
        }
        if (paramsDto.getCategories() != null) {
            predicateList.add(root.get("category").in(paramsDto.getCategories()));
        }
        if (paramsDto.getPaid() != null) {
            if (paramsDto.getPaid()) {
                predicateList.add(cb.isTrue(root.get("paid")));
            } else {
                predicateList.add(cb.isFalse(root.get("paid")));
            }
        }
        if (paramsDto.getRangeStart() != null) {
            predicateList.add(cb.greaterThan(root.get("eventDate"), paramsDto.getRangeStart()));
        }
        if (paramsDto.getRangeEnd() != null) {
            predicateList.add(cb.lessThan(root.get("eventDate"), paramsDto.getRangeEnd()));
        }
        if (paramsDto.getOnlyAvailable()) {
            List<Long> availableEventIds = participationRepository.findAvailableEventIds();
            predicateList.add(root.get("id").in(availableEventIds));
        }

        return cb.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<EventEntity> root, AdminSearchParamsDto adminSearchParamsDto) {
        List<Predicate> predicateList = new ArrayList<>();

        if (adminSearchParamsDto.getUsers() != null) {
            predicateList.add(root.get("initiator").in(adminSearchParamsDto.getUsers()));
        }
        if (adminSearchParamsDto.getCategories() != null) {
            predicateList.add(root.get("category").in(adminSearchParamsDto.getCategories()));
        }
        if (adminSearchParamsDto.getStates() != null) {
            predicateList.add(root.get("state").in(adminSearchParamsDto.getStates()));
        }
        if (adminSearchParamsDto.getRangeStart() != null) {
            predicateList.add(cb.greaterThan(root.get("eventDate"), adminSearchParamsDto.getRangeStart()));
        }
        if (adminSearchParamsDto.getRangeEnd() != null) {
            predicateList.add(cb.lessThan(root.get("eventDate"), adminSearchParamsDto.getRangeEnd()));
        }
        return cb.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
    }
}
