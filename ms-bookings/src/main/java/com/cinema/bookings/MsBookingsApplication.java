package com.cinema.bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients // <-- Activa los clientes HTTP automáticos
@SpringBootApplication
public class MsBookingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBookingsApplication.class, args);
	}

}
