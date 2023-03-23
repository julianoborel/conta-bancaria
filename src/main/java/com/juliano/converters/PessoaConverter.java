package com.juliano.converters;

import com.juliano.models.Pessoa;
import com.juliano.services.PessoaService;

import javax.annotation.*;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean
@RequestScoped
@FacesConverter(value = "pessoaConverter")
public class PessoaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            PessoaService pessoaService = context.getApplication().evaluateExpressionGet(context, "#{pessoaService}", PessoaService.class);
            return pessoaService.buscarPorId(Long.parseLong(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Pessoa) {
            Pessoa pessoa = (Pessoa) value;
            return pessoa.getId().toString();
        }
        return null;
    }

}
