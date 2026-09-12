package com.unicesumar.doacoes.fakes;

import com.unicesumar.doacoes.model.Doacao;
import com.unicesumar.doacoes.repository.DoacaoRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeDoacaoRepository implements DoacaoRepository {

    private final Map<String, Doacao> dados = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public Doacao salvar(Doacao doacao) {
        String id = String.valueOf(proximoId++);
        doacao.setId(id);
        dados.put(id, doacao);
        return doacao;
    }

    @Override
    public Optional<Doacao> buscarPorId(String id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Doacao> listarTodas() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public boolean atualizar(Doacao doacao) {
        if (!dados.containsKey(doacao.getId())) {
            return false;
        }
        dados.put(doacao.getId(), doacao);
        return true;
    }

    @Override
    public boolean remover(String id) {
        return dados.remove(id) != null;
    }
}
