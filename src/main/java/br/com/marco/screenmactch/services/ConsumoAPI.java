package br.com.marco.screenmactch.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    public String obterDados(String endereco){
        //Mandando a requisição
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null;
        //Recebendo e tratando a resposta
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException e ){
            throw new RuntimeException(e);
        }catch (InterruptedException e ){
            throw new RuntimeException(e);
        }
        //Retornando a reposta da requisição
        String json = response.body();
        return json;
    };
}
