package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.id in :ids")
    Page<UserEntity> findByIdS(Long[] ids, Pageable pageable);

    @Query("select new ru.practicum.ewm.user.UserShort(u.id, u.name) from UserEntity u")
    Optional<UserShort> findByUserId(Long userId);

}
