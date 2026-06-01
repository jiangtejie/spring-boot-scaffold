package com.example.scaffold.infrastructure.health;

import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 数据库健康检查指示器。
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (var conn = dataSource.getConnection()) {
            if (conn.isValid(3)) {
                return Health.up()
                        .withDetail("database", conn.getMetaData().getDatabaseProductName())
                        .build();
            }
            return Health.down().withDetail("database", "Connection invalid").build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
