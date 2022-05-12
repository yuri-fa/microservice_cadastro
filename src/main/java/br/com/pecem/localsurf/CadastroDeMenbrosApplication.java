package br.com.pecem.localsurf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableAsync
public class CadastroDeMenbrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroDeMenbrosApplication.class, args);
	}

}
