package br.com.marco.screenmactch.models;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama");

    private String categoriasOMDB;
    private String categoriaPT;


    Categoria(String categoriasOMDB,
              String categoriaPT){
        this.categoriasOMDB = categoriasOMDB;
        this.categoriaPT = categoriaPT;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriasOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPT(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPT.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
