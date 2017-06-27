package com.example.developmentvmachine.serviciosweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development VMachine on 04/05/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WSCervezasResponse {

    private String status;
    private String mensaje;

    @JsonProperty("cervezas")
    private List<Cerveza> cervezasList;

    public WSCervezasResponse(){
        this.status = null;
        this.mensaje = null;
        List cervezas = new ArrayList<Cerveza>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Cerveza> getCervezas() {
        return cervezasList;
    }

    public void setCervezas(List<Cerveza> cervezas) {
        this.cervezasList = cervezas;
    }
}
