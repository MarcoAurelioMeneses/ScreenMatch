package br.com.marco.screenmactch;

import br.com.marco.screenmactch.principal.Principal;
import br.com.marco.screenmactch.repositorio.SerieRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmactchApplication  implements CommandLineRunner {
	@Autowired
	private SerieRepositorio repositorio;

	public static void main(String[] args) {

		SpringApplication.run(
				ScreenmactchApplication.class, args
		);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();

	}

}
