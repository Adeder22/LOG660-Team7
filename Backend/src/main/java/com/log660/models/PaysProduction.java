package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYS_PRODUCTION")
public class PaysProduction {
    @Id
    @Column(name = "NOMPAYS", nullable = false, length = 100)
    private String nompays;

    public String getNompays() {
        return nompays;
    }

    public void setNompays(String nompays) {
        this.nompays = nompays;
    }

    //TODO [Reverse Engineering] generate columns from DB
}