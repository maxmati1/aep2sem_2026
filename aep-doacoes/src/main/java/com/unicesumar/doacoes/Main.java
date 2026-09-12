package com.unicesumar.doacoes;

import com.unicesumar.doacoes.repository.MongoDoacaoRepository;
import com.unicesumar.doacoes.service.DoacaoService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017";
        String nomeBanco = "doacoes_db";

        MongoDoacaoRepository repository = new MongoDoacaoRepository(connectionString, nomeBanco);
        DoacaoService service = new DoacaoService(repository);
        Scanner scanner = new Scanner(System.in);

        new App(service, scanner).executar();

        repository.fechar();
        System.out.println("Encerrado.");
    }
}
