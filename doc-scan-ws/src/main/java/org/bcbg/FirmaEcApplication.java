package org.bcbg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FirmaEcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirmaEcApplication.class, args);
	}

}
