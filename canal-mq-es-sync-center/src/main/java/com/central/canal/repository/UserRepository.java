package com.central.canal.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.central.canal.domain.User;

public interface UserRepository extends ElasticsearchRepository<User, Long>{

}
