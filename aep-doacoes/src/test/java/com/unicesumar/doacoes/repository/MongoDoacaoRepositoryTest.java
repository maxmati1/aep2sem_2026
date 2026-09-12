package com.unicesumar.doacoes.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.unicesumar.doacoes.model.Doacao;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MongoDoacaoRepositoryTest {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String BANCO_TESTE = "doacoes_db_test";

    private static MongoDoacaoRepository repository;
    private static MongoClient clientDeLimpeza;

    @BeforeAll
    static void configurar() {
        repository = new MongoDoacaoRepository(CONNECTION_STRING, BANCO_TESTE);
        clientDeLimpeza = MongoClients.create(CONNECTION_STRING);
    }

    @AfterAll
    static void limparBancoDeTeste() {
        clientDeLimpeza.getDatabase(BANCO_TESTE).drop();
        clientDeLimpeza.close();
        repository.fechar();
    }

    private Doacao doacaoValida() {
        return new Doacao("Maria", "Arroz", "Graos", 10, "kg",
                LocalDate.now(), LocalDate.now().plusMonths(6));
    }

    @Test
    void deveSalvarEBuscarPorId() {
        Doacao salva = repository.salvar(doacaoValida());

        assertNotNull(salva.getId());
        Optional<Doacao> encontrada = repository.buscarPorId(salva.getId());
        assertTrue(encontrada.isPresent());
        assertEquals("Maria", encontrada.get().getDoador());
        assertEquals(10, encontrada.get().getQuantidade());
    }

    @Test
    void buscarPorIdInexistenteDeveRetornarVazio() {
        Optional<Doacao> encontrada = repository.buscarPorId(new ObjectId().toHexString());
        assertTrue(encontrada.isEmpty());
    }

    @Test
    void deveListarTodasAsDoacoesSalvas() {
        repository.salvar(doacaoValida());
        repository.salvar(doacaoValida());

        List<Doacao> todas = repository.listarTodas();

        assertTrue(todas.size() >= 2);
    }

    @Test
    void deveAtualizarUmaDoacaoExistente() {
        Doacao salva = repository.salvar(doacaoValida());
        salva.setStatus(Doacao.STATUS_DISTRIBUIDA);

        boolean atualizado = repository.atualizar(salva);

        assertTrue(atualizado);
        Optional<Doacao> encontrada = repository.buscarPorId(salva.getId());
        assertEquals(Doacao.STATUS_DISTRIBUIDA, encontrada.get().getStatus());
    }

    @Test
    void deveRemoverUmaDoacaoExistente() {
        Doacao salva = repository.salvar(doacaoValida());

        boolean removido = repository.remover(salva.getId());

        assertTrue(removido);
        assertTrue(repository.buscarPorId(salva.getId()).isEmpty());
    }

    @Test
    void deveRetornarFalsoAoRemoverIdInexistente() {
        boolean removido = repository.remover(new ObjectId().toHexString());
        assertFalse(removido);
    }
}
