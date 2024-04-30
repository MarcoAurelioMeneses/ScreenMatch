package br.com.marco.screenmactch.principal;

import br.com.marco.screenmactch.models.*;
import br.com.marco.screenmactch.repositorio.SerieRepositorio;
import br.com.marco.screenmactch.services.ConsumoAPI;
import br.com.marco.screenmactch.services.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    private Scanner entradaDeDados = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=939b7e0b";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepositorio repositorio;
    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {

        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Séries buscadas
                4 - Buscar série por título
                5 - Buscar por ator
                6 - Top 5 séries
                7 - Buscar por categoria
                
                0 - Sair                                 
                """;

            System.out.println(menu);
            opcao = entradaDeDados.nextInt();
            entradaDeDados.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    BuscarSeriePorTitulo();
                    break;
                case 5:
                    BuscarSeriePorAtor();
                    break;
                case 6:
                    BuscarTop5();
                    break;
                case 7:
                    BuscarPorCategoria();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = entradaDeDados.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = entradaDeDados.next();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obterDados(ENDERECO + serieEncontrada
                        .getTitulo().replace(" ", "+") +
                        "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor
                        .obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =
            temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas() {

        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void BuscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = entradaDeDados.next();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da série: " + serieBuscada.get());
        }else {
            System.out.println("Série não encontrada!");
        }
    }

    private void BuscarSeriePorAtor() {
        System.out.println("Qual o nome para busca");
        var nomeAtor = entradaDeDados.nextLine();
        System.out.println("Avaliações a partir de que valor: ");
        var avaliacao = entradaDeDados.nextDouble();
        List<Serie> seriesEncontradas = repositorio
                .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(
                nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println(
                s.getTitulo() + " avaliação: " + s.getAvaliacao()
        ));
    }

    private void BuscarTop5() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void BuscarPorCategoria() {
        System.out.println("Qual o genero deseja buscar: ");
        var nomeGenero = entradaDeDados.nextLine();
        Categoria categoria = Categoria.fromPT(nomeGenero);
        List<Serie> seriesPorcategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries do genero " + nomeGenero);
        seriesPorcategoria.forEach(System.out::println);

    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = entradaDeDados.nextInt();
        entradaDeDados.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = entradaDeDados.nextDouble();
        entradaDeDados.nextLine();
        List<Serie> filtroSeries = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }
}



