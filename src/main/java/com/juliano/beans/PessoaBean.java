package com.juliano.beans;

import com.juliano.models.Pessoa;
import com.juliano.services.PessoaService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;


@ManagedBean
@RequestScope
public class PessoaBean {

    @Autowired
    private PessoaService pessoaService;


    @Getter
    @Setter
    private Pessoa pessoa = new Pessoa();


    public String salvar() {
        this.pessoa = pessoa;
        pessoaService.salvar(pessoa);
        addMessage("Confirmed", "You have accepted");
        pessoa = new Pessoa();
        return "pessoa?faces-redirect=true";
    }

    public String deletar(Pessoa pessoa) {
        pessoaService.deletar(pessoa);
        addMessage("Confirmed", "Record deleted");
        return "pessoa?faces-redirect=true";
    }

    public String editar(Pessoa pessoa) {
        this.pessoa = pessoa;
        return "pessoa";
    }

    public List<Pessoa> getPessoas() {

        return pessoaService.listarTodos();
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
