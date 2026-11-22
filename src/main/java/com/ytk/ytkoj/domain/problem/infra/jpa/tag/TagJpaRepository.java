package com.ytk.ytkoj.domain.problem.infra.jpa.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
}
