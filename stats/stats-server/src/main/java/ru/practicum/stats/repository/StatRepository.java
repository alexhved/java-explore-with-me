package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.entity.StatEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatEntity, Long> {

    @Query("select s from StatEntity s where s.timestamp between ?1 and ?2 and s.uri in ?3")
    List<StatEntity> findByTime(LocalDateTime startTime, LocalDateTime endTime, String[] uris);

    @Query("select distinct s from StatEntity s where s.timestamp between ?1 and ?2 and s.uri in ?3")
    List<StatEntity> findByTimeUnique(LocalDateTime startTime, LocalDateTime endTime, String[] uris);
}
