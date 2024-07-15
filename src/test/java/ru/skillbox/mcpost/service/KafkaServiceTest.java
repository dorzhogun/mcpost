package ru.skillbox.mcpost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.mcpost.event.NotificationEvent;
import ru.skillbox.mcpost.AbstractTest;

public class KafkaServiceTest extends AbstractTest {

    @Autowired
    protected KafkaTemplate<String, NotificationEvent> template;

    @Value("${app.kafka.notificationTopic}")
    protected String notificationTopic;

    @Container
    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"))
                    .withEmbeddedZookeeper();

    @DynamicPropertySource
    protected static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }
}


