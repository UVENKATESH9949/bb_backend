// src/main/java/com/BrainBlitz/repository/QuestionGroupRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.QuestionGroup;
import com.BrainBlitz.enums.GroupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {

    Page<QuestionGroup> findByGroupType(GroupType groupType, Pageable pageable);
}