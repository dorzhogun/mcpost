package ru.skillbox.mcpost.mapper;

import ru.skillbox.mcpost.dto.CommentDto;
import ru.skillbox.mcpost.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(
            target = "likeAmount",
            expression = "java(" +
                    "comment.getLikes() != null ? " +
                    "(int) comment.getLikes().stream()" +
                    ".filter(like -> !like.getIsDeleted())" +
                    ".count() : 0" +
                    ")"
    )
    @Mapping(
            target = "commentsCount",
            expression = "java(" +
                    "comment.getComments() != null ? " +
                    "(int) comment.getComments().stream()" +
                    ".filter(com -> !com.getIsDeleted())" +
                    ".count() : 0" +
                    ")"
    )
    @Mapping(
            target = "commentType",
            expression = "java(" +
                    "comment.getParentId() != null ?" +
                    "ru.skillbox.mcpost.model.enums.CommentType.COMMENT :" +
                    "ru.skillbox.mcpost.model.enums.CommentType.POST" +
                    ")"
    )
    CommentDto toDto(Comment comment);
    List<CommentDto> toDtoList(List<Comment> comments);
}