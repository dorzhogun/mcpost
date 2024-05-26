package ru.skillbox.mcpost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class McpostApplication {
	public static void main(String[] args) {
		SpringApplication.run(McpostApplication.class, args);
	}
}
