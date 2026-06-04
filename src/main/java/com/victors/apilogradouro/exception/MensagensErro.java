package com.victors.apilogradouro.exception;

// TODO: adicionar mensagens das outras entidades conforme forem criadas

public final class MensagensErro {

    private MensagensErro() {}

    public static final String UF_NAO_ENCONTRADA  = "UF não encontrada.";
    public static final String UF_SIGLA_DUPLICADA = "Já existe uma UF cadastrada com esta sigla.";

    public static final String CIDADE_NAO_ENCONTRADA  = "Cidade não encontrada.";
    public static final String CIDADE_NOME_DUPLICADO  = "Já existe uma cidade cadastrada com este nome.";
}
