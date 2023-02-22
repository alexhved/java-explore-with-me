package ru.practicum.stats.repository;

import dto.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.entity.StatEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatEntity, Long> {

    @Query("select new dto.ViewStats(s.app, s.uri, count(s.uri))" +
            " from StatEntity s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri")
    List<ViewStats> findByTime(LocalDateTime startTime, LocalDateTime endTime, String[] uris);

    @Query("select new dto.ViewStats(s.app, s.uri, count(s.uri))" +
            " from StatEntity s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri")
    List<ViewStats> findByTimeUnique(LocalDateTime startTime, LocalDateTime endTime, String[] uris);

    @Query("select new dto.ViewStats(s.app, s.uri, count(s.uri))" +
            " from StatEntity s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri")
    List<ViewStats> findAllByTime(LocalDateTime startTime, LocalDateTime endTime);
}
