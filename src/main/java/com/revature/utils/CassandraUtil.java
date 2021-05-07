package com.revature.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

@Component
public class CassandraUtil {
	
	private static CqlSession session;
	
	@Bean
	public static CqlSession getSession() {
		if(session == null) {
			DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
			session = CqlSession.builder().withConfigLoader(loader).withKeyspace("trms").build();
		}
		return session;
	}
	
	public static void closeSession() {
		session.close();
		session = null;
	}
}
