package com.trxjster.organizationservice.mapper;

import com.trxjster.organizationservice.dto.OrganizationDto;
import com.trxjster.organizationservice.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AutoOrganizationMapper {

        AutoOrganizationMapper MAPPER = Mappers.getMapper(AutoOrganizationMapper.class);
        OrganizationDto mapToOrganizationtDto(Organization organization);
        Organization mapToOrganization(OrganizationDto organizationDto);

}
