package com.juliano.services;

import com.juliano.models.Pessoa;
import com.juliano.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Pessoa> listarTodos() {

        return pessoaRepository.findAll();
    }

    public Pessoa salvar(Pessoa pessoa) {

        return pessoaRepository.save(pessoa);
    }

    public void deletar(Pessoa pessoa) {
        pessoaRepository.delete(pessoa);

    }

    public Pessoa buscarPorId(Long id) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            return pessoaOptional.get();
        } else {
            throw new RuntimeException();
        }
    }

    public List<Pessoa> listarPessoasComContas() {
        String jpql = "SELECT DISTINCT c.pessoa FROM Conta c";
        TypedQuery<Pessoa> query = entityManager.createQuery(jpql, Pessoa.class);
        return query.getResultList();
    }
}