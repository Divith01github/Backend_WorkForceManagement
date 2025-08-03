package com.assignment.workforcemgmt.mapper;

import com.assignment.workforcemgmt.dto.TaskManagementDto;
import com.assignment.workforcemgmt.model.TaskManagement;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {
    ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);


    TaskManagementDto modelToDto(TaskManagement model);


    TaskManagement dtoToModel(TaskManagementDto dto);


    List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);
}

