package com.ytk.ytkoj.global.base;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * isDelete 칼럼은 넣지 않습니다. 해당 프로젝트는 hard delete으로 운영됩니다.
 * */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @ColumnDefault("'2025-01-01 00:00:00'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @ColumnDefault("'2025-01-01 00:00:00'")
    private LocalDateTime updatedAt;
}
