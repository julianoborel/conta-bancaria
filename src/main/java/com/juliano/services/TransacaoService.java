package com.juliano.services;

import com.juliano.models.Transacao;
import com.juliano.repositories.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Transacao> listarTodos() {
        return transacaoRepository.findAll();
    }

    public Transacao salvar(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public Transacao buscarPorId(Long id) {
        Optional<Transacao> transacaoOptional = transacaoRepository.findById(id);
        if (transacaoOptional.isPresent()) {
            return transacaoOptional.get();
        } else {
            throw new RuntimeException();
        }
    }

    public List<Transacao> listarPorConta(Long idConta) {
        Query query = entityManager.createQuery("SELECT t FROM Transacao t WHERE t.conta.id = :idConta");
        query.setParameter("idConta", idConta);
        List<Transacao> transacoes = query.getResultList();
        return transacoes;
    }
}
