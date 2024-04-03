package br.com.marco.screenmactch;

import br.com.marco.screenmactch.models.DadosSerie;
import br.com.marco.screenmactch.services.ConsumoAPI;
import br.com.marco.screenmactch.services.ConverteDados;
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
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=Game+of+Thrones&apikey=939b7e0b");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
