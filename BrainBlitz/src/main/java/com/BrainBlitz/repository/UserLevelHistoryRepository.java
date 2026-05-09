package com.BrainBlitz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.UserLevelHistory;
import com.BrainBlitz.enums.LevelChangeReason;

@Repository
public interface UserLevelHistoryRepository 
    extends JpaRepository<UserLevelHistory, Long> {

    // Fetch full level history for a user
    // Ordered by latest first
    List<UserLevelHistory> findByUserIdOrderByChangedAtDesc(Long userId);

    // Fetch last N level changes for a user
    // Used for level journey graph on profile page
    List<UserLevelHistory> findTop10ByUserIdOrderByChangedAtDesc(
        Long userId);

    // Fetch level changes by reason
    // e.g. how many times did user lose level due to inactivity
    List<UserLevelHistory> findByUserIdAndReason(
        Long userId, LevelChangeReason reason);

    // Fetch last level change for a user
    // Used to check what caused most recent level change
    Optional<UserLevelHistory> findTopByUserIdOrderByChangedAtDesc(
        Long userId);

    @Query("SELECT COUNT(u) FROM UserLevelHistory u WHERE u.user.id = :userId AND u.newLevel > u.previousLevel")
    int countLevelUpsForUser(@Param("userId") Long userId);

    // Fetch all level changes caused by inactivity
    // Used for analytics — how many users losing level due to inactivity
    List<UserLevelHistory> findByReason(LevelChangeReason reason);
}
