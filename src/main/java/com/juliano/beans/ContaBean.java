package com.juliano.beans;

import com.juliano.models.Conta;
import com.juliano.models.Pessoa;
import com.juliano.models.Transacao;
import com.juliano.services.ContaService;
import com.juliano.services.PessoaService;
import com.juliano.services.TransacaoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@ManagedBean
@ViewScoped
@Getter
@Setter
public class ContaBean implements Serializable {

    @Autowired
    private ContaService contaService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private TransacaoService transacaoService;

    private List<Transacao> transacoesFiltradas;

    private Conta conta = new Conta();

    private Pessoa pessoa = new Pessoa();

    private Transacao transacao = new Transacao();

    private String cor;

    private Pessoa pessoaSelecionada;

    private Conta contaSelecionada;

    private boolean tabelaRenderizada = false;

    private List<Conta> contas;

    private List<Pessoa> pessoas;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;

    private Double valor;

    private String labelContas;

    private Long idPessoaSelecionada;

    private Long idContaSelecionada;

    private String tipoOperacao;


    @PostConstruct
    public void init() {
        conta = new Conta();
        contas = contaService.listarTodos();
        pessoa = new Pessoa();
        pessoas = pessoaService.listarTodos();
        transacao = new Transacao();
    }

    public List<SelectItem> getOpcoesPessoas() {
        List<Pessoa> pessoas = pessoaService.listarTodos();
        List<SelectItem> opcoes = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            String label = pessoa.getNome() + " - " + pessoa.getCpf();
            SelectItem selectItem = new SelectItem(pessoa, label);
            opcoes.add(selectItem);
        }
        return opcoes;
    }

    public List<SelectItem> getOpcoesPessoasComContas() {
        List<Pessoa> pessoas = pessoaService.listarPessoasComContas();
        List<SelectItem> opcoes = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            labelContas = pessoa.getNome() + " - " + pessoa.getCpf();
            SelectItem selectItem = new SelectItem(pessoa, labelContas);
            opcoes.add(selectItem);
        }
        return opcoes;
    }


    public void atualizarIdPessoaSelecionada() {
        if (pessoaSelecionada != null) {
            idPessoaSelecionada = pessoaSelecionada.getId();
        } else {
            idPessoaSelecionada = null;
        }
    }

    public List<SelectItem> getOpcoesContas(Long idPessoa) {
        if (idPessoaSelecionada == null) {
            return new ArrayList<SelectItem>();
        }
        List<Conta> contas = contaService.listarPorPessoa(idPessoaSelecionada);
        List<SelectItem> opcoes = new ArrayList<>();
        for (Conta conta : contas) {
            String label = conta.getNumero() + " - R$ Saldo: " + conta.getSaldo();
            SelectItem selectItem = new SelectItem(conta, label);
            opcoes.add(selectItem);
        }
        return opcoes;
    }

    public void atualizarIdContaSelecionada() {
        if (contaSelecionada != null) {
            idContaSelecionada = contaSelecionada.getId();
        } else {
            idContaSelecionada = null;
        }
    }

    public void filtrarTransacoes() {
        transacoesFiltradas = new ArrayList<>();
        for (Transacao t : transacoes) {
            if (t.getConta().equals(contaSelecionada)) {
                transacoesFiltradas.add(t);
            }
        }
    }

    public String salvar(){

        for (Pessoa pessoa : pessoas) {

            if (pessoa.getCpf().equals(pessoaSelecionada.getCpf())) {
                pessoaService.salvar(pessoa);
                conta.setPessoa(pessoa);
                conta.setSaldo(0.0);
                contaService.salvar(conta);
                break;
            }
        }
        return "conta?faces-redirect=true";
    }


    public void atualizarSaldo() {
        for (Conta conta : contas) {
            if (tipoOperacao.equals("Depositar")) {
                if(conta.getPessoa().getCpf().equals(pessoaSelecionada.getCpf())) {
                    if(conta.getNumero().equals(contaSelecionada.getNumero())) {
                        conta.atualizarSaldo(valor);
                        contaService.salvar(conta);
                        Transacao transacao = new Transacao();
                        transacao.setConta(conta);
                        transacao.setData(new Date());
                        transacao.setValor(valor);
                        transacao.setTipoOperacao("color : black");
                        transacaoService.salvar(transacao);
                        atualizarIdContaSelecionada();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Depósito realizado com sucesso", "O valor de R$" + valor + " foi depositado a sua conta."));
                        valor = null;

                        break;
                    }
                    break;
                }
            } else if (tipoOperacao.equals("Sacar")) {
                if(conta.getPessoa().getCpf().equals(pessoaSelecionada.getCpf())) {
                    if(conta.getNumero().equals(contaSelecionada.getNumero())) {
                        if (conta.getSaldo() >= valor) {
                            conta.atualizarSaldo(-valor);
                            contaService.salvar(conta);
                            Transacao transacao = new Transacao();
                            transacao.setConta(conta);
                            transacao.setData(new Date());
                            transacao.setValor(-valor);
                            transacao.setTipoOperacao("color: red");
                            transacaoService.salvar(transacao);
                            atualizarIdContaSelecionada();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Saque realizado com sucesso", "O valor de R$" + valor + " foi debitado da sua conta."));
                            valor = null;
                            break;
                        } else {
                            valor = null;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Saldo insuficiente", "O saldo da conta é inferior ao valor do saque."));
                        }
                        break;
                    }
                }
            }
        }
    }


    public String deletar(Conta conta) {
        contaService.deletar(conta);
        addMessage("Confirmed", "Record deleted");
        return "conta?faces-redirect=true";
    }

    public void editar(Conta conta) {

        pessoaSelecionada = conta.getPessoa();
        this.conta = conta;
    }


    public List<Conta> getContas() {
        return contaService.listarTodos();
    }

    public List<Transacao> getTransacoes() {
        transacoes = transacaoService.listarPorConta(idContaSelecionada);
        return transacoes.stream()
                .sorted(Comparator.comparing(Transacao::getData).reversed())
                .collect(Collectors.toList());
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
