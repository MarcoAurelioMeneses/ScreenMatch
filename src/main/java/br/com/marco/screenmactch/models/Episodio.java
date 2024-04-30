package br.com.marco.screenmactch.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.DateTimeException;
import java.time.LocalDate;


@Entity
@Table(name = "episodios")
public class Episodio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Double avaliacao;
    private LocalDate dataLancamento;
    private Integer numero;
    @ManyToOne
    private Serie serie;

    public Episodio(){}







    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numero = dadosEpisodio.numeroEpisodio();
        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        }catch (DateTimeException ex){
            this.dataLancamento = null;
        }


    }




    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return  "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento +
                ", numero=" + numero ;

    }


}
