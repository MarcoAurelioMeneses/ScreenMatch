package br.com.marco.screenmactch.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;


public class ConsultarChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}
