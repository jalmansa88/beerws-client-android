package com.example.developmentvmachine.serviciosweb.model;

import java.math.BigDecimal;

/**
 * Created by Development VMachine on 04/05/2017.
 */

public class Cerveza {

    private int id;
    private String name;
    private String description;
    private String country;
    private String type;
    private String family;
    private BigDecimal alc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public BigDecimal getAlc() {
        return alc;
    }

    public void setAlc(BigDecimal alc) {
        this.alc = alc;
    }

    @Override
    public String toString() {
        return "Cerveza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", country='" + country + '\'' +
                ", type='" + type + '\'' +
                ", family='" + family + '\'' +
                ", alc=" + alc +
                '}';
    }
}
