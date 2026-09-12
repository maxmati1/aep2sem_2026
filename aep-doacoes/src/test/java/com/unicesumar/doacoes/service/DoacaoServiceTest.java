package com.unicesumar.doacoes.service;

import com.unicesumar.doacoes.fakes.FakeDoacaoRepository;
import com.unicesumar.doacoes.model.Doacao;
import com.unicesumar.doacoes.repository.DoacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoacaoServiceTest {

    private DoacaoRepository repository;
    private DoacaoService service;

    @BeforeEach
    void setUp() {
        repository = new FakeDoacaoRepository();
        service = new DoacaoService(repository);
    }

    private Doacao doacaoValida() {
        return new Doacao("Maria", "Arroz", "Graos", 10, "kg",
                LocalDate.now(), LocalDate.now().plusMonths(6));
    }

    @Test
    void deveCadastrarDoacaoValidaComStatusRecebida() {
        Doacao salva = service.cadastrar(doacaoValida());

        assertTrue(salva.getId() != null && !salva.getId().isBlank());
        assertEquals(Doacao.STATUS_RECEBIDA, salva.getStatus());
    }

    @Test
    void naoDeveCadastrarDoacaoSemDoador() {
        Doacao doacao = doacaoValida();
        doacao.setDoador("  ");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(doacao));
    }

    @Test
    void naoDeveCadastrarDoacaoSemItem() {
        Doacao doacao = doacaoValida();
        doacao.setItem(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(doacao));
    }

    @Test
    void naoDeveCadastrarDoacaoComQuantidadeZeroOuNegativa() {
        Doacao doacao = doacaoValida();
        doacao.setQuantidade(0);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(doacao));
    }

    @Test
    void naoDeveCadastrarDoacaoSemUnidade() {
        Doacao doacao = doacaoValida();
        doacao.setUnidade("");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(doacao));
    }

    @Test
    void naoDeveCadastrarDoacaoSemDataDeRecebimento() {
        Doacao doacao = doacaoValida();
        doacao.setDataRecebimento(null);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(doacao));
    }

    @Test
    void naoDeveCadastrarDoacaoNula() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
    }

    @Test
    void deveBuscarDoacaoCadastradaPeloId() {
        Doacao salva = service.cadastrar(doacaoValida());

        Optional<Doacao> encontrada = service.buscarPorId(salva.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Maria", encontrada.get().getDoador());
    }

    @Test
    void buscarPorIdComIdVazioDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(""));
    }

    @Test
    void deveListarTodasAsDoacoesCadastradas() {
        service.cadastrar(doacaoValida());
        service.cadastrar(doacaoValida());

        List<Doacao> todas = service.listarTodas();

        assertEquals(2, todas.size());
    }

    @Test
    void deveAtualizarStatusDeDoacaoExistente() {
        Doacao salva = service.cadastrar(doacaoValida());

        boolean atualizado = service.atualizarStatus(salva.getId(), Doacao.STATUS_DISTRIBUIDA);

        assertTrue(atualizado);
        assertEquals(Doacao.STATUS_DISTRIBUIDA, service.buscarPorId(salva.getId()).get().getStatus());
    }

    @Test
    void atualizarStatusDeDoacaoInexistenteDeveRetornarFalso() {
        boolean atualizado = service.atualizarStatus("id-que-nao-existe", Doacao.STATUS_DISTRIBUIDA);

        assertFalse(atualizado);
    }

    @Test
    void atualizarComStatusInvalidoDeveLancarExcecao() {
        Doacao salva = service.cadastrar(doacaoValida());

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizarStatus(salva.getId(), "VENCIDA"));
    }

    @Test
    void deveRemoverDoacaoExistente() {
        Doacao salva = service.cadastrar(doacaoValida());

        boolean removido = service.remover(salva.getId());

        assertTrue(removido);
        assertTrue(service.buscarPorId(salva.getId()).isEmpty());
    }

    @Test
    void removerComIdVazioDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.remover(" "));
    }

    @Test
    void listarProximasDaValidadeDeveIgnorarDoacoesJaDistribuidas() {
        Doacao vencendoEDistribuida = service.cadastrar(new Doacao(
                "Joao", "Leite", "Laticinios", 5, "litros", LocalDate.now(), LocalDate.now().plusDays(2)));
        service.atualizarStatus(vencendoEDistribuida.getId(), Doacao.STATUS_DISTRIBUIDA);

        service.cadastrar(new Doacao(
                "Ana", "Feijao", "Graos", 8, "kg", LocalDate.now(), LocalDate.now().plusDays(3)));

        List<Doacao> proximas = service.listarProximasDoValidade(5);

        assertEquals(1, proximas.size());
        assertEquals("Ana", proximas.get(0).getDoador());
    }

    @Test
    void listarProximasDaValidadeDeveIgnorarDoacoesForaDoPrazo() {
        service.cadastrar(new Doacao(
                "Carlos", "Arroz", "Graos", 20, "kg", LocalDate.now(), LocalDate.now().plusDays(30)));

        List<Doacao> proximas = service.listarProximasDoValidade(5);

        assertTrue(proximas.isEmpty());
    }

    @Test
    void listarProximasDaValidadeComDiasNegativosDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.listarProximasDoValidade(-1));
    }
}
