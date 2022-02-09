package com.poscoict.config.app;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:com/poscoict/jblog/config/app/jdbc.properties")
public class DBConfig {
	
	@Autowired
	private Environment env;
	
	
	// Connection Pool DataSource
	@Bean
	public DataSource dataSoruce() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		
		// 초기 연결개수 설정
		dataSource.setInitialSize(env.getProperty("jdbc.initialSize", Integer.class));
		// 아무리 많이 들어와도 100개이상은 들어오지마
		dataSource.setMaxActive(env.getProperty("jdbc.maxActive", Integer.class));
		return dataSource;
	}
}
