package ru.skillbox.mcpost.dto.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.mcpost.model.Post;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class PostSearchCriteria {
    @JsonProperty("ids")
    private String ids;
    @JsonProperty("accountIds")
    private String accountIds;
    @JsonProperty("authorIds")
    private List<UUID> authorIds;
    @JsonProperty("isBlocked")
    private Boolean isBlocked;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("withFriends")
    private Boolean withFriends;
    @JsonProperty("title")
    private String title;
    @JsonProperty("text")
    private String text;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("dateFrom")
    private Instant dateFrom;
    @JsonProperty("dateTo")
    private Instant dateTo;

    public Specification<Post> getSpecification() {
        return getSpecification(this);
    }

    private Specification<Post> getSpecification(PostSearchCriteria criteria) {
        Map<String, Object> fields = new HashMap<>();
        for (Field field : PostSearchCriteria.class.getDeclaredFields()) {
            try {
                fields.put(field.getName(), field.get(criteria));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access field: " + field.getName(), e);
            }
        }
        return Specification.allOf(
                fields.keySet().stream()
                        .map(fieldName -> PostSearchCriteria
                                .getSpecification(fieldName, fields, authorIds))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private static Specification<Post> getSpecification(
            String fieldName, Map<String, Object> fields, List<UUID> authorIds
    ) {
        Object fieldValue = fields.get(fieldName);
        if (fieldValue == null
                || fieldName.equals("withFriends")
                || fieldName.equals("showFriends")) {
            return null;
        }
        return switch (fieldName) {
            case "ids", "accountIds" -> Specification.anyOf(
                    Arrays.stream(((String) fieldValue).split(","))
                            .filter(str -> !str.isBlank())
                            .map(PostSearchCriteria::getSpecification)
                            .toList()
            );
            case "authorIds" -> Specification.anyOf(
                    authorIds
                            .stream()
                            .map(UUID::toString)
                            .map(PostSearchCriteria::getSpecification)
                            .toList()
            );
            case "text" -> (root, query, cb) -> cb.like(root.get("postText"), "%" + fieldValue + "%");
            case "dateTo" -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get("timeChanged"), (Instant) fieldValue);
            case "dateFrom" ->
                    (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timeChanged"), (Instant) fieldValue);
            default -> (root, query, cb) -> cb.equal(root.get(fieldName), fieldValue);
        };
    }

    private static Specification<Post> getSpecification(String id) {
        if (id.length() > 10) {
            return (root, query, cb) -> cb.equal(root.get("authorId"), UUID.fromString(id));
        }
        return (root, query, cb) -> cb.equal(root.get("id"), Long.parseLong(id));
    }
}
