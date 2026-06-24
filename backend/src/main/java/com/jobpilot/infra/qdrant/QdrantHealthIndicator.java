package com.jobpilot.infra.qdrant;

import com.jobpilot.config.properties.QdrantProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class QdrantHealthIndicator implements HealthIndicator {
    private final QdrantProperties properties;
    private final RestClient restClient;

    public QdrantHealthIndicator(QdrantProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public Health health() {
        try {
            String response = restClient.get()
                    .uri(properties.getHost() + "/readyz")
                    .retrieve()
                    .body(String.class);
            return Health.up()
                    .withDetail("host", properties.getHost())
                    .withDetail("collection", properties.getCollection())
                    .withDetail("ready", response)
                    .build();
        } catch (Exception exception) {
            return Health.down(exception)
                    .withDetail("host", properties.getHost())
                    .withDetail("collection", properties.getCollection())
                    .build();
        }
    }
}
