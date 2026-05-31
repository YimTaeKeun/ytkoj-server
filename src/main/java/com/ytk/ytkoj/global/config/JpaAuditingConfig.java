package com.ytk.ytkoj.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// japa auditing 기능을 활성화합니다.
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
