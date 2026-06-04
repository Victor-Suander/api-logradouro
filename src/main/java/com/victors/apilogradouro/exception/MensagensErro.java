package com.victors.apilogradouro.exception;

// TODO: adicionar mensagens das outras entidades conforme forem criadas

/** Centraliza as mensagens de erro da aplicação para evitar strings duplicadas no código. */
public final class MensagensErro {

    private MensagensErro() {}

    public static final String UF_NAO_ENCONTRADA   = "UF não encontrada.";
    public static final String UF_SIGLA_DUPLICADA  = "Já existe uma UF cadastrada com esta sigla.";
}