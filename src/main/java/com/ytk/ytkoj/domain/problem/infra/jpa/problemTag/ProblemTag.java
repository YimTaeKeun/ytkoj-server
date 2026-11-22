package com.ytk.ytkoj.domain.problem.infra.jpa.problemTag;

import com.ytk.ytkoj.domain.problem.infra.jpa.tag.Tag;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProblemTag extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private ProblemJpaEntity problemJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ProblemTag(ProblemJpaEntity problemJpaEntity, Tag tag){
        this.problemJpaEntity = problemJpaEntity;
        this.tag = tag;
    }
}
