package com.korotin.tasker.mapper.config;

import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface MapConfig {
}
