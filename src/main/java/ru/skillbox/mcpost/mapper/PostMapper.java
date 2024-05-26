package ru.skillbox.mcpost.mapper;

import ru.skillbox.mcpost.dto.PostDto;
import ru.skillbox.mcpost.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { TagMapper.class })
public interface PostMapper {
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Post toEntity(PostDto postDto);
    @Mapping(
            target = "likeAmount",
            expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)"
    )
    @Mapping(
            target = "commentsCount",
            expression = "java(post.getComments() != null ? post.getComments().size() : 0)"
    )
    PostDto toDto(Post post);
    List<PostDto> toDtoList(List<Post> posts);
}
