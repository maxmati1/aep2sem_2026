package com.unicesumar.doacoes.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoacaoTest {

    @Test
    void construtorPreenchidoDeveIniciarComStatusRecebida() {
        Doacao doacao = new Doacao("Maria", "Arroz", "Graos", 10, "kg",
                LocalDate.now(), LocalDate.now().plusMonths(6));

        assertEquals(Doacao.STATUS_RECEBIDA, doacao.getStatus());
        assertEquals("Maria", doacao.getDoador());
        assertEquals("Arroz", doacao.getItem());
        assertEquals(10, doacao.getQuantidade());
    }

    @Test
    void gettersESettersDevemFuncionarCorretamente() {
        Doacao doacao = new Doacao();
        doacao.setId("abc123");
        doacao.setDoador("Joao");
        doacao.setItem("Feijao");
        doacao.setCategoria("Graos");
        doacao.setQuantidade(5);
        doacao.setUnidade("kg");
        LocalDate hoje = LocalDate.now();
        doacao.setDataRecebimento(hoje);
        doacao.setValidade(hoje.plusMonths(3));
        doacao.setStatus(Doacao.STATUS_DISTRIBUIDA);

        assertEquals("abc123", doacao.getId());
        assertEquals("Joao", doacao.getDoador());
        assertEquals("Feijao", doacao.getItem());
        assertEquals("Graos", doacao.getCategoria());
        assertEquals(5, doacao.getQuantidade());
        assertEquals("kg", doacao.getUnidade());
        assertEquals(hoje, doacao.getDataRecebimento());
        assertEquals(hoje.plusMonths(3), doacao.getValidade());
        assertEquals(Doacao.STATUS_DISTRIBUIDA, doacao.getStatus());
    }

    @Test
    void toStringNaoDeveEstarVazio() {
        Doacao doacao = new Doacao("Ana", "Leite", "Laticinios", 2, "litros",
                LocalDate.now(), LocalDate.now().plusDays(10));
        assertEquals(true, doacao.toString().contains("Ana"));
    }
}
