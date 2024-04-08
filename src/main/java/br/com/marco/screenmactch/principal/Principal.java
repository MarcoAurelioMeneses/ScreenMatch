package br.com.marco.screenmactch.principal;

import br.com.marco.screenmactch.models.DadosEpisodio;
import br.com.marco.screenmactch.models.DadosSerie;
import br.com.marco.screenmactch.models.DadosTemporada;
import br.com.marco.screenmactch.models.Episodio;
import br.com.marco.screenmactch.services.ConsumoAPI;
import br.com.marco.screenmactch.services.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner entradaDeDados = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY =  "&apikey=939b7e0b";

    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = entradaDeDados.nextLine();
        var json = consumoAPI.obterDados(ENDERECO +
                nomeSerie.replace(" ", "+") +
                API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i<= dados.totalTemporadas(); i++){
        	json = consumoAPI.obterDados(ENDERECO +
                    nomeSerie.replace(" ", "+") +
                    "&season=" + i +
                    API_KEY);
        	DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
        	temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for (int i = 0; i<= dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 episódios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro Filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(5)
                .peek(e -> System.out.println("Limite " + e))
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());


        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano voce deseja ver os espisódios ");
        var ano = entradaDeDados.nextInt();
        entradaDeDados.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null &&
                        e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                "Episódio: " + e.getTitulo() +
                                 "Data lançamento: " + e.getDataLancamento().format(formatter)
                ));
    }
}
