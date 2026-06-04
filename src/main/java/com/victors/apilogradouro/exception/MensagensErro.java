package com.victors.apilogradouro.exception;

// TODO: adicionar mensagens das outras entidades conforme forem criadas

public final class MensagensErro {

    private MensagensErro() {}

    public static final String UF_NAO_ENCONTRADA  = "UF não encontrada.";
    public static final String UF_SIGLA_DUPLICADA = "Já existe uma UF cadastrada com esta sigla.";

    public static final String CIDADE_NAO_ENCONTRADA  = "Cidade não encontrada.";
    public static final String CIDADE_NOME_DUPLICADO  = "Já existe uma cidade cadastrada com este nome.";

    public static final String BAIRRO_NAO_ENCONTRADO  = "Bairro não encontrado.";
    public static final String BAIRRO_NOME_DUPLICADO  = "Já existe um bairro cadastrado com este nome.";

    public static final String LOGRADOURO_NAO_ENCONTRADO = "Logradouro não encontrado.";
    public static final String LOGRADOURO_CEP_DUPLICADO  = "Já existe um logradouro cadastrado com este CEP.";

    public static final String UF_COM_CIDADES        = "Não é possível excluir uma UF que possui cidades cadastradas.";
    public static final String CIDADE_COM_BAIRROS    = "Não é possível excluir uma cidade que possui bairros cadastrados.";
    public static final String BAIRRO_COM_LOGRADOUROS = "Não é possível excluir um bairro que possui logradouros cadastrados.";
}
