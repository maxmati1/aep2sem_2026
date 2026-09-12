package com.unicesumar.doacoes;

import com.unicesumar.doacoes.model.Doacao;
import com.unicesumar.doacoes.service.DoacaoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private final DoacaoService service;
    private final Scanner scanner;

    public App(DoacaoService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> cadastrarDoacao();
                    case "2" -> listarDoacoes();
                    case "3" -> buscarDoacao();
                    case "4" -> atualizarStatus();
                    case "5" -> removerDoacao();
                    case "6" -> listarProximasDaValidade();
                    case "0" -> continuar = false;
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n=== Cadastro de Doacoes de Alimentos ===");
        System.out.println("1 - Cadastrar doacao");
        System.out.println("2 - Listar todas as doacoes");
        System.out.println("3 - Buscar doacao por id");
        System.out.println("4 - Atualizar status de uma doacao");
        System.out.println("5 - Remover doacao");
        System.out.println("6 - Listar doacoes proximas da validade");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void cadastrarDoacao() {
        System.out.print("Nome do doador: ");
        String doador = scanner.nextLine();
        System.out.print("Item doado: ");
        String item = scanner.nextLine();
        System.out.print("Categoria (ex: graos, laticinios, higiene): ");
        String categoria = scanner.nextLine();
        System.out.print("Quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        System.out.print("Unidade (kg, unidades, litros...): ");
        String unidade = scanner.nextLine();
        System.out.print("Validade (aaaa-mm-dd): ");
        LocalDate validade = LocalDate.parse(scanner.nextLine());

        Doacao doacao = new Doacao(doador, item, categoria, quantidade, unidade, LocalDate.now(), validade);
        Doacao salva = service.cadastrar(doacao);
        System.out.println("Doacao cadastrada com id: " + salva.getId());
    }

    private void listarDoacoes() {
        List<Doacao> doacoes = service.listarTodas();
        if (doacoes.isEmpty()) {
            System.out.println("Nenhuma doacao cadastrada ainda.");
            return;
        }
        doacoes.forEach(System.out::println);
    }

    private void buscarDoacao() {
        System.out.print("Id da doacao: ");
        String id = scanner.nextLine();
        Optional<Doacao> doacao = service.buscarPorId(id);
        System.out.println(doacao.isPresent() ? doacao.get() : "Doacao nao encontrada.");
    }

    private void atualizarStatus() {
        System.out.print("Id da doacao: ");
        String id = scanner.nextLine();
        System.out.print("Novo status (RECEBIDA, DISTRIBUIDA, DESCARTADA): ");
        String status = scanner.nextLine();
        boolean atualizado = service.atualizarStatus(id, status);
        System.out.println(atualizado ? "Status atualizado." : "Doacao nao encontrada.");
    }

    private void removerDoacao() {
        System.out.print("Id da doacao: ");
        String id = scanner.nextLine();
        boolean removido = service.remover(id);
        System.out.println(removido ? "Doacao removida." : "Doacao nao encontrada.");
    }

    private void listarProximasDaValidade() {
        System.out.print("Ver doacoes que vencem em quantos dias? ");
        int dias = Integer.parseInt(scanner.nextLine());
        List<Doacao> proximas = service.listarProximasDoValidade(dias);
        if (proximas.isEmpty()) {
            System.out.println("Nenhuma doacao vencendo nesse periodo.");
            return;
        }
        proximas.forEach(System.out::println);
    }
}
