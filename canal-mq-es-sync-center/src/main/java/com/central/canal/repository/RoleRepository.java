package com.central.canal.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.central.canal.domain.Role;

public interface RoleRepository extends ElasticsearchRepository<Role, Long>{

}
