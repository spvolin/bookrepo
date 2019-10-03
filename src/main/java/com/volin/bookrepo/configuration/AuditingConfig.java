package com.volin.bookrepo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Файл конфигурации JpaAuditing
 *
 * @Configuration Клас-конфигурация.
 * @EnableJpaAuditing Возможность отслеживать изменения подклассов класса DateAudit  
 */

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
}
