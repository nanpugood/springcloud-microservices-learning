package com.central.canal.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
@Document(indexName = "canal-user", type = "user",shards = 3, refreshInterval="-1")
public class User implements Serializable {
	
	private static final long serialVersionUID = -7008244946458464646L;

	@Id
    private Integer id;
	@Field(analyzer = "ik_max_word",type = FieldType.Text,fielddata=true)
    private String name;
	@Field(type = FieldType.Keyword)
    private String age;
	
}
