package com.unicesumar.doacoes.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.unicesumar.doacoes.model.Doacao;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MongoDoacaoRepository implements DoacaoRepository {

    private static final String COLECAO = "doacoes";

    private final MongoCollection<Document> colecao;
    private final MongoClient client;

    public MongoDoacaoRepository(String connectionString, String nomeBanco) {
        this.client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(nomeBanco);
        this.colecao = database.getCollection(COLECAO);
    }

    @Override
    public Doacao salvar(Doacao doacao) {
        Document doc = paraDocumento(doacao);
        colecao.insertOne(doc);
        doacao.setId(doc.getObjectId("_id").toHexString());
        return doacao;
    }

    @Override
    public Optional<Doacao> buscarPorId(String id) {
        Document doc = colecao.find(Document.parse("{\"_id\": {\"$oid\": \"" + id + "\"}}")).first();
        return Optional.ofNullable(doc).map(this::paraDoacao);
    }

    @Override
    public List<Doacao> listarTodas() {
        List<Doacao> resultado = new ArrayList<>();
        FindIterable<Document> documentos = colecao.find();
        for (Document doc : documentos) {
            resultado.add(paraDoacao(doc));
        }
        return resultado;
    }

    @Override
    public boolean atualizar(Doacao doacao) {
        Document filtro = new Document("_id", new ObjectId(doacao.getId()));
        Document valores = new Document("$set", paraDocumento(doacao));
        UpdateResult resultado = colecao.updateOne(filtro, valores);
        return resultado.getModifiedCount() > 0;
    }

    @Override
    public boolean remover(String id) {
        Document filtro = new Document("_id", new ObjectId(id));
        DeleteResult resultado = colecao.deleteOne(filtro);
        return resultado.getDeletedCount() > 0;
    }

    private Document paraDocumento(Doacao doacao) {
        Document doc = new Document();
        doc.put("doador", doacao.getDoador());
        doc.put("item", doacao.getItem());
        doc.put("categoria", doacao.getCategoria());
        doc.put("quantidade", doacao.getQuantidade());
        doc.put("unidade", doacao.getUnidade());
        doc.put("dataRecebimento", toDate(doacao.getDataRecebimento()));
        doc.put("validade", toDate(doacao.getValidade()));
        doc.put("status", doacao.getStatus());
        return doc;
    }

    private Doacao paraDoacao(Document doc) {
        Doacao doacao = new Doacao();
        doacao.setId(doc.getObjectId("_id").toHexString());
        doacao.setDoador(doc.getString("doador"));
        doacao.setItem(doc.getString("item"));
        doacao.setCategoria(doc.getString("categoria"));
        doacao.setQuantidade(doc.getInteger("quantidade", 0));
        doacao.setUnidade(doc.getString("unidade"));
        doacao.setDataRecebimento(toLocalDate(doc.getDate("dataRecebimento")));
        doacao.setValidade(toLocalDate(doc.getDate("validade")));
        doacao.setStatus(doc.getString("status"));
        return doacao;
    }

    private Date toDate(LocalDate data) {
        if (data == null) {
            return null;
        }
        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Date data) {
        if (data == null) {
            return null;
        }
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void fechar() {
        client.close();
    }
}
