package com.unicesumar.doacoes.repository;

import com.unicesumar.doacoes.model.Doacao;

import java.util.List;
import java.util.Optional;

public interface DoacaoRepository {

    Doacao salvar(Doacao doacao);

    Optional<Doacao> buscarPorId(String id);

    List<Doacao> listarTodas();

    boolean atualizar(Doacao doacao);

    boolean remover(String id);
}
