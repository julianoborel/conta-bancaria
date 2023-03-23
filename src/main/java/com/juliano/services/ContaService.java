package com.juliano.services;

import com.juliano.models.Conta;
import com.juliano.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Conta> listarTodos() {
        return contaRepository.findAll();
    }

    public Conta salvar(Conta conta) {
        return contaRepository.save(conta);
    }

    public void deletar(Conta conta) {
        contaRepository.delete(conta);
    }

    public Conta buscarPorId(Long id) {
        Optional<Conta> contaOptional = contaRepository.findById(id);
        if (contaOptional.isPresent()) {
            return contaOptional.get();
        } else {
            throw new RuntimeException();
        }
    }

    public List<Conta> listarPorPessoa(Long idPessoa) {
        Query query = entityManager.createQuery("SELECT c FROM Conta c WHERE c.pessoa.id = :idPessoa");
        query.setParameter("idPessoa", idPessoa);
        List<Conta> contas = query.getResultList();
        return contas;
    }
}
