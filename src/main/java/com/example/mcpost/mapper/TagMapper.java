package com.example.mcpost.mapper;

import com.example.mcpost.dto.TagDto;
import com.example.mcpost.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);
    Tag toEntity(TagDto tagDto);
    List<TagDto> toListDto(List<Tag> tags);
    List<Tag> toListEntity(List<TagDto> tagDtos);

}
