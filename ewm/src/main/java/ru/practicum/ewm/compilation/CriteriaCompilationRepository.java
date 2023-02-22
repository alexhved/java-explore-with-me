package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CriteriaCompilationRepository {
    private final EntityManager entityManager;

    public CriteriaCompilationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Page<CompilationEntity> findWithParams(Boolean pinned, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompilationEntity> cq = cb.createQuery(CompilationEntity.class);
        Root<CompilationEntity> root = cq.from(CompilationEntity.class);
        root.fetch("events", JoinType.LEFT);

        Predicate predicate = buildPredicate(cb, root, pinned);
        cq.where(predicate);

        TypedQuery<CompilationEntity> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(from * size)
                .setMaxResults(size);

        Pageable pageable = PageRequest.of(from, size);
        Long count = findCount(cb, predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private Long findCount(CriteriaBuilder cb, Predicate predicate) {
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<CompilationEntity> countRoot = countQ.from(CompilationEntity.class);
        countQ.select(cb.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQ).getSingleResult();
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<CompilationEntity> root, Boolean pinned) {
        List<Predicate> predicateList = new ArrayList<>();


        if (pinned != null) {
            if (pinned) {
                predicateList.add(cb.isTrue(root.get("pinned")));
            } else {
                predicateList.add(cb.isFalse(root.get("pinned")));
            }
        }
        return cb.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
    }
}
