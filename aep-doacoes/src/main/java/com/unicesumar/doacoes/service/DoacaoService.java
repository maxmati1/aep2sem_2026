package com.unicesumar.doacoes.service;

import com.unicesumar.doacoes.model.Doacao;
import com.unicesumar.doacoes.repository.DoacaoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoacaoService {

    private static final List<String> STATUS_VALIDOS = List.of(
            Doacao.STATUS_RECEBIDA, Doacao.STATUS_DISTRIBUIDA, Doacao.STATUS_DESCARTADA
    );

    private final DoacaoRepository repository;

    public DoacaoService(DoacaoRepository repository) {
        this.repository = repository;
    }

    public Doacao cadastrar(Doacao doacao) {
        validarCamposObrigatorios(doacao);

        if (doacao.getStatus() == null || doacao.getStatus().isBlank()) {
            doacao.setStatus(Doacao.STATUS_RECEBIDA);
        }

        return repository.salvar(doacao);
    }

    public Optional<Doacao> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O id da doacao e obrigatorio.");
        }
        return repository.buscarPorId(id);
    }

    public List<Doacao> listarTodas() {
        return repository.listarTodas();
    }

    public boolean atualizarStatus(String id, String novoStatus) {
        if (!STATUS_VALIDOS.contains(novoStatus)) {
            throw new IllegalArgumentException(
                    "Status invalido. Use um dos seguintes: " + STATUS_VALIDOS);
        }

        Optional<Doacao> existente = repository.buscarPorId(id);
        if (existente.isEmpty()) {
            return false;
        }

        Doacao doacao = existente.get();
        doacao.setStatus(novoStatus);
        return repository.atualizar(doacao);
    }

    public boolean remover(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O id da doacao e obrigatorio.");
        }
        return repository.remover(id);
    }

    public List<Doacao> listarProximasDoValidade(int diasLimite) {
        if (diasLimite < 0) {
            throw new IllegalArgumentException("O numero de dias nao pode ser negativo.");
        }

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(diasLimite);

        List<Doacao> proximasDoVencimento = new ArrayList<>();
        for (Doacao doacao : repository.listarTodas()) {
            if (Doacao.STATUS_DISTRIBUIDA.equals(doacao.getStatus())) {
                continue;
            }
            if (doacao.getValidade() == null) {
                continue;
            }
            boolean dentroDoLimite = !doacao.getValidade().isBefore(hoje)
                    && !doacao.getValidade().isAfter(limite);
            if (dentroDoLimite) {
                proximasDoVencimento.add(doacao);
            }
        }
        return proximasDoVencimento;
    }

    private void validarCamposObrigatorios(Doacao doacao) {
        if (doacao == null) {
            throw new IllegalArgumentException("A doacao nao pode ser nula.");
        }
        if (isBlank(doacao.getDoador())) {
            throw new IllegalArgumentException("O nome do doador e obrigatorio.");
        }
        if (isBlank(doacao.getItem())) {
            throw new IllegalArgumentException("O item doado e obrigatorio.");
        }
        if (doacao.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (isBlank(doacao.getUnidade())) {
            throw new IllegalArgumentException("A unidade de medida e obrigatoria (ex: kg, unidades, litros).");
        }
        if (doacao.getDataRecebimento() == null) {
            throw new IllegalArgumentException("A data de recebimento e obrigatoria.");
        }
    }

    private boolean isBlank(String texto) {
        return texto == null || texto.isBlank();
    }
}
