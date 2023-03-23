package com.juliano.converters;

import com.juliano.models.Conta;
import com.juliano.services.ContaService;

import javax.annotation.*;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean
@RequestScoped
@FacesConverter(value = "contaConverter")
public class ContaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            ContaService contaService = context.getApplication().evaluateExpressionGet(context, "#{contaService}", ContaService.class);
            return contaService.buscarPorId(Long.parseLong(value));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Conta) {
            Conta conta = (Conta) value;
            return conta.getId().toString();
        }
        return null;
    }

}
