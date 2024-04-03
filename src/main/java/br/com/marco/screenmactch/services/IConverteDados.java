package br.com.marco.screenmactch.services;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
