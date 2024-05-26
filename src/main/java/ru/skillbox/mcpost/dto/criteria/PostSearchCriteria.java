package ru.skillbox.mcpost.dto.criteria;

import ru.skillbox.mcpost.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
public class PostSearchCriteria {
    private String ids;
    private String accountIds;
    private String blockedIds;
    private Boolean isDeleted;
    private Boolean withFriends;
    private String author;
    private String title;
    private String postText;
    private String tags;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    public Specification<Post> getSpecification() {
        return getSpecification(this);
    }

    private Specification<Post> getSpecification(PostSearchCriteria criteria) {
        Map<String, Object> fields = new HashMap<>();
        Arrays.stream(PostSearchCriteria.class.getDeclaredFields()).forEach(field -> {
            try {
                fields.put(field.getName(), field.get(criteria));
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return Specification.allOf(
                fields.keySet().stream()
                        .map(fieldName -> PostSearchCriteria.getSpecification(fieldName, fields))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private static Specification<Post> getSpecification(String fieldName, Map<String, Object> fields)
    {
        Object fieldValue = fields.get(fieldName);
        if (fieldValue == null || fieldName.equals("withFriends") ||
                fieldName.equals("showFriends") || fieldName.equals("author")) {
            return null;
        }
        if (fieldName.toLowerCase().contains("ids")) {
            return Specification.anyOf(
                    Arrays.stream(((String) fieldValue).split(","))
                            .filter(str -> !str.isBlank())
                            .map(PostSearchCriteria::getSpecification)
                            .toList()
            );
        }
        return switch (fieldName) {
            case "postText" -> (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    root.get("postText"), "%" + fieldValue + "%");
            case "dateTo" -> (root, query, cb) -> cb.lessThanOrEqualTo(
                    root.get("publishDate"), (LocalDateTime) fieldValue);
            case "dateFrom" -> (root, query, cb) -> cb.greaterThanOrEqualTo(
                    root.get("publishDate"), (LocalDateTime) fieldValue);
            default -> (root, query, cb) -> cb.equal(root.get(fieldName), fieldValue);
        };
    }

    private static Specification<Post> getSpecification(String id) {
        if (id.length() > 10) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("authorId"), UUID.fromString(id));
        }
        return (root, query, cb) -> cb.equal(root.get("id"), Long.parseLong(id));
    }
}
