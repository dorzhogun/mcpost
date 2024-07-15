package ru.skillbox.mcpost.dto.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.skillbox.mcpost.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class CommentSearchCriteria {
    @JsonProperty("postId")
    private Long postId;
    @JsonProperty("parentId")
    private Long parentId;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    public Specification<Comment> getSpecification() {
        return getSpecification(this);
    }

    private Specification<Comment> getSpecification(CommentSearchCriteria criteria) {
        Map<String, Object> fields = new HashMap<>();
        for (Field field : CommentSearchCriteria.class.getDeclaredFields()) {
            try {
                fields.put(field.getName(), field.get(criteria));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access field: " + field.getName(), e);
            }
        }
        return Specification.allOf(
                fields.keySet().stream()
                        .map(fieldName -> CommentSearchCriteria.getSpecification(fieldName, fields))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private static Specification<Comment> getSpecification(String fieldName, Map<String, Object> fields) {
        Object fieldValue = fields.get(fieldName);
        if (fieldValue == null) {
            return null;
        }
        if (fieldName.equals("parentId") && (Long) fieldValue == 0) {
            return (root, query, cb) -> cb.isNull(root.get(fieldName));
        }
        return (root, query, cb) -> cb.equal(root.get(fieldName), fieldValue);
    }
}
