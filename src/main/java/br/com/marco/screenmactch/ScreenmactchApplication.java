package br.com.marco.screenmactch;

import br.com.marco.screenmactch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmactchApplication  implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(
				ScreenmactchApplication.class, args
		);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();

	}

}
