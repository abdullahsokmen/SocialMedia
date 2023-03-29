package com.abdullah.config.mapper;

import com.abdullah.dto.request.CreateFollowRequestDto;
import com.abdullah.repository.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IFollowMapper {

    IFollowMapper INSTANCE= Mappers.getMapper(IFollowMapper.class);


    Follow toFollow(final CreateFollowRequestDto dto,final String userId);
}
