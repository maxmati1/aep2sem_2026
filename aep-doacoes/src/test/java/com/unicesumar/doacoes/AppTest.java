package com.unicesumar.doacoes;

import com.unicesumar.doacoes.fakes.FakeDoacaoRepository;
import com.unicesumar.doacoes.repository.DoacaoRepository;
import com.unicesumar.doacoes.service.DoacaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    private final PrintStream saidaOriginal = System.out;

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    @Test
    void deveExecutarTodoOFluxoDoMenuSemQuebrar() {
        String entrada = String.join("\n",
                "1", "Maria", "Arroz", "Graos", "10", "kg", "2027-01-01",
                "1", "Joao", "Feijao", "Graos", "abc",
                "2",
                "3", "1",
                "3", "999",
                "4", "1", "DISTRIBUIDA",
                "4", "1", "INVALIDO",
                "6", "5",
                "5", "1",
                "5", "999",
                "9",
                "0"
        ) + "\n";

        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saidaCapturada, true, StandardCharsets.UTF_8));

        DoacaoRepository repository = new FakeDoacaoRepository();
        DoacaoService service = new DoacaoService(repository);
        Scanner scanner = new Scanner(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));

        new App(service, scanner).executar();

        String saida = saidaCapturada.toString(StandardCharsets.UTF_8);

        assertTrue(saida.contains("Doacao cadastrada com id"));
        assertTrue(saida.contains("Erro:"));
        assertTrue(saida.contains("Doacao nao encontrada."));
        assertTrue(saida.contains("Status atualizado."));
        assertTrue(saida.contains("Doacao removida."));
        assertTrue(saida.contains("Opcao invalida."));
    }
}
