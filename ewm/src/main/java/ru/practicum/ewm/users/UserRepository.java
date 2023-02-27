package ru.practicum.ewm.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select new java.lang.String(u.name) from UserEntity u where u.id =:userId")
    String findUserName(Long userId);

    @Query("select u from UserEntity u where u.id in :ids")
    Page<UserEntity> findByIdS(Long[] ids, Pageable pageable);

    @Query("select new ru.practicum.ewm.users.UserShortDto(u.id, u.name) from UserEntity u where u.id = :userId")
    Optional<UserShortDto> findByUserId(Long userId);

}
