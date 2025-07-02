package com.example.springboot;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, DataSource dataSource) {
		return args -> {

			System.out.println("✅ Starting Spring Boot Application...");

			// ✅ Check DB connection
			try (Connection connection = dataSource.getConnection()) {
				System.out.println("✅ Successfully connected to DB: " + connection.getMetaData().getURL());
				System.out.println("   → DB Username: " + connection.getMetaData().getUserName());
				System.out.println("   → DB Product: " + connection.getMetaData().getDatabaseProductName());
			} catch (SQLException e) {
				System.err.println("❌ Failed to connect to the database: " + e.getMessage());
			}
		};
	}
}

