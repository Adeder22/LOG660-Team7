package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GENRE")
public class Genre {
    @Id
    @Column(name = "TYPEGENRE", nullable = false, length = 100)
    private String typegenre;

    public String getTypegenre() {
        return typegenre;
    }

    public void setTypegenre(String typegenre) {
        this.typegenre = typegenre;
    }

    //TODO [Reverse Engineering] generate columns from DB
}