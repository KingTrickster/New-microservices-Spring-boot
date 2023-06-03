package com.trxjster.organizationservice.service;

import com.trxjster.organizationservice.dto.OrganizationDto;
import com.trxjster.organizationservice.entity.Organization;
import com.trxjster.organizationservice.mapper.AutoOrganizationMapper;
import com.trxjster.organizationservice.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
        Organization organization = AutoOrganizationMapper.MAPPER.mapToOrganization(organizationDto);
        organization.setOrganizationCreatedDate(LocalDateTime.now());

        Organization savedOrganization = organizationRepository.save(organization);
        OrganizationDto response = AutoOrganizationMapper.MAPPER.mapToOrganizationtDto(savedOrganization);
        return response;
    }

    @Override
    public OrganizationDto getOrganizationByCode(String organizationCode) {
        Organization organization = organizationRepository.findByOrganizationCode(organizationCode).get();
        return AutoOrganizationMapper.MAPPER.mapToOrganizationtDto(organization);
    }
}
