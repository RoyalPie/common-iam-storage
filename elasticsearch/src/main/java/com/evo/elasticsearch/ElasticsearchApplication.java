package com.evo.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableElasticsearchRepositories(basePackages = "com.evo.elasticsearch.infrastructure")
public class ElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchApplication.class, args);
	}

}
