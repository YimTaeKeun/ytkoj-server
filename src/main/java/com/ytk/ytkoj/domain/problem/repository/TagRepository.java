package com.ytk.ytkoj.domain.problem.repository;

import com.ytk.ytkoj.domain.problem.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
}
