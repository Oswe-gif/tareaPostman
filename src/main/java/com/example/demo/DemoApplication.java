package com.example.demo;
import com.example.demo.service.SQLiteConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SQLiteConnection.connect();
		SpringApplication.run(DemoApplication.class, args);
	}

}
