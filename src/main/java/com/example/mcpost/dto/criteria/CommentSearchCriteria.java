package com.example.mcpost.dto.criteria;

import com.example.mcpost.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class CommentSearchCriteria {
    private Long postId;
    private Long parentId;
    private Boolean isDeleted;

    public Specification<Comment> getSpecification() {
        return getSpecification(this);
    }

    private Specification<Comment> getSpecification(CommentSearchCriteria criteria) {
        Map<String, Object> fields = new HashMap<>();
        Arrays.stream(CommentSearchCriteria.class.getDeclaredFields()).forEach(field -> {
            try {
                fields.put(field.getName(), field.get(criteria));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
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

