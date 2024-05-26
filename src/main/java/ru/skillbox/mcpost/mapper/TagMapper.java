package ru.skillbox.mcpost.mapper;

import ru.skillbox.mcpost.dto.TagDto;
import ru.skillbox.mcpost.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);
    Tag toEntity(TagDto tagDto);
    List<TagDto> toListDto(List<Tag> tags);
    List<Tag> toListEntity(List<TagDto> tagDtos);

}
