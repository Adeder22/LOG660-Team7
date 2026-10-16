package com.log660.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "FORFAIT")
public class Forfait {
    @Id
    @Column(name = "CODEFORFAIT", nullable = false)
    private Character id;

    @Column(name = "NOMFORFAIT", nullable = false, length = 20)
    private String nomforfait;

    @Column(name = "PRIX", nullable = false, precision = 5, scale = 2)
    private BigDecimal prix;

    @Column(name = "LOCATIONMAX", nullable = false)
    private Short locationmax;

    @Column(name = "DUREEMAX")
    private Short dureemax;

    public Character getId() {
        return id;
    }

    public void setId(Character id) {
        this.id = id;
    }

    public String getNomforfait() {
        return nomforfait;
    }

    public void setNomforfait(String nomforfait) {
        this.nomforfait = nomforfait;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Short getLocationmax() {
        return locationmax;
    }

    public void setLocationmax(Short locationmax) {
        this.locationmax = locationmax;
    }

    public Short getDureemax() {
        return dureemax;
    }

    public void setDureemax(Short dureemax) {
        this.dureemax = dureemax;
    }

}