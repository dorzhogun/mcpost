package ru.skillbox.mcpost;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.mcpost.config.security.TokenFilter;
import ru.skillbox.mcpost.dto.JwtRq;
import ru.skillbox.mcpost.feign.PostFeignClient;

import java.util.Map;

import static org.mockito.Mockito.when;


@Testcontainers
@AutoConfigureMockMvc
@Sql("classpath:db/init.sql")
@SpringBootTest(
        value = {"eureka.client.enabled:false", "spring.liquibase.enabled:false"}
)
public class AbstractTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected TokenFilter filter;
    @Autowired
    protected ObjectMapper objectMapper;

    @Container
    protected static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:12.3"));

    @Container
    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"))
                    .withEmbeddedZookeeper();

    @DynamicPropertySource
    protected static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @BeforeEach
    public void setUp() {
        PostFeignClient feignClient = Mockito.mock(PostFeignClient.class);
        when(feignClient.validateToken(Mockito.any())).thenAnswer(invocation -> {
            JwtRq request = invocation.getArgument(0);
            return Map.of("principal", request.getToken(), "authorities", "USER");
        });
        filter.setFeignClient(feignClient);

    }

}
